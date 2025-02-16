async function login() {
    const form = document.getElementById('loginForm');
    const formData = new FormData(form);
    const json = JSON.stringify(Object.fromEntries(formData.entries()));
    const errorMessage = document.getElementById('error-message');

    const response = await fetch('/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: json,
    });

    if (!response.ok) {
        errorMessage.textContent = 'Client-Fehler: ' + response.statusText;
    } else {
        const result = await response.json();
        if (result.error) {
            errorMessage.textContent = 'Server-Fehler: ' + result.error;
        } else {
            // Login erfolgreich, umleiten zur Hauptanwendung
            window.location.href = '/drinks/';
        }
    }
}

document.getElementById("loginForm").onsubmit = (event) => {
    event.preventDefault();
    login();
};
