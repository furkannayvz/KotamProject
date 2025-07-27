import React from "react";
import { useNavigate } from "react-router-dom";
import "./Header.css"; 

function Header({ user, onLogout }) {
  const navigate = useNavigate();

  return (
    <header className="main-header">
      <div className="header-left">
        <button
          className="header-logo-btn"
          onClick={() => navigate("/dashboard")}
        >
          <img
            src="/kotam-logo.png"
            alt="Kotam"
            className="header-logo"
          />
          <span className="header-title">KOTAM</span>
        </button>
      </div>
      <div className="header-center">
        {/*  */}
      </div>
      <div className="header-right">
        <div className="header-user">
          <span className="header-username">
            {user ? user.firstName : "User"}
          </span>
          <img
            src="/profile-avatar.png"
            alt="Profile"
            className="header-avatar"
            onClick={onLogout}
            title="Logout"
          />
        </div>
      </div>
    </header>
  );
}

export default Header;