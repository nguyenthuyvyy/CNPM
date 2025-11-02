import axios from "axios";

const AUTH_URL = "http://localhost:8081/api/auth";
const RESTAURANT_URL = "http://localhost:8083/api/restaurants";

// ===== LOGIN =====
export const login = async (email, password) => {
  const res = await axios.post(`${AUTH_URL}/login`, { email, password });
  const data = res.data;

  // Lưu token và role vào localStorage
  localStorage.setItem("token", data.token);
  localStorage.setItem("role", data.role);
  localStorage.setItem("email", data.email);

  return data;
};

// 🆕 ===== GET RESTAURANT BY OWNER EMAIL =====
export const getRestaurantByOwnerEmail = async (email) => {
  const res = await axios.get(`${RESTAURANT_URL}/owner`, { params: { email } });
  return res.data; // trả về restaurantId
};

// ===== REGISTER =====
export const register = async (userData) => {
  const res = await axios.post(`${AUTH_URL}/register`, userData);
  return res.data;
};

// ===== LOGOUT =====
export const logout = () => {
  localStorage.clear();
};
