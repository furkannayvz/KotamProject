// src/components/UsageList.js
import React, { useEffect, useState } from 'react';

function UsageList() {
  const [usages, setUsages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch('http://34.52.140.50:8080/api/usages')
      .then((response) => {
        if (!response.ok) {
          throw new Error('API isteği başarısız: ' + response.status);
        }
        return response.json();
      })
      .then((data) => {
        setUsages(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Loading usages...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div style={{ padding: 20, fontFamily: 'sans-serif', maxWidth: 600, margin: 'auto' }}>
      <h1>Usage List</h1>
      {usages.length === 0 ? (
        <p>No usage data found.</p>
      ) : (
        <ul>
          {usages.map((usage) => (
            <li key={usage.id}>
              <strong>{usage.username}</strong> – {usage.usageType} – {usage.amount}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default UsageList;