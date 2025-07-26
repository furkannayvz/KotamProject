document.addEventListener('DOMContentLoaded', () => {
    const internetChartElement = document.getElementById('internet-chart');
    const minutesChartElement = document.getElementById('minutes-chart');
    const smsChartElement = document.getElementById('sms-chart');

    const internetPackageName = document.getElementById('internet-package-name');
    const internetDataText = document.getElementById('internet-data-text');
    const internetExpirationText = document.getElementById('internet-expiration-text');

    const minutesPackageName = document.getElementById('minutes-package-name');
    const minutesDataText = document.getElementById('minutes-data-text');
    const minutesExpirationText = document.getElementById('minutes-expiration-text');

    const smsPackageName = document.getElementById('sms-package-name');
    const smsDataText = document.getElementById('sms-data-text');
    const smsExpirationText = document.getElementById('sms-expiration-text');

    const planNameElement = document.getElementById('plan-name');
    const planExpiresElement = document.getElementById('plan-expires');
    const daysLeftTextElement = document.getElementById('days-left-text');
    const daysLeftProgressElement = document.getElementById('days-left-progress');
    const summarySmsValueElement = document.getElementById('summary-sms-value');
    const summaryGbValueElement = document.getElementById('summary-gb-value');
    const summaryMinuteValueElement = document.getElementById('summary-minute-value');

    const logoutButton = document.getElementById('logout-button');

    let currentUser = null;
    let userPackage = null;
    let userBalance = null;
    let isLoading = false;

    function createCircularChart(elementId, percentage) {
        const element = document.getElementById(elementId);
        if (!element) {
            console.error(`Element with id ${elementId} not found`);
            return;
        }

        element.innerHTML = '';

        const radius = 50;
        const thickness = 10;
        const width = 120;
        const height = 120;
        
        const arc = d3.arc()
            .innerRadius(radius - thickness)
            .outerRadius(radius)
            .startAngle(0);

        const svg = d3.select(`#${elementId}`)
            .append("svg")
            .attr("width", width)
            .attr("height", height)
            .attr("class", "chart-svg");

        const g = svg.append("g")
            .attr("transform", `translate(${width/2},${height/2})`);

        g.append("path")
            .datum({ endAngle: 2 * Math.PI })
            .style("fill", "#e2e8f0")
            .attr("d", arc);

        g.append("path")
            .datum({ endAngle: (percentage / 100) * 2 * Math.PI })
            .style("fill", "#3b4f7d")
            .attr("d", arc);

        g.append("text")
            .attr("text-anchor", "middle")
            .attr("dy", "0.35em")
            .style("font-size", "14px")
            .style("font-weight", "bold")
            .style("fill", "#192348")
            .text(`${Math.round(percentage)}%`);

        console.log(`Chart ${elementId} created with ${percentage}%`);
    }

    function showErrorState(message) {
        console.error(message);
        if (planNameElement) planNameElement.textContent = 'Error Loading Package';
        if (planExpiresElement) planExpiresElement.textContent = message;
    }

    function showNoPackageState() {
        console.log('User has no active package');
        
        if (planNameElement) planNameElement.textContent = 'No Active Package';
        if (planExpiresElement) planExpiresElement.textContent = 'Please contact support to activate a package';
        if (daysLeftTextElement) daysLeftTextElement.textContent = 'No package selected';
        
        if (internetPackageName) internetPackageName.textContent = 'No Package';
        if (internetDataText) internetDataText.textContent = '0 GB / 0 GB';
        if (internetExpirationText) internetExpirationText.textContent = 'No active package';
        
        if (minutesPackageName) minutesPackageName.textContent = 'No Package';
        if (minutesDataText) minutesDataText.textContent = '0 / 0 Minutes';
        if (minutesExpirationText) minutesExpirationText.textContent = 'No active package';
        
        if (smsPackageName) smsPackageName.textContent = 'No Package';
        if (smsDataText) smsDataText.textContent = '0 / 0 SMS';
        if (smsExpirationText) smsExpirationText.textContent = 'No active package';
        
        if (summarySmsValueElement) summarySmsValueElement.textContent = '0';
        if (summaryGbValueElement) summaryGbValueElement.textContent = '0';
        if (summaryMinuteValueElement) summaryMinuteValueElement.textContent = '0';
        
        if (daysLeftProgressElement) daysLeftProgressElement.style.width = '0%';
        
        createCircularChart("internet-chart", 0);
        createCircularChart("minutes-chart", 0);
        createCircularChart("sms-chart", 0);
    }

    async function loadUserData() {
        isLoading = true;
        try {
            currentUser = JSON.parse(sessionStorage.getItem('currentUser')) || JSON.parse(localStorage.getItem('currentUser'));

            if (!currentUser || !currentUser.phone) {
                console.error('No current user found or phone number is missing. Redirecting to login.');
                window.location.href = 'login.html';
                return;
            }

            console.log('Loading data for user:', currentUser.phone);
            console.log('Current user data:', currentUser);

            if (currentUser.packageEntity) {
                console.log('Using package data from login:', currentUser.packageEntity);
                userPackage = currentUser.packageEntity;
                userPackage.sdate = currentUser.sdate;
            } else {
                console.log('No package in stored user data, trying API...');
                
                const packageResponse = await window.electronAPI.getUserPackageByMsisdn(currentUser.phone);
                console.log('Package API response:', packageResponse);
                
                if (packageResponse.success && packageResponse.data) {
                    userPackage = packageResponse.data;
                    userPackage.sdate = packageResponse.sdate;
                } else {
                    console.log('Main API failed, trying alternative...');
                    
                    try {
                        const altResponse = await window.electronAPI.getUserPackageAlternative(currentUser.phone);
                        if (altResponse && altResponse.success && altResponse.data) {
                            userPackage = altResponse.data;
                            console.log('Alternative API succeeded:', userPackage);
                        } else {
                            console.log('All API attempts failed, showing no package state');
                            showNoPackageState();
                            isLoading = false;
                            return;
                        }
                    } catch (altError) {
                        console.error('Alternative API also failed:', altError);
                        showNoPackageState();
                        isLoading = false;
                        return;
                    }
                }
            }

            const balanceResponse = await window.electronAPI.getRemainingBalance(currentUser.phone);
            console.log('Balance response:', balanceResponse);
            
            if (balanceResponse.success) {
                userBalance = balanceResponse.data;
                console.log('Balance loaded successfully:', userBalance);
            } else {
                console.error('Failed to fetch remaining balance:', balanceResponse.message);
                userBalance = {
                    leftData: userPackage.dataQuota || 0,
                    leftSms: userPackage.smsQuota || 0,
                    leftMinutes: userPackage.minutesQuota || 0
                };
                console.log('Using package quotas as fallback balance:', userBalance);
            }

            updateUsageDisplay();

        } catch (error) {
            console.error('Error loading user data:', error);
            showErrorState('Error loading data: ' + error.message);
        } finally {
            isLoading = false;
        }
    }

    function updateUsageDisplay() {
        try {
            if (!currentUser || !userPackage) {
                console.warn('Cannot update display: Missing user or package data');
                return;
            }

            console.log('Updating UI with:', { currentUser, userPackage, userBalance });

            const packageName = userPackage.packageName || userPackage.name || 'Unknown Package';
            if (planNameElement) planNameElement.textContent = packageName;

            let expirationDate = new Date();
            const packageStartDate = userPackage.sdate ? new Date(userPackage.sdate) : 
                                   (currentUser.sdate ? new Date(currentUser.sdate) : new Date());
            const periodDays = userPackage.period || 30;

            if (packageStartDate && periodDays) {
                expirationDate = new Date(packageStartDate);
                expirationDate.setDate(packageStartDate.getDate() + periodDays);

                const today = new Date();
                today.setHours(0, 0, 0, 0);
                expirationDate.setHours(0, 0, 0, 0);

                const diffTime = expirationDate.getTime() - today.getTime();
                const daysLeft = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

                if (planExpiresElement) planExpiresElement.textContent = `Expires on ${expirationDate.toLocaleString('en-US', { month: 'long', day: 'numeric' })} (${daysLeft} days left)`;
                if (daysLeftTextElement) daysLeftTextElement.textContent = `${daysLeft} days left`;

                const progressPercentage = Math.max(0, Math.min(100, (daysLeft / periodDays) * 100));
                if (daysLeftProgressElement) daysLeftProgressElement.style.width = `${progressPercentage}%`;
            }

            const displayBalance = userBalance || {
                leftData: userPackage.dataQuota || 0,
                leftSms: userPackage.smsQuota || 0,
                leftMinutes: userPackage.minutesQuota || 0
            };

            if (summarySmsValueElement) summarySmsValueElement.textContent = displayBalance.leftSms || '0';
            if (summaryGbValueElement) summaryGbValueElement.textContent = ((displayBalance.leftData || 0) / 1000).toFixed(1);
            if (summaryMinuteValueElement) summaryMinuteValueElement.textContent = displayBalance.leftMinutes || '0';

            if (internetPackageName) internetPackageName.textContent = packageName;
            if (internetDataText) internetDataText.textContent = `${((displayBalance.leftData || 0) / 1000).toFixed(1)} GB / ${((userPackage.dataQuota || 0) / 1000).toFixed(1)} GB`;
            if (internetExpirationText) internetExpirationText.textContent = `Expires on ${expirationDate.toLocaleDateString()}`;

            if (minutesPackageName) minutesPackageName.textContent = packageName;
            if (minutesDataText) minutesDataText.textContent = `${displayBalance.leftMinutes || 0} / ${userPackage.minutesQuota || 0} Minutes`;
            if (minutesExpirationText) minutesExpirationText.textContent = `Expires on ${expirationDate.toLocaleDateString()}`;

            if (smsPackageName) smsPackageName.textContent = packageName;
            if (smsDataText) smsDataText.textContent = `${displayBalance.leftSms || 0} / ${userPackage.smsQuota || 0} SMS`;
            if (smsExpirationText) smsExpirationText.textContent = `Expires on ${expirationDate.toLocaleDateString()}`;

            const internetRemainingPercentage = userPackage.dataQuota > 0 ? 
                ((displayBalance.leftData || 0) / userPackage.dataQuota) * 100 : 0;
            const minutesRemainingPercentage = userPackage.minutesQuota > 0 ? 
                ((displayBalance.leftMinutes || 0) / userPackage.minutesQuota) * 100 : 0;
            const smsRemainingPercentage = userPackage.smsQuota > 0 ? 
                ((displayBalance.leftSms || 0) / userPackage.smsQuota) * 100 : 0;

            createCircularChart("internet-chart", Math.max(0, Math.min(100, internetRemainingPercentage)));
            createCircularChart("minutes-chart", Math.max(0, Math.min(100, minutesRemainingPercentage)));
            createCircularChart("sms-chart", Math.max(0, Math.min(100, smsRemainingPercentage)));

            console.log('UI updated successfully');

        } catch (error) {
            console.error('Error updating display:', error);
            showErrorState('Error displaying usage data: ' + error.message);
        }
    }

    if (logoutButton) {
        logoutButton.addEventListener('click', () => {
            sessionStorage.removeItem('currentUser');
            localStorage.removeItem('currentUser');
            window.location.href = 'login.html';
        });
    }

    function startAutoRefresh() {
        setInterval(() => {
            if (currentUser && !isLoading) {
                console.log('Auto-refreshing usage data...');
                loadUserData();
            }
        }, 60000); 
    }

    function initializeCharts() {
        createCircularChart("internet-chart", 0);
        createCircularChart("minutes-chart", 0);
        createCircularChart("sms-chart", 0);
    }

    console.log('Usage renderer initialized');
    initializeCharts();
    loadUserData();
    startAutoRefresh();
});