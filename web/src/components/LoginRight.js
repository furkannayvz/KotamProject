// src/components/LoginRight.js
import React from "react";
import "./LoginRight.css";

function LoginRight() {
  return (
    <div className="login-right-gradient">
      <div className="login-right-content">
        <h2>
          <span className="bold">Visualize Data<br />Follow Quota</span>
          <span className="light">Effortlessly</span>
        </h2>
      </div>
      <img
        src="/Icon-svg.png"
        alt="icon"
        className="login-right-icon"
        draggable={false}
      />
    </div>
  );
}

export default LoginRight;