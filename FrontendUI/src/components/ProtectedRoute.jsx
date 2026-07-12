import React from 'react';
import { Navigate } from 'react-router-dom';
import { getAuthToken } from '../utils/token';

const ProtectedRoute = ({ children }) => {
  const token = getAuthToken();
  
  if (!token) {
    // If not authenticated, redirect to login
    return <Navigate to="/login" replace />;
  }
  
  return children;
};

export default ProtectedRoute;
