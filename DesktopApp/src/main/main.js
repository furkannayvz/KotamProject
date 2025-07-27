const { app, BrowserWindow, ipcMain, Menu } = require('electron');
const path = require('node:path');

let mainWindow;

const BASE_URL = 'http://34.32.107.243:8080/api'; 

function createWindow() {
    mainWindow = new BrowserWindow({
        width: 1200,
        height: 800,
        minWidth: 1200,
        minHeight: 800,
        frame: true,
        webPreferences: {
            preload: path.join(__dirname, 'preload.js'),
            contextIsolation: true,
            nodeIntegration: false,
        },
    });

    Menu.setApplicationMenu(null);

    mainWindow.loadFile('renderer/html/login.html');
}

app.whenReady().then(() => {
    createWindow();

    app.on('activate', () => {
        if (BrowserWindow.getAllWindows().length === 0) {
            createWindow();
        }
    });
});

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit();
    }
});

ipcMain.handle('forgot-password', async (event, { email, nationalId }) => {
    console.log(`main.js: Forgot password attempt: Email: ${email}, National ID: ${nationalId}`);
    try {
        const response = await fetch(`${BASE_URL}/auth/forgot-password`, { 
            method: 'POST',
            headers: {
                'Content-Type': 'application/json', 
            },
            body: JSON.stringify({ 
                email: email,
                nationalId: nationalId
            })
        });

        if (response.ok) {
            const message = await response.text();
            console.log('main.js: Forgot password successful:', message);
            return { success: true, message: message };
        } else {
            const errorText = await response.text();
            console.error('main.js: Forgot password unsuccessful:', errorText);
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Network error during forgot password:', error);
        return {
            success: false,
            message: 'Unable to connect to server. Please try again later.'
        };
    }
});

ipcMain.handle('verify-code', async (event, { email, code }) => {
    console.log(`main.js: Verifying code for Email: ${email}, Code: ${code}`);
    try {
        const response = await fetch(`${BASE_URL}/auth/verify-code`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email: email,
                code: code
            })
        });

        if (response.ok) {
            const message = await response.text();
            console.log('main.js: Code verification successful:', message);
            return { success: true, message: message };
        } else {
            const errorText = await response.text();
            console.error('main.js: Code verification unsuccessful:', errorText);
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Network error during code verification:', error);
        return {
            success: false,
            message: 'Unable to connect to server. Please try again later.'
        };
    }
});

