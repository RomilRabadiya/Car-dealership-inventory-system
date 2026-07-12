import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { logout } from '../services/authService';
import { getAuthName, getAuthRole } from '../utils/token';
import { getAllVehicles, searchVehicles } from '../services/vehicleService';
import VehicleCard from '../components/VehicleCard';
import SearchFilter from '../components/SearchFilter';

const Dashboard = () => {
  const navigate = useNavigate();
  const name = getAuthName();
  const role = getAuthRole();

  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isSearching, setIsSearching] = useState(false);

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
      setIsSearching(false);
      const data = await getAllVehicles();
      setVehicles(data);
    } catch (err) {
      setError('Failed to fetch inventory data.');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (filters) => {
    try {
      setLoading(true);
      // If there are no filters at all, just fetch all
      if (Object.keys(filters).length === 0) {
        return fetchVehicles();
      }
      setIsSearching(true);
      const data = await searchVehicles(filters);
      setVehicles(data);
    } catch (err) {
      setError('Failed to perform search.');
    } finally {
      setLoading(false);
    }
  };

  const handleClearSearch = () => {
    fetchVehicles();
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

        <SearchFilter onSearch={handleSearch} onClear={handleClearSearch} />

        {error && <div className="error-message" style={{ marginBottom: '1rem' }}>{error}</div>}

        {loading ? (
          <div className="empty-state">Loading inventory...</div>
        ) : vehicles.length === 0 ? (
          <div className="empty-state">
            {isSearching ? (
              <>
                <h3>No Results Found</h3>
                <p>Try adjusting or clearing your search filters.</p>
              </>
            ) : (
              <>
                <h3>No Vehicles Found</h3>
                <p>Your inventory is currently empty.</p>
              </>
            )}
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
