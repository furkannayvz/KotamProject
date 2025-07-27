import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './SmsSimulator.css'; 

function SmsSimulator() {
    const [from, setFrom] = useState('');
    const [text, setText] = useState('');
    const [responseMessage, setResponseMessage] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setResponseMessage('');
        setError('');
        setIsLoading(true);

        try {
            
            const response = await fetch('http://34.52.140.50:8080/api/sms/incoming', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ from, text }),
            });

            const data = await response.text(); 

            if (response.ok) {
                setResponseMessage(data);
            } else {
                setError(data || "An error occurred.");
            }
        } catch (err) {
            setError("Unable to connect to the server.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="sms-sim-container">
            <div className="sms-sim-box">
                <header className="sms-sim-header">
                    <h1>SMS Simulator</h1>
                    <button onClick={() => navigate('/home')} className="back-button">
                        Back to Dashboard
                    </button>
                </header>

                <form onSubmit={handleSubmit} className="sms-sim-form">
                    <div className="form-group">
                        <label>From (Phone Number / MSISDN)</label>
                        <input
                            type="text"
                            value={from}
                            onChange={e => setFrom(e.target.value)}
                            placeholder="e.g., 5347101010"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Message Text</label>
                        <input
                            type="text"
                            value={text}
                            onChange={e => setText(e.target.value)}
                            placeholder="e.g., KALAN"
                            required
                        />
                    </div>
                    <button type="submit" className="send-button" disabled={isLoading}>
                        {isLoading ? 'Sending...' : 'Send SMS'}
                    </button>
                </form>

                <div className="sms-sim-response">
                    <h3>Response:</h3>
                    {responseMessage && <div className="response-success">{responseMessage}</div>}
                    {error && <div className="response-error">{error}</div>}
                    {!responseMessage && !error && <div className="response-placeholder">Response from the server will appear here...</div>}
                </div>
            </div>
        </div>
    );
}

export default SmsSimulator;