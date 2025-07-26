document.addEventListener('DOMContentLoaded', () => {
    console.log('register-renderer.js: DOMContentLoaded - Script loaded.');

    const registerFirstNameInput = document.getElementById('name');
    const registerLastNameInput = document.getElementById('surname');
    const registerPhoneInput = document.getElementById('phone');
    const registerEmailInput = document.getElementById('email');
    const registerNationalIdInput = document.getElementById('national-id');
    const registerPasswordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirm-password');
    
    const registerButton = document.getElementById('signup-button');
    const registerMessage = document.getElementById('signup-message');
    const backToLoginFromRegister = document.getElementById('login-page-link');

    const reqLength = document.getElementById('req-length');
    const reqNumber = document.getElementById('req-number');
    const reqUppercase = document.getElementById('req-uppercase');
    const reqSymbol = document.getElementById('req-symbol');

    function showNotification(message, isSuccess) {
        registerMessage.textContent = message;
        registerMessage.classList.remove('hidden', 'message-success', 'message-error');
        registerMessage.classList.add(isSuccess ? 'message-success' : 'message-error');
        registerMessage.classList.add('show');
        console.log(`register-renderer.js: Displaying message: "${message}" (Success: ${isSuccess})`);
        
        const timeout = isSuccess ? 500 : 1000;
        setTimeout(() => {
            registerMessage.classList.remove('show');
            registerMessage.classList.add('hidden');
            console.log('register-renderer.js: Message hidden after timeout.');
        }, timeout);
    }

    const validatePassword = () => {
        const password = registerPasswordInput.value;
        let isValid = true;

        if (password.length >= 8) {
            reqLength.classList.remove('invalid');
            reqLength.classList.add('valid');
        } else {
            reqLength.classList.remove('valid');
            reqLength.classList.add('invalid');
            isValid = false;
        }

        if (/[0-9]/.test(password)) {
            reqNumber.classList.remove('invalid');
            reqNumber.classList.add('valid');
        } else {
            reqNumber.classList.remove('valid');
            reqNumber.classList.add('invalid');
            isValid = false;
        }

        if (/[A-Z]/.test(password)) {
            reqUppercase.classList.remove('invalid');
            reqUppercase.classList.add('valid');
        } else {
            reqUppercase.classList.remove('valid');
            reqUppercase.classList.add('invalid');
            isValid = false;
        }

        if (/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/.test(password)) {
            reqSymbol.classList.remove('invalid');
            reqSymbol.classList.add('valid');
        } else {
            reqSymbol.classList.remove('valid');
            reqSymbol.classList.add('invalid');
            isValid = false;
        }

        return isValid;
    };

    if (registerPasswordInput) {
        registerPasswordInput.addEventListener('input', validatePassword);
    } else {
        console.error("register-renderer.js: registerPasswordInput (ID 'password') not found.");
    }
    
    if (registerNationalIdInput) {
        registerNationalIdInput.addEventListener('input', function (e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 11) {
                value = value.slice(0, 11);
            }
            e.target.value = value;
        });
    } else {
        console.error("register-renderer.js: registerNationalIdInput (ID 'national-id') not found.");
    }

    if (registerPhoneInput) {
        registerPhoneInput.addEventListener('input', function (e) {
            e.target.value = e.target.value.replace(/\D/g, '');
        });
    } else {
        console.error("register-renderer.js: registerPhoneInput (ID 'phone') not found.");
    }

    function clearAndHideMessage(element) {
        element.textContent = '';
        element.classList.add('hidden');
        element.classList.remove('message-success', 'message-error', 'show');
        console.log('register-renderer.js: Message cleared and hidden.');
    }

    function setButtonLoading(button, isLoading) {
        if (isLoading) {
            button.disabled = true;
            button.textContent = 'Registering...';
            button.style.opacity = '0.6';
        } else {
            button.disabled = false;
            button.textContent = 'Sign Up';
            button.style.opacity = '1';
        }
    }

    if (registerButton) {
        registerButton.addEventListener('click', async (e) => {
            e.preventDefault(); 
            clearAndHideMessage(registerMessage);
            console.log('register-renderer.js: Register button clicked. Starting client-side validation...');

            const firstName = registerFirstNameInput.value.trim();
            const lastName = registerLastNameInput.value.trim();
            const phoneNumber = registerPhoneInput.value.trim();
            const email = registerEmailInput.value.trim();
            const nationalId = registerNationalIdInput.value.trim();
            const password = registerPasswordInput.value;
            const confirmPassword = confirmPasswordInput.value;

            if (!firstName || !lastName || !phoneNumber || !email || !nationalId || !password || !confirmPassword) {
                showNotification('Please fill in all required fields.', false);
                console.log('register-renderer.js: Validation failed: Missing required fields.');
                return;
            }

            if (!/\S+@\S+\.\S+/.test(email)) {
                showNotification('Please enter a valid email address.', false);
                console.log('register-renderer.js: Validation failed: Invalid email format.');
                return;
            }

            const isPasswordStrong = validatePassword();
            if (!isPasswordStrong) {
                showNotification('Password does not meet all requirements (at least 8 characters, one number, one uppercase, one symbol).', false);
                console.log('register-renderer.js: Validation failed: Password not strong enough.');
                return;
            }

            if (password !== confirmPassword) {
                showNotification('Passwords do not match.', false);
                console.log('register-renderer.js: Validation failed: Passwords do not match.');
                return;
            }

            if (!/^\d{10}$/.test(phoneNumber)) {
                showNotification('Please enter a valid 10-digit phone number (e.g., 5XXXXXXXXX).', false);
                console.log('register-renderer.js: Validation failed: Invalid phone number format.');
                return;
            }

            if (!/^\d{11}$/.test(nationalId)) {
                showNotification('Please enter a valid 11-digit National ID.', false);
                console.log('register-renderer.js: Validation failed: Invalid National ID format.');
                return;
            }

            console.log('register-renderer.js: All client-side validations passed. Calling registration API...');

            setButtonLoading(registerButton, true);

            try {
                const result = await window.electronAPI.register({
                    firstName: firstName,
                    lastName: lastName,
                    phoneNumber: phoneNumber,
                    email: email,
                    nationalId: nationalId,
                    password: password
                });

                console.log('register-renderer.js: Registration API result:', result);

                if (result.success) {
                    const userDataForStorage = {
                        firstName: firstName,
                        lastName: lastName,
                        phoneNumber: phoneNumber,
                        email: email,
                        nationalId: nationalId
                    };
                    
                    sessionStorage.setItem('userData', JSON.stringify(userDataForStorage));
                    localStorage.setItem('userData', JSON.stringify(userDataForStorage));
                    
                    showNotification('Registration successful! Redirecting to package selection...', true);
                    console.log('package-selection-renderer.js: Registration successful. User data stored:', userDataForStorage);
                    
                    setTimeout(() => {
                        window.location.href = '../html/package-selection.html';
                    }, 2000);
                } else {
                    showNotification(result.message || 'Registration failed. Please try again.', false);
                    console.log('register-renderer.js: Registration failed:', result.message);
                }
            } catch (error) {
                console.error('register-renderer.js: Error during registration:', error);
                showNotification('An unexpected error occurred. Please try again.', false);
            } finally {
                setButtonLoading(registerButton, false);
            }
        });
    } else {
        console.error("register-renderer.js: registerButton (ID 'signup-button') not found.");
    }

    if (backToLoginFromRegister) {
        backToLoginFromRegister.addEventListener('click', (e) => {
            e.preventDefault();
            console.log('register-renderer.js: "Log in" link clicked. Navigating to login.html...');
            window.location.href = '../html/login.html'; 
        });
    } else {
        console.error("register-renderer.js: backToLoginFromRegister (ID 'login-page-link') not found.");
    }
});
