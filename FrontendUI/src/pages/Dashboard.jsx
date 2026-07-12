import React from 'react';
import { useNavigate } from 'react-router-dom';
import { logout } from '../services/authService';
import { getAuthName, getAuthRole } from '../utils/token';

const Dashboard = () => {
  const navigate = useNavigate();
  const name = getAuthName();
  const role = getAuthRole();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="app-container">
      <nav className="navbar">
        <div className="nav-brand">CarInventory</div>
        <div className="nav-links">
          <span className="nav-item">Welcome, {name} ({role})</span>
          <button onClick={handleLogout} className="btn-outline">Logout</button>
        </div>
      </nav>
      
      <main className="main-content">
        <div style={{ padding: '2rem', border: '1px solid black' }}>
          <h2 style={{ fontSize: '1.5rem', marginBottom: '1rem' }}>Dashboard Overview</h2>
          <p style={{ color: 'var(--text-secondary)' }}>
            This is a placeholder for the Dashboard (Module 2).
            <br />
            Authentication flow works perfectly!
          </p>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;
