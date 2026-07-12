import apiClient from '../api/axios';
import { setAuthData, clearAuthData } from '../utils/token';

export const register = async (name, email, password) => {
    const response = await apiClient.post('/auth/register', {
        name,
        email,
        password
    });
    return response.data;
};

export const login = async (email, password) => {
    const response = await apiClient.post('/auth/login', {
        email,
        password
    });
    
    // Assuming backend returns { token, role, name } or similar structure
    // Since BackendAPI has JwtAuthenticationFilter and LoginService, check expected response.
    // Usually it's something like { token: "eyJhbG...", role: "ADMIN", name: "Romil" }
    // Or maybe just token, user: { name, role }
    // We'll store it according to standard flat structure, modify if backend structure is nested.
    if (response.data && response.data.token) {
        const token = response.data.token;
        let role = 'USER';
        let name = email;
        
        try {
            // Decode the JWT payload (the second part of the token)
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));
            
            const decoded = JSON.parse(jsonPayload);
            if (decoded.role) role = decoded.role;
            if (decoded.sub) name = decoded.sub; // sub usually holds the email/username
        } catch (e) {
            console.error('Failed to decode JWT', e);
        }

        setAuthData(token, role, name);
    }
    
    return response.data;
};

export const logout = () => {
    clearAuthData();
};
