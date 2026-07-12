import React from 'react';

const Spinner = ({ size = 'md', className = '' }) => {
    return (
        <div className={`spinner-wrapper ${size} ${className}`}>
            <div className="spinner-circle"></div>
        </div>
    );
};

export default Spinner;
