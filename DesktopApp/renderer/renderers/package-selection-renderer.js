document.addEventListener('DOMContentLoaded', () => {
    console.log('package-selection-renderer.js: DOMContentLoaded - Script loaded.');

    let selectedPackage = null;
    let userData = JSON.parse(sessionStorage.getItem('userData') || localStorage.getItem('userData') || '{}');
    let allPackages = []; 

    console.log('package-selection-renderer.js: User data from storage:', userData);

    loadPackagesFromBackend();

    document.getElementById('back-button').addEventListener('click', function () {
        console.log('package-selection-renderer.js: Back button clicked, navigating to register.html');
        window.location.href = 'register.html';
    });

    document.getElementById('signup-button').addEventListener('click', async function () {
        if (!selectedPackage) {
            showMessage('Please select a package', 'error');
            return;
        }

        console.log('package-selection-renderer.js: Signup button clicked with package:', selectedPackage);

        if (!userData.phoneNumber && !userData.phone) {
            showMessage('User phone number not found. Please register again.', 'error');
            setTimeout(() => {
                window.location.href = 'register.html';
            }, 2000);
            return;
        }

        const phoneNumber = userData.phoneNumber || userData.phone;

        setButtonLoading(this, true);

        try {
            console.log('package-selection-renderer.js: Selecting package with MSISDN:', phoneNumber, 'Package ID:', selectedPackage.id);
            
            const selectionResult = await window.electronAPI.selectPackage({
                msisdn: phoneNumber,
                packageId: selectedPackage.id
            });

            if (selectionResult.success) {
                showMessage('Package selected successfully! Account created. Redirecting to login...', 'success');
                console.log('package-selection-renderer.js: Package selection successful:', selectionResult.message);

                sessionStorage.removeItem('userData');
                localStorage.removeItem('userData');

                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 2000);
            } else {
                showMessage(selectionResult.message || 'Package selection failed. Please try again.', 'error');
                console.error('package-selection-renderer.js: Package selection failed:', selectionResult.message);
            }
        } catch (error) {
            console.error('package-selection-renderer.js: Error during package selection:', error);
            showMessage('An unexpected error occurred. Please try again.', 'error');
        } finally {
            setButtonLoading(this, false);
        }
    });

    async function loadPackagesFromBackend() {
        try {
            console.log('package-selection-renderer.js: Loading packages from backend...');
            
            const packagesGrid = document.querySelector('.packages-grid');
            packagesGrid.innerHTML = '<div style="text-align: center; padding: 40px; font-size: 18px; color: #666;">Loading packages...</div>';
            
            const result = await window.electronAPI.getPackages();
            
            if (result.success && result.data) {
                allPackages = result.data;
                console.log('package-selection-renderer.js: Packages loaded from backend:', allPackages);
                renderPackages(allPackages);
            } else {
                console.warn('package-selection-renderer.js: Failed to load packages from backend:', result.message);
                showMessage('Failed to load packages. Please refresh the page.', 'error');
                packagesGrid.innerHTML = '<div style="text-align: center; padding: 40px; font-size: 18px; color: #dc3545;">Failed to load packages. Please refresh the page.</div>';
            }
        } catch (error) {
            console.error('package-selection-renderer.js: Error loading packages from backend:', error);
            showMessage('Error connecting to server. Please check your connection.', 'error');
            const packagesGrid = document.querySelector('.packages-grid');
            packagesGrid.innerHTML = '<div style="text-align: center; padding: 40px; font-size: 18px; color: #dc3545;">Error connecting to server. Please refresh the page.</div>';
        }
    }

    function renderPackages(packages) {
        const packagesGrid = document.querySelector('.packages-grid');
        packagesGrid.innerHTML = '';

        packages.forEach(packageData => {
            const packageCard = createPackageCard(packageData);
            packagesGrid.appendChild(packageCard);
        });

        console.log('package-selection-renderer.js: Rendered', packages.length, 'packages');
    }

    function createPackageCard(packageData) {
        const card = document.createElement('div');
        card.className = 'package-card';
        card.dataset.packageId = packageData.id;
        card.dataset.price = packageData.price;

        const dataDisplay = formatDataAmount(packageData.dataQuota);
        const minutesDisplay = formatNumber(packageData.minutesQuota);
        const smsDisplay = formatNumber(packageData.smsQuota);

        card.innerHTML = `
            <div class="package-header">
                <h3 class="package-name">${packageData.name || packageData.packageName}</h3>
                <div class="package-price">
                    <span class="amount">${packageData.price}TL</span>/monthly
                </div>
            </div>
            <ul class="package-features">
                <li><span class="check-icon"></span>${dataDisplay} data</li>
                <li><span class="check-icon"></span>${minutesDisplay} minutes</li>
                <li><span class="check-icon"></span>${smsDisplay} SMS</li>
                <li><span class="check-icon"></span>${packageData.period} days validity</li>
            </ul>
        `;

        card.addEventListener('click', function () {
            document.querySelectorAll('.package-card').forEach(c => c.classList.remove('selected'));
            
            this.classList.add('selected');

            selectedPackage = {
                id: packageData.id,
                name: packageData.name || packageData.packageName,
                price: packageData.price,
                dataQuota: packageData.dataQuota,
                minutesQuota: packageData.minutesQuota,
                smsQuota: packageData.smsQuota,
                period: packageData.period
            };

            console.log('package-selection-renderer.js: Package selected:', selectedPackage);

            document.getElementById('signup-button').disabled = false;
        });

        return card;
    }

    function formatDataAmount(amount) {
        if (amount >= 1000) {
            return (amount / 1000) + 'GB';
        }
        return amount + 'MB';
    }

    function formatNumber(number) {
        return number.toLocaleString();
    }

    function showMessage(message, type) {
        const messageBox = document.getElementById('message');
        messageBox.textContent = message;
        messageBox.className = `message-box message-${type} show`;
        console.log(`package-selection-renderer.js: Displaying message: "${message}" (Type: ${type})`);

        if (!message.includes('Redirecting')) {
            setTimeout(() => {
                messageBox.className = 'message-box hidden';
                console.log('package-selection-renderer.js: Message hidden after timeout.');
            }, 5000);
        }
    }

    function setButtonLoading(button, isLoading) {
        if (isLoading) {
            button.disabled = true;
            button.textContent = 'Creating Account...';
            button.style.opacity = '0.6';
            console.log('package-selection-renderer.js: Button set to loading state');
        } else {
            button.disabled = selectedPackage ? false : true;
            button.textContent = 'Sign Up';
            button.style.opacity = '1';
            console.log('package-selection-renderer.js: Button loading state removed');
        }
    }
});