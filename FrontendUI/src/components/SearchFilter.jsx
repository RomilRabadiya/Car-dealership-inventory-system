import React, { useState } from 'react';

const SearchFilter = ({ onSearch, onClear }) => {
  const [filters, setFilters] = useState({
    make: '',
    model: '',
    category: '',
    minPrice: '',
    maxPrice: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Clean up empty string filters before sending
    const activeFilters = Object.fromEntries(
      Object.entries(filters).filter(([_, v]) => v.trim() !== '')
    );
    onSearch(activeFilters);
  };

  const handleClear = () => {
    setFilters({
      make: '',
      model: '',
      category: '',
      minPrice: '',
      maxPrice: ''
    });
    onClear();
  };

  return (
    <div className="search-filter-container">
      <form onSubmit={handleSubmit} className="search-form">
        <div className="search-inputs">
          <input
            type="text"
            name="make"
            placeholder="Make (e.g. Toyota)"
            value={filters.make}
            onChange={handleChange}
            className="form-input"
          />
          <input
            type="text"
            name="model"
            placeholder="Model (e.g. Corolla)"
            value={filters.model}
            onChange={handleChange}
            className="form-input"
          />
          <input
            type="text"
            name="category"
            placeholder="Category (e.g. Sedan)"
            value={filters.category}
            onChange={handleChange}
            className="form-input"
          />
          <input
            type="number"
            name="minPrice"
            placeholder="Min Price"
            value={filters.minPrice}
            onChange={handleChange}
            className="form-input"
            min="0"
          />
          <input
            type="number"
            name="maxPrice"
            placeholder="Max Price"
            value={filters.maxPrice}
            onChange={handleChange}
            className="form-input"
            min="0"
          />
        </div>
        <div className="search-actions">
          <button type="submit" className="btn-primary search-btn">Search</button>
          <button type="button" onClick={handleClear} className="btn-outline clear-btn">Clear</button>
        </div>
      </form>
    </div>
  );
};

export default SearchFilter;
