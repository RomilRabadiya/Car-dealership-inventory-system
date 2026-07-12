import apiClient from '../api/axios';

export const getAllVehicles = async () => {
    const response = await apiClient.get('/vehicles');
    return response.data;
};

export const searchVehicles = async (filters) => {
    const response = await apiClient.get('/vehicles/search', { params: filters });
    return response.data;
};

export const purchaseVehicle = async (id) => {
    const response = await apiClient.post(`/vehicles/${id}/purchase`);
    return response.data;
};

export const restockVehicle = async (id, quantity) => {
    const response = await apiClient.post(`/vehicles/${id}/restock`, null, { params: { quantity } });
    return response.data;
};

export const addVehicle = async (vehicle) => {
    const response = await apiClient.post('/vehicles', vehicle);
    return response.data;
};

export const updateVehicle = async (id, vehicle) => {
    const response = await apiClient.put(`/vehicles/${id}`, vehicle);
    return response.data;
};

export const deleteVehicle = async (id) => {
    const response = await apiClient.delete(`/vehicles/${id}`);
    return response.data;
};
