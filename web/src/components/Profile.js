import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Profile.css';

function Profile() {
    const [user, setUser] = useState(null);
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phone: ''
    });
    const [message, setMessage] = useState('');
    const navigate = useNavigate();
    
    const userId = localStorage.getItem("userId");

    useEffect(() => {
        if (!userId) {
            navigate("/login");
            return;
        }

       
        fetch(`http://localhost:8080/api/customer/${userId}`)
            .then(res => {
                if (!res.ok) {
                    throw new Error('Kullanıcı bilgileri çekilemedi');
                }
                return res.json();
            })
            .then(data => {
                setUser(data);
                setFormData({
                    firstName: data.name, // Backend'den 'name' olarak geliyor
                    lastName: data.surname, // Backend'den 'surname' olarak geliyor
                    email: data.email,
                    phone: data.msisdn // Backend'den 'msisdn' olarak geliyor
                });
            })
            .catch(err => {
                console.error("Kullanıcı bilgileri çekilemedi:", err);
                setMessage("Kullanıcı bilgileri çekilirken bir hata oluştu.");
            });
    }, [userId, navigate]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        setMessage('Profile update feature is not yet implemented in the backend.');
    };

    if (!user) {
        return <div className="profile-container">Loading profile...</div>;
    }

    return (
        <div className="profile-container">
            <header className="profile-header">
                <h1>My Profile</h1>
                <button onClick={() => navigate('/home')}>Back to Dashboard</button>
            </header>
            <form className="profile-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>First Name</label>
                    <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} />
                </div>
                <div className="form-group">
                    <label>Last Name</label>
                    <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} />
                </div>
                <div className="form-group">
                    <label>Email</label>
                    <input type="email" name="email" value={formData.email} onChange={handleChange} />
                </div>
                <div className="form-group">
                    <label>Phone Number</label>
                    <input type="text" name="phone" value={formData.phone} onChange={handleChange} />
                </div>
                <button type="submit" className="save-button">Save Changes</button>
                {message && <p className="profile-message">{message}</p>}
            </form>
        </div>
    );
}

export default Profile;
