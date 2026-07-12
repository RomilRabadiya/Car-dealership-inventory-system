import React, { useState, useEffect } from 'react';

const VehicleModal = ({ isOpen, onClose, onSubmit, initialData }) => {
  const isEditing = !!initialData;
  const [formData, setFormData] = useState({
    make: '',
    model: '',
    category: '',
    price: '',
    quantity: ''
  });

  useEffect(() => {
    if (initialData) {
      setFormData({
        make: initialData.make || '',
        model: initialData.model || '',
        category: initialData.category || '',
        price: initialData.price || '',
        quantity: initialData.quantity || ''
      });
    } else {
      setFormData({ make: '', model: '', category: '', price: '', quantity: '' });
    }
  }, [initialData, isOpen]);

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({
      ...formData,
      price: parseFloat(formData.price),
      quantity: parseInt(formData.quantity, 10)
    });
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>{isEditing ? 'Edit Vehicle' : 'Add Vehicle'}</h2>
        <form onSubmit={handleSubmit} className="search-form">
          <div className="form-group">
            <label className="form-label">Make</label>
            <input type="text" name="make" value={formData.make} onChange={handleChange} className="form-input" required />
          </div>
          <div className="form-group">
            <label className="form-label">Model</label>
            <input type="text" name="model" value={formData.model} onChange={handleChange} className="form-input" required />
          </div>
          <div className="form-group">
            <label className="form-label">Category</label>
            <input type="text" name="category" value={formData.category} onChange={handleChange} className="form-input" required />
          </div>
          <div className="form-group">
            <label className="form-label">Price ($)</label>
            <input type="number" name="price" value={formData.price} onChange={handleChange} className="form-input" step="0.01" min="0" required />
          </div>
          <div className="form-group">
            <label className="form-label">Quantity</label>
            <input type="number" name="quantity" value={formData.quantity} onChange={handleChange} className="form-input" min="0" required />
          </div>
          <div className="search-actions">
            <button type="button" onClick={onClose} className="btn-outline">Cancel</button>
            <button type="submit" className="btn-primary" style={{ marginTop: 0 }}>Save</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default VehicleModal;
