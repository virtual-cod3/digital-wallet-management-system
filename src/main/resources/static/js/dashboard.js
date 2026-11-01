
// API CONFIGURATION

const API_BASE_URL = "/api";

const token = localStorage.getItem("token");
const userId = localStorage.getItem("userId");


// CHECK LOGIN

if (!token || !userId) {
    window.location.href = "login.html";
}


// COMMON API FUNCTION

async function walletRequest(endpoint, method = "GET", data = null) {

    const options = {
        method: method,
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    };

    if (data !== null) {
        options.body = JSON.stringify(data);
    }

    const response = await fetch(
        `${API_BASE_URL}${endpoint}`,
        options
    );

    const result = await response.json();

    if (!response.ok) {
        throw new Error(
            result.error || Object.values(result).join(", ") || "Request failed"
        );
    }

    return result;
}


// DISPLAY WELCOME MESSAGE

const welcomeUser = document.getElementById("welcomeUser");

if (welcomeUser) {
    welcomeUser.textContent =
        `Welcome, ${localStorage.getItem("fullName") || "User"}!`;
}


// DISPLAY WALLET BALANCE

async function loadBalance() {

    try {

        const wallet = await walletRequest(
            `/wallets/${userId}/balance`
        );

        document.getElementById("balance").textContent =
            Number(wallet.balance).toLocaleString("en-IN", {
                style: "currency",
                currency: "INR"
            });

    } catch (error) {

        showMessage(error.message, "red");
    }
}


// DISPLAY MESSAGE

function showMessage(message, color) {

    const messageElement = document.getElementById("message");

    messageElement.textContent = message;
    messageElement.style.color = color;
}


// DEPOSIT MONEY

document.getElementById("depositForm")
    .addEventListener("submit", async function(event) {

        event.preventDefault();

        const amount = Number(
            document.getElementById("depositAmount").value
        );

        try {

            await walletRequest(
                `/wallets/${userId}/deposit`,
                "POST",
                { amount: amount }
            );

            showMessage("Money deposited successfully!", "green");

            this.reset();

            await loadBalance();
            await loadTransactions();

        } catch (error) {

            showMessage(error.message, "red");
        }

    });


// WITHDRAW MONEY

document.getElementById("withdrawForm")
    .addEventListener("submit", async function(event) {

        event.preventDefault();

        const amount = Number(
            document.getElementById("withdrawAmount").value
        );

        try {

            await walletRequest(
                `/wallets/${userId}/withdraw`,
                "POST",
                { amount: amount }
            );

            showMessage("Money withdrawn successfully!", "green");

            this.reset();

            await loadBalance();
            await loadTransactions();

        } catch (error) {

            showMessage(error.message, "red");
        }

    });


// TRANSFER MONEY

document.getElementById("transferForm")
    .addEventListener("submit", async function(event) {

        event.preventDefault();

        const receiverUserId = Number(
            document.getElementById("receiverUserId").value
        );

        const amount = Number(
            document.getElementById("transferAmount").value
        );

        try {

            await walletRequest(
                `/wallets/${userId}/transfer`,
                "POST",
                {
                    receiverUserId: receiverUserId,
                    amount: amount
                }
            );

            showMessage("Money transferred successfully!", "green");

            this.reset();

            await loadBalance();
            await loadTransactions();

        } catch (error) {

            showMessage(error.message, "red");
        }

    });


// LOAD TRANSACTION HISTORY

async function loadTransactions() {

    const table = document.getElementById("transactionTable");

    try {

        const transactions = await walletRequest(
            `/wallets/${userId}/transactions`
        );

        table.innerHTML = "";

        if (transactions.length === 0) {

            table.innerHTML = `
                <tr>
                    <td colspan="4">No transactions found.</td>
                </tr>
            `;

            return;
        }

        transactions.forEach(transaction => {

            const row = document.createElement("tr");

            const idCell = document.createElement("td");
            idCell.textContent = transaction.id;

            const typeCell = document.createElement("td");
            typeCell.textContent = transaction.type;

            const amountCell = document.createElement("td");
            amountCell.textContent =
                Number(transaction.amount).toLocaleString("en-IN", {
                    style: "currency",
                    currency: "INR"
                });

            const dateCell = document.createElement("td");
            dateCell.textContent =
                new Date(transaction.dateTime).toLocaleString("en-IN");

            row.appendChild(idCell);
            row.appendChild(typeCell);
            row.appendChild(amountCell);
            row.appendChild(dateCell);

            table.appendChild(row);

        });

    } catch (error) {

        table.innerHTML = `
            <tr>
                <td colspan="4">Unable to load transactions.</td>
            </tr>
        `;

        console.error(error);
    }
}


// LOGOUT

document.getElementById("logoutBtn")
    .addEventListener("click", function() {

        localStorage.removeItem("token");
        localStorage.removeItem("userId");
        localStorage.removeItem("fullName");
        localStorage.removeItem("email");

        window.location.href = "login.html";

    });


// INITIAL DASHBOARD LOADING

if (token && userId) {
    loadBalance();
    loadTransactions();
}
