import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { CircularProgressbar, buildStyles } from "react-circular-progressbar";
import "react-circular-progressbar/dist/styles.css";
import "./Dashboard.css";

function UsageCard({ title, type, percentage, valueText, expireText }) {
  return (
    <div className="usage-card">
      <div className="usage-card-progress">
        <CircularProgressbar
          value={percentage}
          text={`${percentage.toFixed(1)}%`}
          styles={buildStyles({
            textColor: "#0e3970",
            pathColor: "#0e3970",
            trailColor: "#e5eaf4",
            textSize: "22px",
            strokeLinecap: "round",
          })}
        />
      </div>
      <div className="usage-card-info">
        <div className="usage-card-header">
          <span className="usage-card-title">{title}</span>
          <span className="usage-card-type">{type}</span>
        </div>
        <div className="usage-card-value">{valueText}</div>
        <div className="usage-card-expire">{expireText}</div>
      </div>
    </div>
  );
}

function Dashboard() {
    const [user, setUser] = useState(null);
    const [balance, setBalance] = useState(null);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const msisdn = localStorage.getItem("msisdn");

    useEffect(() => {
        if (!msisdn) {
            navigate("/login");
            return;
        }

        fetch(`http://localhost:8080/api/customer/${msisdn}`)
            .then(res => {
                if (!res.ok) throw new Error('Kullanıcı bilgisi alınamadı.');
                return res.json();
            })
            .then(data => setUser(data))
            .catch(err => {
                console.error("Kullanıcı API hatası:", err);
                setError("Kullanıcı bilgileri yüklenirken bir sorun oluştu.");
            });

        
        fetch(`http://localhost:8080/api/balances/${msisdn}`)
            .then(res => {
                if (res.status === 404) {
                    setError('Bu kullanıcıya ait bakiye bilgisi bulunamadı.');
                    return Promise.reject('balance_not_found');
                }
                if (!res.ok) {
                    throw new Error(`Bakiye API hatası! Status: ${res.status}`);
                }
                return res.json();
            })
            .then(data => setBalance(data))
            .catch(err => {
                if (err.name !== 'balance_not_found') {
                    console.error("Bakiye API hatası:", err);
                    setError("Bakiye bilgileri alınırken bir sorun oluştu.");
                }
            });
    }, [msisdn, navigate]);
    
  
    if (error) return <div className="dashboard-error">Hata: {error}</div>;
   
    if (!user || !balance || !balance.packageEntity) {
        return <div className="dashboard-loading">Loading...</div>;
    }

  
    const startDate = new Date(balance.getsDate);
    const expireDate = new Date(startDate.setDate(startDate.getDate() + 30)); // Paketin 30 gün geçerli olduğunu varsayıyoruz
    const daysLeft = Math.max(0, Math.floor((expireDate - new Date()) / (1000 * 60 * 60 * 24)));
    const totalDaysInPeriod = 30;

  
    const percentData = balance.packageEntity.dataQuota ? (balance.leftData / balance.packageEntity.dataQuota) * 100 : 0;
    const percentMinutes = balance.packageEntity.minutesQuota ? (balance.leftMinutes / balance.packageEntity.minutesQuota) * 100 : 0;
    const percentSMS = balance.packageEntity.smsQuota ? (balance.leftSms / balance.packageEntity.smsQuota) * 100 : 0;
    
    return (
        <div className="dashboard-container">
          <header className="dashboard-header">
            <div className="header-logo-container">
              <img src="/kotambeyaz.png" alt="KOTAM Logo" className="header-logo" />
              <span className="header-brand">i2i Systems</span>
            </div>
            <div className="header-actions">
              <button className="profile-button" onClick={() => navigate("/profile")}>
                  Profil
              </button>
              <button className="logout-button" onClick={() => { localStorage.clear(); navigate("/login"); }}>
                  Logout ⇥
              </button>
            </div>
          </header>
          <main className="dashboard-content">
            <p className="dashboard-description">
              Track your remaining data and minutes in real-time.
            </p>
            <div className="dashboard-grid">
                {/* Sol Sütun */}
                <div className="left-column">
                    <h2 className="column-title">Remaining Usage</h2>
                    {/* KULLANIM KARTLARI  */}
                    <UsageCard
                        title={balance.packageEntity.packageName || "Default Package"}
                        type="Data"
                        percentage={percentData}
                        valueText={`${(balance.leftData / 1024).toFixed(2)} GB / ${(balance.packageEntity.dataQuota / 1024).toFixed(1)} GB`}
                        expireText={`Expires on ${expireDate.toLocaleDateString('en-US', { month: 'long', day: 'numeric' })} (${daysLeft} days left)`}
                    />
                    <UsageCard
                        title={balance.packageEntity.packageName || "Default Package"}
                        type="Minute"
                        percentage={percentMinutes}
                        valueText={`${balance.leftMinutes} / ${balance.packageEntity.minutesQuota}`}
                        expireText={`Expires on ${expireDate.toLocaleDateString('en-US', { month: 'long', day: 'numeric' })} (${daysLeft} days left)`}
                    />
                    <UsageCard
                        title={balance.packageEntity.packageName || "Default Package"}
                        type="SMS"
                        percentage={percentSMS}
                        valueText={`${balance.leftSms} / ${balance.packageEntity.smsQuota}`}
                        expireText={`Expires on ${expireDate.toLocaleDateString('en-US', { month: 'long', day: 'numeric' })} (${daysLeft} days left)`}
                    />
                </div>
                {/*  */}
                <div className="right-column">
                    <h2 className="column-title">My Plan</h2>
                    <div className="plan-card">
                        <div className="plan-card-header-box">
                            <h3>{balance.packageEntity.packageName}</h3>
                            <p>Expires on {expireDate.toLocaleDateString('en-US', { month: 'long', day: 'numeric' })} ({daysLeft} days left)</p>
                        </div>
                        <div className="plan-card-body">
                            <div className="plan-days-left">
                                <span><b>{daysLeft}</b> days left</span>
                                <span>{expireDate.toLocaleDateString('tr-TR')}</span>
                            </div>
                            <div className="plan-progress-bar-container">
                                <div className="plan-progress-bar" style={{ width: `${100 - (daysLeft / totalDaysInPeriod * 100)}%` }}></div>
                            </div>
                        </div>
                        {/*  */}
                        <div className="plan-card-footer">
                            <span><b>{balance.packageEntity.smsQuota}</b> SMS</span>
                            <span><b>{(balance.packageEntity.dataQuota / 1024).toFixed(1)}</b> GB</span>
                            <span><b>{balance.packageEntity.minutesQuota}</b> Minute</span>
                        </div>
                    </div>
                </div>
            </div>
          </main>
        </div>
    );
}

export default Dashboard;
