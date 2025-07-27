import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './NewPassword.css';
import LoginRight from './LoginRight';
import PasswordStrengthIndicator from './PasswordStrengthIndicator';

function NewPassword() {
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const location = useLocation();

    const email = location.state?.email;
    const nationalId = location.state?.nationalId;
    const code = location.state?.code;

    useEffect(() => {
        if (!email || !nationalId || !code) {
            navigate('/forgot-password');
        }
    }, [email, nationalId, code, navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        setError('');

        if (newPassword !== confirmPassword) {
            setError("Passwords do not match.");
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/auth/reset-password', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    email,
                    nationalId,
                    code,
                    newPassword,
                    confirmPassword
                }),
            });

            if (response.ok) {
                setMessage("Password updated successfully! Redirecting to login...");
                setTimeout(() => navigate('/login'), 3000);
            } else {
                const data = await response.json();
                setError(data.message || "An error occurred while updating password.");
            }
        } catch (err) {
            setError("Unable to connect to the server.");
        }
    };

    return (
        <div className="new-password-root">
            <div className="new-password-left">
                <div className="new-password-box">
                    <img src="/kotam-logo.png" alt="Kotam Logo" className="kotam-logo" />
                    <h1>Set New Password</h1>
                    <form onSubmit={handleSubmit}>
                        <label>New Password</label>
                        <input type="password" placeholder="Your New Password" value={newPassword} onChange={e => setNewPassword(e.target.value)} required />
                        
                        <PasswordStrengthIndicator password={newPassword} />

                        <label>Confirm New Password</label>
                        <input type="password" placeholder="Confirm Your New Password" value={confirmPassword} onChange={e => setConfirmPassword(e.target.value)} required />
                        
                        {error && <div className="new-password-error">{error}</div>}
                        {message && <div className="new-password-success">{message}</div>}
                        <button type="submit" className="new-password-btn-main">Save New Password</button>
                    </form>
                </div>
            </div>
            <LoginRight />
        </div>
    );
}

export default NewPassword;
