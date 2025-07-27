import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";
import LoginRight from "./LoginRight";

function Login() {
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const response = await fetch('http://localhost:8080/api/customer/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ msisdn: phone, password }),
      });

      if (response.ok) {
        const user = await response.json();
        
        localStorage.setItem("msisdn", user.msisdn); 
        
 
        localStorage.setItem("userId", user.msisdn); 
        
        navigate("/home");
      } else if (response.status === 401 || response.status === 404) {
        setError('Phone number or password is incorrect.');
      } else {
        setError('An unknown error occurred.');
      }
    } catch (err) {
      setError('Cannot connect to the server.');
    }
  };

  return (
    <div className="login-root">
      {/* SOL TARAF */}
      <div className="login-left">
        <div className="login-box">
          <img src="/kotam-logo.png" alt="Kotam Logo" className="kotam-logo" />
          <h1>WELCOME TO<br />KOTAM</h1>
          <p>
            Track every GB you use, every minute you spend. <br />
            With Kotam, monitor your remaining usage in real time.
          </p>
          <form onSubmit={handleLogin}>
            <input
              type="text"
              placeholder="Your Phone Number"
              value={phone}
              onChange={e => setPhone(e.target.value)}
              required
            />
            <div className="password-area">
              <input
                type="password"
                placeholder="Your Password"
                value={password}
                onChange={e => setPassword(e.target.value)}
                required
              />
            </div>
            <div className="forgot-link">
              <button
                type="button"
                className="forgot-btn"
                onClick={() => navigate("/forgot-password")}
              >
                Forgot password?
              </button>
            </div>
            {error && <div className="error">{error}</div>}
            <button type="submit" className="login-btn">Login</button>
          </form>
          <div className="register-area">
            Don’t have an account?{" "}
            <button
              className="signup-btn"
              type="button"
              onClick={() => navigate("/register")}
            >
              Sign up
            </button>
          </div>
        </div>
      </div>

      {/* SAĞ TARAF */}
      <LoginRight />

    </div>
  );
}

export default Login;
