from __future__ import annotations

import base64
import hashlib
import json
import secrets
import time
import urllib.error
import urllib.parse
import urllib.request
import webbrowser
from http.server import BaseHTTPRequestHandler, HTTPServer
from typing import Any


KEYCLOAK_BASE_URL = "http://localhost:8081"
REALM = "meet-me-at"
CLIENT_ID = "meet-me-at-web"

REDIRECT_HOST = "localhost"
REDIRECT_PORT = 5173
REDIRECT_PATH = "/auth/callback"
REDIRECT_URI = (
    f"http://{REDIRECT_HOST}:{REDIRECT_PORT}{REDIRECT_PATH}"
)

GATEWAY_BASE_URL = "http://localhost:8080"

AUTHORIZATION_ENDPOINT = (
    f"{KEYCLOAK_BASE_URL}/realms/{REALM}"
    "/protocol/openid-connect/auth"
)

TOKEN_ENDPOINT = (
    f"{KEYCLOAK_BASE_URL}/realms/{REALM}"
    "/protocol/openid-connect/token"
)

EXPECTED_ISSUER = f"{KEYCLOAK_BASE_URL}/realms/{REALM}"


def base64url(data: bytes) -> str:
    return base64.urlsafe_b64encode(data).rstrip(b"=").decode("ascii")


def random_base64url(byte_count: int = 32) -> str:
    return base64url(secrets.token_bytes(byte_count))


def read_jwt_payload(token: str) -> dict[str, Any]:
    parts = token.split(".")

    if len(parts) != 3:
        raise RuntimeError("Expected a three-part JWT")

    encoded_payload = parts[1]
    encoded_payload += "=" * (-len(encoded_payload) % 4)

    payload = base64.urlsafe_b64decode(encoded_payload)
    return json.loads(payload.decode("utf-8"))


def exchange_code(code: str, code_verifier: str) -> dict[str, Any]:
    body = urllib.parse.urlencode(
        {
            "grant_type": "authorization_code",
            "client_id": CLIENT_ID,
            "redirect_uri": REDIRECT_URI,
            "code": code,
            "code_verifier": code_verifier,
        }
    ).encode("utf-8")

    request = urllib.request.Request(
        TOKEN_ENDPOINT,
        data=body,
        method="POST",
        headers={
            "Content-Type": "application/x-www-form-urlencoded",
        },
    )

    try:
        with urllib.request.urlopen(request, timeout=15) as response:
            return json.load(response)
    except urllib.error.HTTPError as error:
        details = error.read().decode("utf-8", errors="replace")
        raise RuntimeError(
            f"Token exchange failed with HTTP {error.code}: {details}"
        ) from error


def request_status(url: str, access_token: str | None = None) -> int:
    headers = {}

    if access_token is not None:
        headers["Authorization"] = f"Bearer {access_token}"

    request = urllib.request.Request(url, headers=headers)

    try:
        with urllib.request.urlopen(request, timeout=15) as response:
            return response.status
    except urllib.error.HTTPError as error:
        return error.code


def expect_status(
    description: str,
    url: str,
    expected: int,
    access_token: str | None = None,
) -> None:
    actual = request_status(url, access_token)
    marker = "PASS" if actual == expected else "FAIL"

    print(f"[{marker}] {description}: expected {expected}, received {actual}")

    if actual != expected:
        raise RuntimeError(f"Smoke test failed: {description}")


