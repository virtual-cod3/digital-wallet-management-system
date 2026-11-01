// Updated API configuration for Render deployment

const API_BASE_URL = "/api";

// REGISTRATION

const registerForm = document.getElementById("registerForm");

if (registerForm) {

    registerForm.addEventListener("submit", async function(event) {

        event.preventDefault();

        const fullName = document.getElementById("fullName").value;
        const email = document.getElementById("email").value;
        const phoneNumber = document.getElementById("phoneNumber").value;
        const password = document.getElementById("password").value;

        const message = document.getElementById("message");

        const userData = {
            fullName: fullName,
            email: email,
            phoneNumber: phoneNumber,
            password: password
        };

        try {

            const response = await fetch(`${API_BASE_URL}/users/register`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(userData)
            });

            const result = await response.json();

            if (response.ok) {

                message.textContent = "Registration successful! Please login.";
                message.style.color = "green";

                registerForm.reset();

            } else {

                message.textContent =
                    result.error || Object.values(result).join(", ") || "Registration failed";

                message.style.color = "red";
            }

        } catch (error) {

            message.textContent = "Unable to connect to the server.";
            message.style.color = "red";

            console.error(error);
        }

    });

}



// LOGIN

const loginForm = document.getElementById("loginForm");

if (loginForm) {

    loginForm.addEventListener("submit", async function(event) {

        event.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const message = document.getElementById("message");
        const loginButton = loginForm.querySelector('button[type="submit"]');

        const loginData = {
            email: email,
            password: password
        };

        // Show loading state
        const originalButtonText = loginButton.textContent;
        loginButton.disabled = true;
        loginButton.textContent = "Logging in...";

        message.textContent = "";

        try {

            const response = await fetch(`${API_BASE_URL}/users/login`, {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(loginData)

            });

            const result = await response.json();

            if (response.ok) {

                localStorage.setItem("token", result.token);
                localStorage.setItem("userId", result.userId);
                localStorage.setItem("fullName", result.fullName);
                localStorage.setItem("email", result.email);

                message.textContent = "Login successful!";
                message.style.color = "green";

                window.location.href = "dashboard.html";

            } else {

                message.textContent =
                    result.error || "Invalid email or password";

                message.style.color = "red";

            }

        } catch (error) {

            message.textContent = "Unable to connect to the server.";
            message.style.color = "red";

            console.error(error);

        } finally {

            loginButton.disabled = false;
            loginButton.textContent = originalButtonText;

        }

    });

}
