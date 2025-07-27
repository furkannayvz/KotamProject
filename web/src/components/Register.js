import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Register.css";
import LoginRight from "./LoginRight";
// PasswordStrengthIndicator'ı yeni, ayrı dosyasından import ediyoruz.
import PasswordStrengthIndicator from "./PasswordStrengthIndicator"; 

function Register() {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    nationalId: "", // TC kimlik no için
    email: "",
    password: "",
    confirmPassword: "",
    phone: "",
  });
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError(""); // Herhangi bir değişiklikte hatayı temizle
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");

    if (formData.password !== formData.confirmPassword) {
      setError("Passwords do not match.");
      return;
    }
    

    const { confirmPassword, ...userDataToStore } = formData;
    sessionStorage.setItem('registrationData', JSON.stringify(userDataToStore));


    navigate("/select-tariff");
  };

  return (
    <div className="register-root">
      <div className="register-left">
        <div className="register-box">
          <img src="/kotam-logo.png" alt="Kotam Logo" className="kotam-logo" />
          <h1>Sign up</h1>
          <form onSubmit={handleSubmit}>
            
            {/* --- TÜM FORM ALANLARI  --- */}
            
            <label>Name</label>
            <input name="firstName" value={formData.firstName} onChange={handleChange} placeholder="Your Name" required />

            <label>Surname</label>
            <input name="lastName" value={formData.lastName} onChange={handleChange} placeholder="Your Surname" required />

            <label>National ID</label>
            <input name="nationalId" value={formData.nationalId} onChange={handleChange} placeholder="Your National ID" required />

            <label>Mail</label>
            <input name="email" value={formData.email} onChange={handleChange} placeholder="Your Mail" required type="email" />

            <label>Phone Number</label>
            <input name="phone" value={formData.phone} onChange={handleChange} placeholder="Your Phone Number" required />

            <label>Password</label>
            <input name="password" value={formData.password} onChange={handleChange} placeholder="Your Password" type="password" required />

            {/* Şifre Güç Göstergesi */}
            <PasswordStrengthIndicator password={formData.password} />

            <label>Confirm Password</label>
            <input name="confirmPassword" value={formData.confirmPassword} onChange={handleChange} placeholder="Confirm Your Password" type="password" required />
            
            {error && <div className="error">{error}</div>}
            <button type="submit" className="register-btn">Continue</button>
          </form>

          <div className="login-area">
            Already have an account?
            <button
              className="login-link"
              type="button"
              onClick={() => navigate("/login")}
            >
              Log in
            </button>
          </div>
        </div>
      </div>
      <LoginRight />
    </div>
  );
}

export default Register;
