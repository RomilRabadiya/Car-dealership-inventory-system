import React from 'react';

const SkeletonLoader = () => {
    return (
        <div className="skeleton-card">
            <div className="skeleton-img"></div>
            <div className="skeleton-content">
                <div className="skeleton-header">
                    <div className="skeleton-line w-50"></div>
                    <div className="skeleton-line w-25"></div>
                </div>
                <div className="skeleton-body">
                    <div className="skeleton-line w-75"></div>
                    <div className="skeleton-line w-50"></div>
                </div>
                <div className="skeleton-footer">
                    <div className="skeleton-line w-30"></div>
                    <div className="skeleton-btn"></div>
                </div>
            </div>
        </div>
    );
};

export default SkeletonLoader;
