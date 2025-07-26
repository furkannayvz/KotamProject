document.addEventListener('DOMContentLoaded', () => {
    console.log('forgot-password-renderer.js loaded.');
    
    const forgotPasswordMessage = document.getElementById('forgot-password-message');
    const loginPageLink = document.getElementById('login-page-link');
    const sendButton = document.getElementById('send-button');

    function displayMessage(element, message, isSuccess) {
        element.textContent = message;
        element.classList.remove('hidden', 'message-success', 'message-error');
        element.classList.add(isSuccess ? 'message-success' : 'message-error');
        element.classList.add('show');
        setTimeout(() => {
            element.classList.remove('show');
            element.classList.add('hidden');
        }, 5000);
    }

    function clearAndHideMessage(element) {
        element.textContent = '';
        element.classList.add('hidden');
        element.classList.remove('message-success', 'message-error');
    }

    const nationalIdInput = document.getElementById('national-id');
    if (nationalIdInput) {
        nationalIdInput.addEventListener('input', function (e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 11) {
                value = value.slice(0, 11);
            }
            e.target.value = value;
        });
    }

    if (sendButton) {
        sendButton.addEventListener('click', async (e) => {
            e.preventDefault();
            clearAndHideMessage(forgotPasswordMessage);
            console.log('forgot-password-renderer.js: Send button clicked.');

            const emailInput = document.getElementById('email');
            const nationalIdInput = document.getElementById('national-id');

            const email = emailInput.value.trim();
            const nationalId = nationalIdInput.value.trim();

            if (!email || !nationalId) {
                displayMessage(forgotPasswordMessage, 'Please enter your email and national ID', false);
                return;
            }
            if (!/\S+@\S+\.\S+/.test(email)) {
                displayMessage(forgotPasswordMessage, 'Enter a valid email address', false);
                return;
            }
            if (!/^\d{11}$/.test(nationalId)) {
                displayMessage(forgotPasswordMessage, 'Enter a valid national ID (11 digits)', false);
                return;
            }

            sendButton.disabled = true;
            sendButton.textContent = 'Checking user...';

            try {
                console.log('forgot-password-renderer.js: Checking if customer exists');
                const customerCheck = await window.electronAPI.checkCustomerExists({ email, nationalId });
                console.log('forgot-password-renderer.js: Customer existence check response:', customerCheck);
                
                if (!customerCheck.success) {
                    displayMessage(forgotPasswordMessage, customerCheck.message || 'Error checking user information', false);
                    return;
                }

                if (!customerCheck.exists) {
                    displayMessage(forgotPasswordMessage, 'No account found with this email and national ID', false);
                    return;
                }

                sendButton.textContent = 'Sending email...';
                console.log('forgot-password-renderer.js: Customer exists, calling forgot password API');
                const result = await window.electronAPI.forgotPassword({ email, nationalId });
                console.log('forgot-password-renderer.js: Password reset API response:', result);
                
                if (result.success) {
                    displayMessage(forgotPasswordMessage, result.message || 'Verification code sent to your email!', true);
                    
                    sessionStorage.setItem('forgotPasswordEmail', email);
                    sessionStorage.setItem('forgotPasswordNationalId', nationalId);
                    
                    setTimeout(() => {
                        window.location.href = 'email-verification.html';
                    }, 1500);
                } else {
                    displayMessage(forgotPasswordMessage, result.message || 'Failed to send verification code', false);
                }
            } catch (error) {
                console.error('forgot-password-renderer.js: Error during password reset process:', error);
                displayMessage(forgotPasswordMessage, 'Error during password reset. Please try again.', false);
            } finally {
                sendButton.disabled = false;
                sendButton.textContent = 'Send Mail';
            }
        });
    } else {
        console.error("forgot-password-renderer.js: 'send-button' ID could not be found");
    }

    if (loginPageLink) {
        loginPageLink.addEventListener('click', function (e) {
            e.preventDefault();
            window.location.href = 'login.html';
        });
    } else {
        console.error("forgot-password-renderer.js: 'login-page-link' ID could not be found.");
    }
});
