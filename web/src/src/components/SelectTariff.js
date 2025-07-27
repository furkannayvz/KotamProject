import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './SelectTariff.css';

function SelectTariff() {
    const [packages, setPackages] = useState([]);
    const [selectedPackage, setSelectedPackage] = useState(null);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        fetch('http://localhost:8080/api/packages/all')
            .then(res => {
                if (!res.ok) {
                    throw new Error('Failed to fetch packages');
                }
                return res.json();
            })
            .then(data => setPackages(data))
            .catch(() => setError('Could not load tariffs. Please try again later.'));
    }, []);

    const handleSelectPackage = (pkg) => {
        setSelectedPackage(pkg);
        setError('');
    };

    const handleFinalSubmit = async () => {
        if (!selectedPackage) {
            setError('Please select at least one package.');
            return;
        }

        setIsLoading(true);
        setError('');
        setSuccess('');

        const storedData = sessionStorage.getItem('registrationData');
        if (!storedData) {
            setError('User data not found. Please go back to the registration page.');
            setIsLoading(false);
            return;
        }

        const userData = JSON.parse(storedData);

        try {
            const params = new URLSearchParams({
                msisdn: userData.phone,
                name: userData.firstName,
                surname: userData.lastName,
                email: userData.email,
                password: userData.password,
                nationalId: userData.nationalId,
            }).toString();

            const registerResponse = await fetch(`http://localhost:8080/api/customer/register?${params}`, {
                method: "POST",
            });

            if (!registerResponse.ok) {
                const errorData = await registerResponse.text();
                throw new Error(errorData || 'User registration failed.');
            }

            const balanceResponse = await fetch('http://localhost:8080/api/balances/balances', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    MSISDN: userData.phone,
                    PACKAGE_ID: selectedPackage.id
                })
            });

            if (!balanceResponse.ok) {
                const errorData = await balanceResponse.text();
                throw new Error(errorData || 'User was registered, but balance creation failed.');
            }

            setSuccess("Registration successful! Redirecting to login...");
            sessionStorage.removeItem('registrationData');
            setTimeout(() => navigate("/login"), 3000);

        } catch (err) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="tariff-container">
            <div className="tariff-content">
                <img src="/kotam-logo.png" alt="Kotam Logo" className="tariff-logo" />
                <h1>My Tariff</h1>
                <p>Choose the perfect plan for your needs</p>

                {error && <div className="tariff-message tariff-error">{error}</div>}
                {success && <div className="tariff-message tariff-success">{success}</div>}

                <div className="tariff-grid">
                    {packages.map(pkg => (
                        <div
                            key={pkg.id}
                            className={`tariff-card ${selectedPackage?.id === pkg.id ? 'selected' : ''}`}
                            onClick={() => handleSelectPackage(pkg)}
                        >
                            <div className="tariff-header">
                                <h3>{pkg.name}</h3>
                                <span><b>{pkg.price.toFixed(2)}TL</b>/month</span>
                            </div>
                            <ul className="tariff-details">
                                {/* DÜZELTME: Küsuratlı sayıyı yuvarlamak için Math.round() eklendi. */}
                                <li>✔️ {Math.round(pkg.dataQuota / 1024)} GB data</li>
                                <li>✔️ {pkg.minutesQuota} minutes</li>
                                <li>✔️ {pkg.smsQuota} sms</li>
                            </ul>
                        </div>
                    ))}
                </div>

                <div className="tariff-actions">
                    <button className="back-btn" onClick={() => navigate('/register')} disabled={isLoading}>Back</button>
                    <button className="signup-btn" onClick={handleFinalSubmit} disabled={isLoading || !selectedPackage}>
                        {isLoading ? 'Registering...' : 'Sign Up'}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default SelectTariff;
