import React from 'react';

const VehicleCard = ({ vehicle }) => {
  return (
    <div className="vehicle-card">
      <div className="vehicle-header">
        <h3 className="vehicle-title">{vehicle.make} {vehicle.model}</h3>
        <span className="vehicle-category">{vehicle.category}</span>
      </div>
      
      <div className="vehicle-body">
        <div className="vehicle-price">${vehicle.price.toFixed(2)}</div>
        <div className={`vehicle-stock ${vehicle.quantity > 0 ? 'in-stock' : 'out-of-stock'}`}>
          {vehicle.quantity > 0 ? `${vehicle.quantity} In Stock` : 'Out of Stock'}
        </div>
      </div>
    </div>
  );
};

export default VehicleCard;
