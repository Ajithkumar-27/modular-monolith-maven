const API_BASE_URL = '/api/v1';

const api = {
    async get(endpoint, isPublic = false) {
        const headers = { 'Content-Type': 'application/json' };
        if (!isPublic) {
            const token = localStorage.getItem('token');
            if (token) headers['Authorization'] = `Bearer ${token}`;
        }
        const response = await fetch(`${API_BASE_URL}${endpoint}`, { headers });
        if (response.status === 401) {
            PNotify.error({ text: 'Session expired. Please login again.', delay: 3000 });
            localStorage.removeItem('token');
            setTimeout(() => window.location.reload(), 2000);
        }
        return response.json();
    },

    async post(endpoint, data, isPublic = false) {
        const headers = { 'Content-Type': 'application/json' };
        if (!isPublic) {
            const token = localStorage.getItem('token');
            if (token) headers['Authorization'] = `Bearer ${token}`;
        }
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers,
            body: JSON.stringify(data)
        });
        return response.json();
    },

    async put(endpoint, data) {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(data)
        });
        return response.ok;
    }
};
