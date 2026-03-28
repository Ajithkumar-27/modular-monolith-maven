let cart = [];
let currentUser = null;

// Routing
function showSection(sectionId) {
    const sections = ['hero', 'products', 'orders'];
    sections.forEach(s => {
        document.getElementById(s).style.display = (s === sectionId) ? 'block' : 'none';
        if (s === sectionId) document.getElementById(s).classList.add('fade-in');
    });
    if (sectionId === 'products') loadProducts();
    if (sectionId === 'orders') loadOrders();
}

// Theme
function toggleTheme() {
    const html = document.documentElement;
    const currentTheme = html.getAttribute('data-theme');
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    html.setAttribute('data-theme', newTheme);
    localStorage.setItem('theme', newTheme);
}

// Auth
function openModal(id) {
    document.getElementById(id).style.display = 'flex';
}

function closeModal(id) {
    document.getElementById(id).style.display = 'none';
}

async function handleLogin(e) {
    e.preventDefault();
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;
    
    try {
        const response = await api.post('/auth/login', { username, password }, true);
        if (response.token) {
            localStorage.setItem('token', response.token);
            currentUser = username;
            updateAuthUI();
            closeModal('login-modal');
            PNotify.success({ text: 'Login successful!', delay: 2000 });
        } else {
            PNotify.error({ text: 'Invalid credentials', delay: 3000 });
        }
    } catch (err) {
        PNotify.error({ text: 'Error logging in', delay: 3000 });
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const username = document.getElementById('register-username').value;
    const password = document.getElementById('register-password').value;
    
    try {
        const response = await api.post('/auth/register', { username, password }, true);
        if (response.token) {
            localStorage.setItem('token', response.token);
            currentUser = username;
            updateAuthUI();
            closeModal('register-modal');
            PNotify.success({ text: 'Registration successful!', delay: 2000 });
        }
    } catch (err) {
        PNotify.error({ text: 'Registration failed', delay: 3000 });
    }
}

function updateAuthUI() {
    const authButtons = document.getElementById('auth-buttons');
    const ordersLink = document.getElementById('orders-link');
    if (localStorage.getItem('token')) {
        authButtons.innerHTML = `<button class="btn" onclick="logout()">Logout</button>`;
        ordersLink.style.display = 'block';
    } else {
        authButtons.innerHTML = `<button class="btn btn-primary" onclick="openModal('login-modal')">Login</button>`;
        ordersLink.style.display = 'none';
    }
}

function logout() {
    localStorage.removeItem('token');
    cart = []; // Clear cart on logout
    updateCartUI();
    currentUser = null;
    updateAuthUI();
    showSection('hero');
    PNotify.info({ text: 'Logged out successfully', delay: 2000 });
}

// Products
async function loadProducts() {
    const productList = document.getElementById('product-list');
    productList.innerHTML = '<p>Loading products...</p>';
    try {
        const products = await api.get('/products', true);
        productList.innerHTML = products.map(p => `
            <div class="product-card glass fade-in">
                <img src="https://picsum.photos/seed/${p.id}/300/200" alt="${p.name}" class="product-image">
                <div class="product-info">
                    <h3>${p.name}</h3>
                    <p style="color: var(--secondary-color); font-size: 0.9rem; margin-bottom: 1rem;">${p.description}</p>
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <span class="product-price">$${p.price}</span>
                        <button class="btn btn-primary" onclick="addToCart(${p.id}, '${p.name}', ${p.price})">Add to Cart</button>
                    </div>
                </div>
            </div>
        `).join('');
    } catch (err) {
        productList.innerHTML = '<p>Failed to load products. Check console.</p>';
    }
}

// Cart
function toggleCart() {
    const drawer = document.getElementById('cart-drawer');
    drawer.classList.toggle('open');
}

function addToCart(id, name, price) {
    const existing = cart.find(item => item.id === id);
    if (existing) {
        existing.quantity++;
    } else {
        cart.push({ id, name, price, quantity: 1 });
    }
    updateCartUI();
    if (!document.getElementById('cart-drawer').classList.contains('open')) toggleCart();
}

function updateCartUI() {
    const container = document.getElementById('cart-items');
    const count = document.getElementById('cart-count');
    const total = document.getElementById('cart-total');
    
    const totalItems = cart.reduce((acc, item) => acc + item.quantity, 0);
    const totalPrice = cart.reduce((acc, item) => acc + (item.price * item.quantity), 0);
    
    count.innerText = totalItems;
    total.innerText = `Total: $${totalPrice.toFixed(2)}`;
    
    container.innerHTML = cart.map((item, index) => `
        <div style="display: flex; justify-content: space-between; margin-bottom: 1rem; align-items: center;">
            <div>
                <strong>${item.name}</strong><br>
                <small>${item.quantity} x $${item.price}</small>
            </div>
            <button class="btn" onclick="removeFromCart(${index})">✕</button>
        </div>
    `).join('');
}

function removeFromCart(index) {
    cart.splice(index, 1);
    updateCartUI();
}

async function checkout() {
    if (!localStorage.getItem('token')) {
        PNotify.notice({ text: 'Please login to place an order', delay: 3000 });
        openModal('login-modal');
        return;
    }
    if (cart.length === 0) {
        PNotify.notice({ text: 'Cart is empty', delay: 2000 });
        return;
    }

    try {
        const orderItems = cart.map(item => ({ productId: item.id, quantity: item.quantity }));
        const order = await api.post('/orders', orderItems);
        PNotify.success({ text: `Order #${order.id} placed successfully!`, delay: 3000 });
        
        // Process payment simulation
        const payment = await api.post(`/payments/process?orderId=${order.id}&amount=${order.totalAmount}`, {});
        PNotify.success({ text: `Payment processed! Status: ${payment.status}`, delay: 3000 });
        
        cart = [];
        updateCartUI();
        toggleCart();
        showSection('orders');
    } catch (err) {
        PNotify.error({ text: 'Order failed. See console.', delay: 3000 });
    }
}

// Orders
async function loadOrders() {
    const container = document.getElementById('order-list');
    container.innerHTML = '<p>Loading orders...</p>';
    try {
        const orders = await api.get('/orders/my-orders');
        container.innerHTML = orders.map(o => `
            <div class="glass" style="padding: 1.5rem; border-radius: 12px; margin-bottom: 1rem;">
                <div style="display: flex; justify-content: space-between;">
                    <strong>Order #${o.id}</strong>
                    <span style="background: ${o.status === 'PAID' ? '#10b981' : '#f59e0b'}; color: white; padding: 2px 8px; border-radius: 4px; font-size: 0.8rem;">${o.status}</span>
                </div>
                <p style="font-size: 0.9rem; margin-top: 0.5rem;">Date: ${new Date(o.orderDate).toLocaleDateString()}</p>
                <p style="font-weight: 700; margin-top: 0.5rem;">Total: $${o.totalAmount}</p>
            </div>
        `).join('');
    } catch (err) {
        container.innerHTML = '<p>Failed to load orders.</p>';
    }
}

// Initial Load
window.onload = () => {
    updateAuthUI();
    const savedTheme = localStorage.getItem('theme') || 'dark';
    document.documentElement.setAttribute('data-theme', savedTheme);
};
