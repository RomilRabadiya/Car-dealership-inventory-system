import React from 'react';
import { Navigate } from 'react-router-dom';
import { getAuthToken, clearAuthData } from '../utils/token';
import { jwtDecode } from 'jwt-decode';
import { toast } from 'sonner';

const ProtectedRoute = ({ children, requireRole }) => {
  const token = getAuthToken();
  
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  try {
    const decoded = jwtDecode(token);
    
    // Check if token has expired
    if (decoded.exp * 1000 < Date.now()) {
      clearAuthData();
      toast.error('Session expired. Please log in again.');
      return <Navigate to="/login" replace />;
    }

    // Role-based routing
    if (requireRole && decoded.role !== requireRole) {
      toast.error('You do not have permission to access this page.');
      return <Navigate to="/dashboard" replace />;
    }
  } catch (error) {
    clearAuthData();
    return <Navigate to="/login" replace />;
  }
  
  return children;
};

export default ProtectedRoute;
