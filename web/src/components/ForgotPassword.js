import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./ForgotPassword.css";
import LoginRight from "./LoginRight";

function ForgotPassword() {
  const [tc, setTc] = useState('');
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      const response = await fetch('http://localhost:8080/api/auth/forgot-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, nationalId: tc }), // `tc` alanı `nationalId` olarak gönderiliyor
      });

      if (response.ok) {
        navigate('/verify-otp', { state: { email: email, nationalId: tc } });
      } else {
        const data = await response.json();
        setError(data.message || "User with provided details not found.");
      }
    } catch (err) {
      setError("Unable to connect to the server.");
    } finally {
        setIsLoading(false);
    }
  };

  return (
    <div className="forgot-root">
      <div className="forgot-left">
        <div className="forgot-box">
          <img src="/kotam-logo.png" alt="Kotam Logo" className="kotam-logo" />
          <h1>Forgot Password</h1>
          <form onSubmit={handleSubmit}>
            <label>National ID</label>
            <input
              type="text"
              placeholder="Your National ID"
              value={tc}
              onChange={e => setTc(e.target.value)}
              required
            />
            <label>Mail</label>
            <input
              type="email"
              placeholder="Your Mail"
              value={email}
              onChange={e => setEmail(e.target.value)}
              required
            />
            <button type="submit" className="forgot-btn-main" disabled={isLoading}>
              {isLoading ? 'Sending...' : 'Send Recovery Email'}
            </button>
          </form>
          {error && <div className="error">{error}</div>}
          <div className="forgot-register-area">
            Go back to{" "}
            <button
              className="forgot-login-link"
              type="button"
              onClick={() => navigate("/login")}
            >
              Login
            </button>
          </div>
        </div>
      </div>
      <LoginRight />
    </div>
  );
}

export default ForgotPassword;