ipcMain.handle('reset-password', async (event, { email, nationalId, code, newPassword, confirmPassword }) => { 
    console.log(`main.js: Password reset attempt: Email: ${email}`);
    try {
        const response = await fetch(`${BASE_URL}/auth/reset-password`, { 
            method: 'POST', 
            headers: {
                'Content-Type': 'application/json', 
            },
            body: JSON.stringify({ 
                email: email,
                nationalId: nationalId,
                code: code, 
                newPassword: newPassword,
                confirmPassword: confirmPassword
            })
        });

        if (response.ok) {
            const message = await response.text();
            console.log('main.js: Password reset successful:', message);
            return { success: true, message: message };
        } else {
            const errorText = await response.text();
            console.error('main.js: Password reset unsuccessful:', errorText);
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Network error during password reset:', error);
        return {
            success: false,
            message: 'Unable to connect to server. Please try again later.'
        };
    }
});

ipcMain.handle('login', async (event, { phoneNumber, password }) => {
    console.log(`main.js: Login attempt: Phone: ${phoneNumber}`);
    try {
        const response = await fetch(`${BASE_URL}/customer/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                msisdn: phoneNumber,
                password: password
            })
        });

        if (response.ok) {
            const customerData = await response.json();
            console.log('main.js: Login successful:', customerData);

            return {
                success: true,
                message: 'Login successful!',
                user: {
                    name: customerData.name,
                    surname: customerData.surname,
                    email: customerData.email,
                    phone: customerData.msisdn,
                    nationalId: customerData.nationalId,
                    customerId: customerData.customerId,
                    packageEntity: customerData.packageEntity, 
                    sdate: customerData.sdate 
                }
            };
        } else {
            const errorText = await response.text();
            console.error('main.js: Login unsuccessful:', errorText);
            return {
                success: false,
                message: response.status === 401 ? 'Invalid phone number or password' : errorText
            };
        }
    } catch (error) {
        console.error('main.js: Connection error during login:', error);
        return {
            success: false,
            message: 'Unable to connect to server. Please check your internet connection.'
        };
    }
});

ipcMain.handle('getUserPackageByMsisdn', async (event, msisdn) => {
    console.log(`main.js: Fetching package for MSISDN: ${msisdn}`);
    try {
        const response = await fetch(`${BASE_URL}/customer/${msisdn}`, { 
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const customerData = await response.json(); 
            console.log('main.js: Full customer data response:', JSON.stringify(customerData, null, 2));
            console.log('main.js: Package entity:', customerData.packageEntity);
            console.log('main.js: Start date:', customerData.sDate || customerData.sdate || customerData.getsDate);
            
            if (!customerData.packageEntity) {
                console.warn('main.js: No package entity found via API. Will rely on stored login data.');
                return { 
                    success: false, 
                    message: 'No package entity in API response - will use stored data',
                    data: null,
                    sdate: customerData.sDate || customerData.sdate || customerData.getsDate
                };
            }
            
            return { 
                success: true, 
                data: customerData.packageEntity, 
                sdate: customerData.sDate || customerData.sdate || customerData.getsDate 
            };
        } else {
            const errorText = await response.text();
            console.error('main.js: Failed to fetch user package data:', errorText);
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Error fetching user package data:', error);
        return { success: false, message: 'Connection error fetching package data.' };
    }
});

ipcMain.handle('getRemainingBalance', async (event, msisdn) => {
    console.log(`main.js: Fetching balance for MSISDN: ${msisdn}`);
    try {
        const response = await fetch(`${BASE_URL}/balances/${msisdn}`, { 
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const balanceData = await response.json(); 
            console.log('main.js: Balance data fetched:', balanceData);
            return { success: true, data: balanceData };
        } else {
            const errorText = await response.text();
            console.error('main.js: Failed to fetch remaining balance:', errorText);
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Error fetching remaining balance:', error);
        return { success: false, message: 'Connection error fetching balance data.' };
    }
});

ipcMain.handle('register', async (event, userData) => {
    console.log('main.js: Registration attempt:', userData);
    try {
        const registerUrl = new URL(`${BASE_URL}/customer/register`);
        registerUrl.searchParams.append('msisdn', userData.phoneNumber);
        registerUrl.searchParams.append('name', userData.firstName);
        registerUrl.searchParams.append('surname', userData.lastName);
        registerUrl.searchParams.append('email', userData.email);
        registerUrl.searchParams.append('password', userData.password);
        registerUrl.searchParams.append('nationalId', userData.nationalId);
        
        console.log('Final URL:', registerUrl.toString());
        
        const response = await fetch(registerUrl.toString(), {
            method: 'POST',
            headers: {
                'Accept': '*/*',
                'Accept-Charset': 'UTF-8',
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            }
        });
        
        console.log('Response status:', response.status);
        console.log('Response headers:', Object.fromEntries(response.headers));
        
        const responseText = await response.text();
        console.log('Response body:', responseText);
        
        if (response.ok) {
            console.log('main.js: Registration successful:', responseText);
            return { success: true, message: responseText };
        } else {
            console.error('main.js: Registration unsuccessful:', response.status, responseText); 
            return { success: false, message: responseText };
        }
    } catch (error) {
        console.error('main.js: Network error during registration:', error);
        return { success: false, message: 'Unable to connect to server. Please try again later.' };
    }
});


ipcMain.handle('check-customer-exists', async (event, { email, nationalId }) => {
    console.log(`main.js: Checking if customer exists: Email: ${email}, National ID: ${nationalId}`);
    try {
        const response = await fetch(`${BASE_URL}/customer/exists?email=${encodeURIComponent(email)}&nationalId=${encodeURIComponent(nationalId)}`);

        if (response.ok) {
            const exists = await response.json();
            console.log('main.js: Customer exists check result:', exists);
            return { success: true, exists: exists };
        } else {
            const errorText = await response.text();
            console.error('main.js: Error checking customer existence:', errorText);
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Network error while checking customer existence:', error);
        return { success: false, message: 'Unable to connect to server. Please try again later.' };
    }
});

ipcMain.handle('get-customer-by-msisdn', async (event, phoneNumber) => {
    console.log(`main.js: Getting customer by phone: ${phoneNumber}`);
    try {
        const response = await fetch(`${BASE_URL}/customer/${phoneNumber}`);

        if (response.ok) {
            const customer = await response.json();
            console.log('main.js: Customer retrieved:', customer);
            return { success: true, data: customer };
        } else {
            const errorText = await response.text();
            console.error('main.js: Error getting customer:', errorText);
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Network error while getting customer:', error);
        return { success: false, message: 'Unable to connect to server. Please try again later.' };
    }
});

ipcMain.handle('get-packages', async (event) => {
    console.log('main.js: Getting packages');
    try {
        const response = await fetch(`${BASE_URL}/packages/all`);

        if (response.ok) {
            const packages = await response.json();
            console.log('main.js: Packages retrieved:', packages);
            return { success: true, data: packages };
        } else {
            const errorText = await response.text();
            console.error('main.js: Error getting packages:', errorText);
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Network error while getting packages:', error);
        return { success: false, message: 'Unable to connect to server. Please try again later.' };
    }
});

ipcMain.handle('get-user-package-by-msisdn', async (event, msisdn) => {
    console.log(`main.js: Getting user package by msisdn: ${msisdn}`);
    try {
        const response = await fetch(`${BASE_URL}/user-package/${msisdn}`);

        if (response.ok) {
            const userPackage = await response.json();
            console.log('main.js: User package retrieved:', userPackage);
            return { success: true, data: userPackage };
        } else {
            const errorText = await response.text();
            console.error('main.js: Error getting user package:', errorText);
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Network error while getting user package:', error);
        return { success: false, message: 'Unable to connect to server. Please try again later.' };
    }
});

ipcMain.handle('get-remaining-balance', async (event, msisdn) => {
    console.log(`main.js: Getting remaining balance for: ${msisdn}`);
    try {
        const response = await fetch(`${BASE_URL}/balance/${msisdn}`);

        if (response.ok) {
            const balance = await response.json();
            console.log('main.js: Balance retrieved:', balance);
            return { success: true, data: balance };
        } else {
            const errorText = await response.text();
            console.error('main.js: Error getting balance:', errorText);
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Network error while getting balance:', error);
        return { success: false, message: 'Unable to connect to server. Please try again later.' };
    }
});

ipcMain.handle('update-customer-password', async (event, data) => {
    console.log('main.js: Updating customer password');
    try {
        const response = await fetch(`${BASE_URL}/customer/reset-password`, { 
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const message = await response.text();
            return { success: true, message: message };
        } else {
            const errorText = await response.text();
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Network error while updating password:', error);
        return { success: false, message: 'Unable to connect to server. Please try again later.' };
    }
});

ipcMain.handle('select-package', async (event, { msisdn, packageId }) => {
    console.log(`main.js: Selecting package: MSISDN: ${msisdn}, Package ID: ${packageId}`);
    try {
        const response = await fetch(`${BASE_URL}/balances/balances`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                MSISDN: msisdn,
                PACKAGE_ID: packageId
            })
        });

        if (response.ok) {
            const message = await response.text();
            console.log('main.js: Package selection successful:', message);
            return { success: true, message: message };
        } else {
            const errorText = await response.text();
            console.error('main.js: Package selection unsuccessful:', errorText);
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Network error during package selection:', error);
        return { success: false, message: 'Unable to connect to server. Please try again later.' };
    }
});

ipcMain.handle('get-package-id-by-name', async (event, packageName) => {
    console.log(`main.js: Getting package ID by name: ${packageName}`);
    try {
        const response = await fetch(`${BASE_URL}/packages/id-by-name/${encodeURIComponent(packageName)}`);

        if (response.ok) {
            const packageId = await response.json();
            console.log('main.js: Package ID retrieved:', packageId);
            return { success: true, data: packageId };
        } else {
            const errorText = await response.text();
            console.error('main.js: Error getting package ID:', errorText);
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Network error while getting package ID:', error);
        return { success: false, message: 'Unable to connect to server. Please try again later.' };
    }
});

ipcMain.handle('search-package-details-by-name', async (event, packageName) => {
    console.log(`main.js: Searching package details by name: ${packageName}`);
    try {
        const response = await fetch(`${BASE_URL}/packages/name/${encodeURIComponent(packageName)}`);

        if (response.ok) {
            const packageDetails = await response.json();
            console.log('main.js: Package details retrieved:', packageDetails);
            return { success: true, data: packageDetails };
        } else {
            const errorText = await response.text();
            console.error('main.js: Error searching package details:', errorText);
            return { success: false, message: errorText };
        }
    } catch (error) {
        console.error('main.js: Network error while searching package details:', error);
        return { success: false, message: 'Unable to connect to server. Please try again later.' };
    }
});

ipcMain.handle('update-balance-minutes', async (event, data) => {
    console.log('main.js: Updating balance minutes:', data);
    return { success: true, message: 'Minutes updated successfully' };
});

ipcMain.handle('update-balance-sms', async (event, data) => {
    console.log('main.js: Updating balance SMS:', data);
    return { success: true, message: 'SMS balance updated successfully' };
});

ipcMain.handle('update-balance-data', async (event, data) => {
    console.log('main.js: Updating balance data:', data);
    return { success: true, message: 'Data balance updated successfully' };
});

ipcMain.handle('update-all-balance', async (event, data) => {
    console.log('main.js: Updating all balance:', data);
    return { success: true, message: 'All balances updated successfully' };
});

ipcMain.handle('insert-balance', async (event, data) => {
    console.log('main.js: Inserting balance:', data);
    return { success: true, message: 'Balance inserted successfully' };
});

ipcMain.on('navigate-to', (event, page) => {
    if (mainWindow) {
        mainWindow.loadFile(`${page}`);
    }
});
