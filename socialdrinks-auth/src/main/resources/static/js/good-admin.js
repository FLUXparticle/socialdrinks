(() => {
    'use strict';

    const loginForm = document.getElementById('login-form');
    const loginStatus = document.getElementById('login-status');
    const overviewSection = document.getElementById('overview-section');
    const notesOutput = document.getElementById('notes-output');
    const refreshButton = document.getElementById('refresh-button');

    window.currentToken = null;

    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            const response = await fetch('/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({ username, password })
            });

            if (!response.ok) {
                throw new Error('Login failed');
            }

            const data = await response.json();
            window.currentToken = data.token;
            loginStatus.textContent = 'Login successful.';
            overviewSection.classList.remove('hidden');
            await loadAllNotes();
        } catch (error) {
            loginStatus.textContent = error.message;
            window.currentToken = null;
            overviewSection.classList.add('hidden');
        }
    });

    refreshButton.addEventListener('click', async () => {
        await loadAllNotes();
    });

    async function loadAllNotes() {
        if (!window.currentToken) {
            return;
        }

        try {
            const response = await fetch('/auth/good/all', {
                method: 'GET',
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Could not load notes');
            }

            const data = await response.json();

            const rows = Object.entries(data).map(([user, text]) => (
                `<tr><td>${user}</td><td>${text || ''}</td></tr>`
            )).join('');

            notesOutput.innerHTML = `
                <table>
                    <thead>
                        <tr><th>User</th><th>Text</th></tr>
                    </thead>
                    <tbody>${rows}</tbody>
                </table>
            `;
        } catch (error) {
            notesOutput.textContent = error.message;
        }
    }
})();
