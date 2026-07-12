import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { logout } from '../services/authService';
import { getAuthName, getAuthRole } from '../utils/token';
import { 
  getAllVehicles, 
  searchVehicles, 
  purchaseVehicle,
  addVehicle,
  updateVehicle,
  deleteVehicle,
  restockVehicle
} from '../services/vehicleService';
import VehicleCard from '../components/VehicleCard';
import SearchFilter from '../components/SearchFilter';
import VehicleModal from '../components/VehicleModal';
import RestockModal from '../components/RestockModal';
import SkeletonLoader from '../components/SkeletonLoader';
import Spinner from '../components/Spinner';
import { toast } from 'sonner';

const Dashboard = () => {
  const navigate = useNavigate();
  const name = getAuthName();
  const role = getAuthRole();
  const isAdmin = role === 'ADMIN';

  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isSearching, setIsSearching] = useState(false);

  // Admin Modal States
  const [isVehicleModalOpen, setIsVehicleModalOpen] = useState(false);
  const [isRestockModalOpen, setIsRestockModalOpen] = useState(false);
  const [selectedVehicle, setSelectedVehicle] = useState(null);

  useEffect(() => {
    fetchVehicles();
  }, []);



  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const fetchVehicles = async () => {
    try {
      setLoading(true);
      setIsSearching(false);
      const data = await getAllVehicles();
      if (Array.isArray(data)) {
        setVehicles(data);
      } else {
        throw new Error("Invalid API response. Expected an array.");
      }
    } catch (err) {
      toast.error('Failed to fetch inventory data.');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (filters) => {
    try {
      setLoading(true);
      if (Object.keys(filters).length === 0) {
        return fetchVehicles();
      }
      setIsSearching(true);
      const data = await searchVehicles(filters);
      if (Array.isArray(data)) {
        setVehicles(data);
      } else {
        throw new Error("Invalid API response. Expected an array.");
      }
    } catch (err) {
      toast.error('Failed to perform search.');
    } finally {
      setLoading(false);
    }
  };

  const handleClearSearch = () => {
    fetchVehicles();
  };

  const handlePurchase = async (id) => {
    try {
      const updatedVehicle = await purchaseVehicle(id);
      setVehicles(prevVehicles => prevVehicles.map(v => v.id === id ? updatedVehicle : v));
      toast.success(`Successfully purchased ${updatedVehicle.make} ${updatedVehicle.model}!`);
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to purchase vehicle. It may be out of stock.');
    }
  };

  // --- Admin Logic ---

  const handleOpenAdd = () => {
    setSelectedVehicle(null);
    setIsVehicleModalOpen(true);
  };

  const handleOpenEdit = (vehicle) => {
    setSelectedVehicle(vehicle);
    setIsVehicleModalOpen(true);
  };

  const handleOpenRestock = (vehicle) => {
    setSelectedVehicle(vehicle);
    setIsRestockModalOpen(true);
  };

  const handleVehicleSubmit = async (formData) => {
    try {
      if (selectedVehicle) {
        // Update
        const updated = await updateVehicle(selectedVehicle.id, formData);
        setVehicles(prev => prev.map(v => v.id === selectedVehicle.id ? updated : v));
        toast.success('Vehicle updated successfully!');
      } else {
        // Add
        const added = await addVehicle(formData);
        setVehicles(prev => [...prev, added]);
        toast.success('Vehicle added successfully!');
      }
      setIsVehicleModalOpen(false);
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to save vehicle.');
    }
  };

  const handleRestockSubmit = async (id, quantity) => {
    try {
      const updated = await restockVehicle(id, quantity);
      setVehicles(prev => prev.map(v => v.id === id ? updated : v));
      toast.success('Vehicle restocked successfully!');
      setIsRestockModalOpen(false);
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to restock vehicle.');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this vehicle?')) return;
    try {
      await deleteVehicle(id);
      setVehicles(prev => prev.filter(v => v.id !== id));
      toast.success('Vehicle deleted successfully!');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to delete vehicle.');
    }
  };

  return (
    <div className="app-container">
      <nav className="navbar">
        <div className="nav-brand">CarInventory</div>
        <div className="nav-links">
          <span className="nav-item">Welcome, {name} {isAdmin && '(ADMIN)'}</span>
          <button onClick={handleLogout} className="btn-outline">Logout</button>
        </div>
      </nav>
      
      <main className="main-content">
        <div className="dashboard-header">
          <h2 className="dashboard-title">Dashboard Overview</h2>
          <div style={{ display: 'flex', gap: '1rem' }}>
            {isAdmin && <button className="btn-primary" style={{ marginTop: 0 }} onClick={handleOpenAdd}>+ Add Vehicle</button>}
            <button className="btn-outline" onClick={fetchVehicles}>Refresh</button>
          </div>
        </div>

        <SearchFilter onSearch={handleSearch} onClear={handleClearSearch} />

        {loading ? (
          <div className="vehicle-grid">
            {[1, 2, 3, 4, 5, 6].map((n) => (
              <SkeletonLoader key={n} />
            ))}
          </div>
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
              <VehicleCard 
                key={vehicle.id} 
                vehicle={vehicle} 
                onPurchase={handlePurchase}
                isAdmin={isAdmin}
                onEdit={handleOpenEdit}
                onRestock={handleOpenRestock}
                onDelete={handleDelete}
              />
            ))}
          </div>
        )}
      </main>

      {/* Admin Modals */}
      <VehicleModal 
        isOpen={isVehicleModalOpen} 
        onClose={() => setIsVehicleModalOpen(false)} 
        onSubmit={handleVehicleSubmit}
        initialData={selectedVehicle}
      />

      <RestockModal 
        isOpen={isRestockModalOpen}
        onClose={() => setIsRestockModalOpen(false)}
        onSubmit={handleRestockSubmit}
        vehicle={selectedVehicle}
      />
    </div>
  );
};

export default Dashboard;
