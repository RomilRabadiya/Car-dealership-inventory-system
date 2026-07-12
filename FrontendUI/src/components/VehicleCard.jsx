import React from 'react';

const VehicleCard = ({ vehicle, onPurchase, isAdmin, onEdit, onRestock, onDelete }) => {
  const isOutOfStock = vehicle.quantity <= 0;

  return (
    <div className="vehicle-card">
      <div className="vehicle-header">
        <h3 className="vehicle-title">{vehicle.make} {vehicle.model}</h3>
        <span className="vehicle-category">{vehicle.category}</span>
      </div>
      
      <div className="vehicle-body">
        <div>
          <div className="vehicle-price">${vehicle.price.toFixed(2)}</div>
          <div className={`vehicle-stock ${!isOutOfStock ? 'in-stock' : 'out-of-stock'}`}>
            {!isOutOfStock ? `${vehicle.quantity} In Stock` : 'Out of Stock'}
          </div>
        </div>
        <button 
          className="btn-outline" 
          disabled={isOutOfStock}
          onClick={() => onPurchase(vehicle.id)}
          style={{ width: 'auto', padding: '0.5rem 1rem' }}
        >
          Purchase
        </button>
      </div>

      {isAdmin && (
        <div style={{ display: 'flex', gap: '0.5rem', marginTop: '1rem', borderTop: '1px dashed var(--border-color)', paddingTop: '1rem' }}>
          <button className="btn-outline" onClick={() => onEdit(vehicle)} style={{ flex: 1, padding: '0.5rem' }}>Edit</button>
          <button className="btn-outline" onClick={() => onRestock(vehicle)} style={{ flex: 1, padding: '0.5rem' }}>Restock</button>
          <button className="btn-outline" onClick={() => onDelete(vehicle.id)} style={{ flex: 1, padding: '0.5rem', borderColor: 'var(--danger)', color: 'var(--danger)' }}>Delete</button>
        </div>
      )}
    </div>
  );
};

export default VehicleCard;