def receive_authorization_code(
    expected_state: str,
    timeout_seconds: int = 180,
) -> str:
    result: dict[str, str] = {}

    class CallbackHandler(BaseHTTPRequestHandler):
        def do_GET(self) -> None:
            parsed_url = urllib.parse.urlparse(self.path)

            if parsed_url.path != REDIRECT_PATH:
                self.send_error(404)
                return

            parameters = urllib.parse.parse_qs(parsed_url.query)

            result["state"] = parameters.get("state", [""])[0]
            result["code"] = parameters.get("code", [""])[0]
            result["error"] = parameters.get("error", [""])[0]
            result["error_description"] = parameters.get(
                "error_description",
                [""],
            )[0]

            response = (
                "<html><body>"
                "<h1>Authentication received</h1>"
                "<p>You can close this browser tab.</p>"
                "</body></html>"
            ).encode("utf-8")

            self.send_response(200)
            self.send_header("Content-Type", "text/html; charset=utf-8")
            self.send_header("Content-Length", str(len(response)))
            self.end_headers()
            self.wfile.write(response)

        def log_message(self, format: str, *args: object) -> None:
            pass

    try:
        server = HTTPServer(
            (REDIRECT_HOST, REDIRECT_PORT),
            CallbackHandler,
        )
    except OSError as error:
        raise RuntimeError(
            f"Could not listen on {REDIRECT_URI}. "
            f"Ensure port {REDIRECT_PORT} is free."
        ) from error

    server.timeout = 1
    deadline = time.monotonic() + timeout_seconds

    try:
        while "code" not in result and "error" not in result:
            if time.monotonic() >= deadline:
                raise TimeoutError("Timed out waiting for Keycloak callback")

            server.handle_request()
    finally:
        server.server_close()

    if result.get("error"):
        raise RuntimeError(
            "Authorization failed: "
            f"{result['error']} - {result.get('error_description', '')}"
        )

    if not secrets.compare_digest(
        result.get("state", ""),
        expected_state,
    ):
        raise RuntimeError("OAuth state mismatch")

    code = result.get("code")

    if not code:
        raise RuntimeError("Keycloak did not return an authorization code")

    return code


def main() -> None:
    code_verifier = random_base64url()
    state = random_base64url()
    nonce = random_base64url()

    code_challenge = base64url(
        hashlib.sha256(code_verifier.encode("ascii")).digest()
    )

    authorization_url = (
        f"{AUTHORIZATION_ENDPOINT}?"
        + urllib.parse.urlencode(
            {
                "client_id": CLIENT_ID,
                "redirect_uri": REDIRECT_URI,
                "response_type": "code",
                "scope": "openid profile email",
                "code_challenge": code_challenge,
                "code_challenge_method": "S256",
                "state": state,
                "nonce": nonce,
            }
        )
    )

    print("Opening Keycloak login...")
    webbrowser.open(authorization_url)

    authorization_code = receive_authorization_code(state)
    token_response = exchange_code(authorization_code, code_verifier)

    access_token = token_response["access_token"]
    access_claims = read_jwt_payload(access_token)
    id_claims = read_jwt_payload(token_response["id_token"])

    if id_claims.get("nonce") != nonce:
        raise RuntimeError("ID token nonce mismatch")

    if access_claims.get("iss") != EXPECTED_ISSUER:
        raise RuntimeError("Access token issuer mismatch")

    if access_claims.get("sub") != id_claims.get("sub"):
        raise RuntimeError("Access and ID token subjects do not match")

    audience = access_claims.get("aud", [])

    if isinstance(audience, str):
        audience = [audience]

    if "meet-me-at-api" not in audience:
        raise RuntimeError(
            f"Expected meet-me-at-api audience, received: {audience}"
        )

    print(f"Token audiences: {', '.join(audience)}")

    username = access_claims.get("preferred_username", "<unknown>")
    print(f"Authenticated as: {username}")
    print()

    expect_status(
        "Public health endpoint",
        f"{GATEWAY_BASE_URL}/actuator/health",
        200,
    )

    expect_status(
        "Unknown public path",
        f"{GATEWAY_BASE_URL}/does-not-exist",
        404,
    )

    protected_test_url = (
        f"{GATEWAY_BASE_URL}/api/__auth-smoke-test__"
    )

    expect_status(
        "Protected path without token",
        protected_test_url,
        401,
    )

    expect_status(
        "Protected path with valid token",
        protected_test_url,
        404,
        access_token,
    )

    print()
    print("All gateway authentication smoke tests passed.")


if __name__ == "__main__":
    main()