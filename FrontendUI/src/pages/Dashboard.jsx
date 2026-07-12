import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { logout } from '../services/authService';
import { getAuthName, getAuthRole } from '../utils/token';
import { getAllVehicles } from '../services/vehicleService';
import VehicleCard from '../components/VehicleCard';

const Dashboard = () => {
  const navigate = useNavigate();
  const name = getAuthName();
  const role = getAuthRole();

  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  useEffect(() => {
    fetchVehicles();
  }, []);

  const fetchVehicles = async () => {
    try {
      setLoading(true);
      const data = await getAllVehicles();
      setVehicles(data);
    } catch (err) {
      setError('Failed to fetch inventory data.');
    } finally {
      setLoading(false);
    }
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
        <div className="dashboard-header">
          <h2 className="dashboard-title">Dashboard Overview</h2>
          <button className="btn-outline" onClick={fetchVehicles}>Refresh</button>
        </div>

        {error && <div className="error-message" style={{ marginBottom: '1rem' }}>{error}</div>}

        {loading ? (
          <div className="empty-state">Loading inventory...</div>
        ) : vehicles.length === 0 ? (
          <div className="empty-state">
            <h3>No Vehicles Found</h3>
            <p>Your inventory is currently empty.</p>
          </div>
        ) : (
          <div className="vehicle-grid">
            {vehicles.map((vehicle) => (
              <VehicleCard key={vehicle.id} vehicle={vehicle} />
            ))}
          </div>
        )}
      </main>
    </div>
  );
};

export default Dashboard;
