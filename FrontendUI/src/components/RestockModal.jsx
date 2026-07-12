import React, { useState, useEffect } from 'react';

const RestockModal = ({ isOpen, onClose, onSubmit, vehicle }) => {
  const [quantity, setQuantity] = useState('');

  useEffect(() => {
    if (isOpen) setQuantity('');
  }, [isOpen]);

  if (!isOpen || !vehicle) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(vehicle.id, parseInt(quantity, 10));
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>Restock {vehicle.make} {vehicle.model}</h2>
        <form onSubmit={handleSubmit} className="search-form">
          <div className="form-group">
            <label className="form-label">Quantity to Add</label>
            <input 
              type="number" 
              value={quantity} 
              onChange={(e) => setQuantity(e.target.value)} 
              className="form-input" 
              min="1" 
              required 
            />
          </div>
          <div className="search-actions">
            <button type="button" onClick={onClose} className="btn-outline">Cancel</button>
            <button type="submit" className="btn-primary" style={{ marginTop: 0 }}>Restock</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default RestockModal;
