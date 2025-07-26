document.addEventListener('DOMContentLoaded', () => {
    console.log('login-renderer.js: Script loaded.');

    const loginPhoneInput = document.getElementById('login-phone');
    const loginPasswordInput = document.getElementById('login-password');
    const loginButton = document.getElementById('login-button');
    const loginMessage = document.getElementById('login-message'); 

    const forgotPasswordLink = document.getElementById('forgot-password-link');
    const registerLink = document.getElementById('register-link');

    const TEST_PHONE_NUMBER = '5551112233'; 
    const TEST_PASSWORD = '12345';   

    function displayMessage(element, message, isSuccess) {
        element.textContent = message;
        element.classList.remove('message-success', 'message-error', 'hidden');
        element.classList.add(isSuccess ? 'message-success' : 'message-error');
        element.classList.add('show'); 
        console.log(`login-renderer.js: Displaying message: "${message}" (Success: ${isSuccess})`);
        setTimeout(() => {
            element.classList.remove('show');
            element.classList.add('hidden'); 
            console.log('login-renderer.js: Message hidden after 3 seconds.');
        }, 3000); 
    }

    function clearAndHideMessage(element) {
        element.textContent = '';
        element.classList.remove('show', 'message-success', 'message-error');
        element.classList.add('hidden');
        console.log('login-renderer.js: Message cleared and hidden.');
    }

    function validatePhoneNumber(phone) {
        const cleanPhone = phone.replace(/\D/g, '');
        
        if (cleanPhone.length === 10 && cleanPhone.startsWith('5')) {
            return cleanPhone;
        }
        
        if (cleanPhone.length === 12 && cleanPhone.startsWith('905')) {
            return cleanPhone.substring(2);
        }
        
        return null;
    }

    if (loginButton) {
        loginButton.addEventListener('click', async (e) => {
            e.preventDefault(); 
            clearAndHideMessage(loginMessage); 
            console.log('login-renderer.js: Login button clicked. Starting validation...');

            const phoneNumber = loginPhoneInput.value.trim(); 
            const password = loginPasswordInput.value.trim(); 

            if (!phoneNumber || !password) {
                displayMessage(loginMessage, 'Please enter your phone number and password.', false);
                console.log('login-renderer.js: Phone number or password is empty.');
                return;
            }

            const validatedPhone = validatePhoneNumber(phoneNumber);
            if (!validatedPhone) {
                displayMessage(loginMessage, 'Please enter a valid Turkish mobile number (10 digits starting with 5).', false);
                console.log('login-renderer.js: Invalid phone number format.');
                return;
            }

            if (validatedPhone === TEST_PHONE_NUMBER && password === TEST_PASSWORD) {
                displayMessage(loginMessage, 'Logging in with test mode...', true);
                console.log('login-renderer.js: Test mode enabled. Redirecting to usage.html.');
                setTimeout(() => {
                    const testUserData = {
                        name: "Test User",
                        surname: "Test Surname",
                        email: "test@example.com",
                        phone: TEST_PHONE_NUMBER,
                        nationalId: "12345678901",
                        customerId: 1,
                        usageData: { 
                            dataRemainingGB: 15,
                            dataTotalGB: 20,
                            minutesRemaining: 500,
                            minutesTotal: 750,
                            smsRemaining: 1000,
                            smsTotal: 1000
                        }
                    };
                    localStorage.setItem('currentUserUsage', JSON.stringify(testUserData));
                    console.log('login-renderer.js: Test user data saved to localStorage:', testUserData);

                    window.location.href = 'usage.html'; 
                    console.log('login-renderer.js: Redirected to usage.html.');
                }, 1000);
                return; 
            }

            const originalButtonText = loginButton.innerHTML;
            loginButton.innerHTML = 'Logging in...';
            loginButton.style.background = '#95a5a6';
            loginButton.disabled = true;
            console.log('login-renderer.js: Attempting login via API...');

            try {
                const result = await window.electronAPI.login({ 
                    phoneNumber: validatedPhone, 
                    password: password 
                });
                console.log('login-renderer.js: Login API response:', result);

                if (result.success) {
                    displayMessage(loginMessage, result.message, true);
                    console.log('login-renderer.js: Login successful, user data:', result.user);
                    
                    setTimeout(() => {
                        localStorage.setItem('currentUser', JSON.stringify(result.user));
                        console.log('login-renderer.js: User data saved to localStorage:', result.user);

                        window.location.href = 'usage.html'; 
                        console.log('login-renderer.js: Redirected to usage.html.');
                    }, 1000); 
                } else {
                    displayMessage(loginMessage, result.message, false);
                    console.log('login-renderer.js: Login failed:', result.message);
                }
            } catch (error) {
                console.error('login-renderer.js: An error occurred during login:', error);
                displayMessage(loginMessage, 'An unexpected error occurred. Please try again.', false);
            } finally {
                loginButton.innerHTML = originalButtonText;
                loginButton.style.background = '';
                loginButton.disabled = false;
            }
        });
    } else {
        console.error("login-renderer.js: 'login-button' element not found.");
    }

    if (forgotPasswordLink) {
        forgotPasswordLink.addEventListener('click', (e) => {
            e.preventDefault(); 
            console.log('login-renderer.js: "Forgot password" link clicked. Navigating to email-verification.html.');
            window.location.href = 'forgot-password.html'; 
        });
    } else {
        console.error("login-renderer.js: 'forgot-password-link' element not found.");
    }

    if (registerLink) {
        registerLink.addEventListener('click', (e) => {
            e.preventDefault(); 
            console.log('login-renderer.js: "Sign up" link clicked. Navigating to register.html.');
            window.location.href = 'register.html'; 
        });
    } else {
        console.error("login-renderer.js: 'register-link' element not found.");
    }

    const inputs = document.querySelectorAll('input');
    inputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.style.transform = 'translateY(-2px)';
            this.parentElement.style.transition = 'transform 0.2s ease';
        });
        input.addEventListener('blur', function() {
            this.parentElement.style.transform = '';
        });
    });

    if (loginPasswordInput) {
        loginPasswordInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                loginButton.click();
            }
        });
    }

    if (loginPhoneInput) {
        loginPhoneInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                loginPasswordInput.focus();
            }
        });
    }
});
