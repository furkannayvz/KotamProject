import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";


import Login from "./components/Login";
import Register from "./components/Register";
import SelectTariff from './components/SelectTariff';
import ForgotPassword from "./components/ForgotPassword";
import VerifyOtp from './components/VerifyOtp';
import NewPassword from './components/NewPassword'; 
import Dashboard from './components/Dashboard';
import Profile from './components/Profile';
import SmsSimulator from './components/SmsSimulator';

function App() {
  return (
    <Router>
      <Routes>
        {/* Ana ve Login Yolları */}
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />

        {/* Kayıt Olma Akışı Yolları */}
        <Route path="/register" element={<Register />} />
        <Route path="/select-tariff" element={<SelectTariff />} />

        {/* Şifre Sıfırlama Akışı Yolları */}
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/verify-otp" element={<VerifyOtp />} />
        <Route path="/new-password" element={<NewPassword />} />

        {/* Giriş Yaptıktan Sonraki Sayfalar */}
        <Route path="/home" element={<Dashboard />} />
        <Route path="/profile" element={<Profile />} />
        
        {/* Simülatör Sayfası */}
        <Route path="/sms-simulator" element={<SmsSimulator />} />
      </Routes>
    </Router>
  );
}

export default App;
