export const setAuthData = (token, role, name) => {
    localStorage.setItem("token", token);
    localStorage.setItem("role", role);
    localStorage.setItem("name", name);
};

export const getAuthToken = () => {
    return localStorage.getItem("token");
};

export const getAuthRole = () => {
    return localStorage.getItem("role");
};

export const getAuthName = () => {
    return localStorage.getItem("name");
};

export const clearAuthData = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("name");
};
