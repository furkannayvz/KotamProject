import React, { useState, useRef, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './VerifyOtp.css';
import LoginRight from './LoginRight';

function VerifyOtp() {
    const [otp, setOtp] = useState(new Array(5).fill(""));
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const location = useLocation();
    
   
    const email = location.state?.email;
    const nationalId = location.state?.nationalId; 

    const inputsRef = useRef([]);

    useEffect(() => {
        // Eğer email veya nationalId bilgisi yoksa, kullanıcıyı başa yönlendir
        if (!email || !nationalId) {
            navigate('/forgot-password');
        }
    }, [email, nationalId, navigate]);

    const handleChange = (element, index) => {
        if (isNaN(element.value)) return false;

        setOtp([...otp.map((d, idx) => (idx === index ? element.value : d))]);

        if (element.nextSibling) {
            element.nextSibling.focus();
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        const enteredOtp = otp.join("");

        if (enteredOtp.length < 5) {
            setError("Please enter the 5-digit code.");
            return;
        }

        try {
        
            const response = await fetch('http://localhost:8080/api/auth/verify-code', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, code: enteredOtp }), // `otp` alanı `code` olarak gönderiliyor
            });

            if (response.ok) {
               
                navigate('/new-password', { state: { email, nationalId, code: enteredOtp } });
            } else {
                const data = await response.json();
                setError(data.message || "Invalid or expired code.");
            }
        } catch (err) {
            setError("Unable to connect to the server.");
        }
    };

    return (
        <div className="otp-root">
            <div className="otp-left">
                <div className="otp-box">
                    <img src="/kotam-logo.png" alt="Kotam Logo" className="kotam-logo" />
                    <p className="otp-instruction">
                        Please enter the 5-digit password we sent to your e-mail account.
                    </p>
                    <form onSubmit={handleSubmit}>
                        <div className="otp-input-container">
                            {otp.map((data, index) => (
                                <input
                                    key={index}
                                    type="text"
                                    className="otp-input"
                                    maxLength="1"
                                    value={data}
                                    onChange={e => handleChange(e.target, index)}
                                    onFocus={e => e.target.select()}
                                    ref={el => inputsRef.current[index] = el}
                                />
                            ))}
                        </div>
                        <div className="resend-code">
                            <button type="button">Resend code</button>
                        </div>
                        {error && <div className="otp-error">{error}</div>}
                        <button type="submit" className="otp-btn-main">Confirm</button>
                    </form>
                </div>
            </div>
            <LoginRight />
        </div>
    );
}

export default VerifyOtp;
