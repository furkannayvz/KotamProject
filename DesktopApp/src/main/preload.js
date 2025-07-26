// preload.js
const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('electronAPI', {
  login: (credentials) => ipcRenderer.invoke('login', credentials),
  register: (userData) => ipcRenderer.invoke('register', userData),
  forgotPassword: (emailData) => ipcRenderer.invoke('forgot-password', emailData),
  verifyCode: (data) => ipcRenderer.invoke('verify-code', data),
  resetPassword: (data) => ipcRenderer.invoke('reset-password', data),
  checkCustomerExists: (data) => ipcRenderer.invoke('check-customer-exists', data),
  getUserPackageByMsisdn: (msisdn) => ipcRenderer.invoke('getUserPackageByMsisdn', msisdn),
  getRemainingBalance: (msisdn) => ipcRenderer.invoke('getRemainingBalance', msisdn),
  getCustomerByMsisdn: (msisdn) => ipcRenderer.invoke('get-customer-by-msisdn', msisdn),
  getPackages: () => ipcRenderer.invoke('get-packages'),
  selectPackage: (data) => ipcRenderer.invoke('select-package', data),
  getPackageIdByName: (packageName) => ipcRenderer.invoke('get-package-id-by-name', packageName),
  updateCustomerPassword: (data) => ipcRenderer.invoke('update-customer-password', data),
  updateBalanceMinutes: (data) => ipcRenderer.invoke('update-balance-minutes', data),
  updateBalanceSms: (data) => ipcRenderer.invoke('update-balance-sms', data),
  updateBalanceData: (data) => ipcRenderer.invoke('update-balance-data', data),
  updateAllBalance: (data) => ipcRenderer.invoke('update-all-balance', data),
  insertBalance: (data) => ipcRenderer.invoke('insert-balance', data),
  searchPackageDetailsByName: (packageName) => ipcRenderer.invoke('search-package-details-by-name', packageName),
  navigateTo: (page) => {
    console.log(`preload.js: navigateTo called, page: ${page}`);
    ipcRenderer.send('navigate-to', page);
  }
});
