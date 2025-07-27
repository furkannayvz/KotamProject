import React, { useState, useEffect } from 'react';
import './Register.css'; // Stillerin çalışması için Register.css'i import ediyoruz

function PasswordStrengthIndicator({ password }) {
    const [checks, setChecks] = useState({
        length: false,
        uppercase: false,
        number: false,
        symbol: false,
    });

    useEffect(() => {
        setChecks({
            length: password.length >= 8,
            uppercase: /[A-Z]/.test(password),
            number: /[0-9]/.test(password),
            symbol: /[^A-Za-z0-9]/.test(password), 
        });
    }, [password]);

    const Criterion = ({ text, checked }) => (
        <div className={`strength-criterion ${checked ? 'valid' : ''}`}>
            <span className="checkmark">{checked ? '✔' : '○'}</span>
            <span>{text}</span>
        </div>
    );

    return (
        <div className="password-strength-indicator">
            <Criterion text="At least 8 characters" checked={checks.length} />
            <Criterion text="At least one uppercase" checked={checks.uppercase} />
            <Criterion text="At least one number" checked={checks.number} />
            <Criterion text="At least one symbol" checked={checks.symbol} />
        </div>
    );
}

export default PasswordStrengthIndicator;
