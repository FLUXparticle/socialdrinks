(() => {
    'use strict';

    const loginForm = document.getElementById('login-form');
    const loginStatus = document.getElementById('login-status');
    const textSection = document.getElementById('text-section');
    const userText = document.getElementById('user-text');
    const saveStatus = document.getElementById('save-status');

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
            textSection.classList.remove('hidden');
            await loadMyText();
        } catch (error) {
            loginStatus.textContent = error.message;
            window.currentToken = null;
            textSection.classList.add('hidden');
        }
    });

    async function loadMyText() {
        if (!window.currentToken) {
            return;
        }

        try {
            const response = await fetch('/auth/good/me', {
                method: 'GET',
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Could not load note');
            }

            const data = await response.json();
            userText.value = data.text || '';
        } catch (error) {
            saveStatus.textContent = error.message;
        }
    }

    document.getElementById('save-text').addEventListener('click', async () => {
        if (!window.currentToken) {
            saveStatus.textContent = 'Please login first.';
            return;
        }

        try {
            const response = await fetch('/auth/good/me', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify({ text: userText.value })
            });

            if (!response.ok) {
                throw new Error('Could not save note');
            }

            saveStatus.textContent = 'Note saved.';
        } catch (error) {
            saveStatus.textContent = error.message;
        }
    });
})();
