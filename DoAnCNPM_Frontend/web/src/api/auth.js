import axios from "axios";

const BASE_URL = "http://localhost:8081/api/auth"; // user-service hoặc gateway tùy cấu hình của bạn

// ===== LOGIN =====
export const login = async (email, password) => {
  const res = await axios.post(`${BASE_URL}/login`, { email, password });
  const data = res.data;

  // Lưu token và role vào localStorage
  localStorage.setItem("token", data.token);
  localStorage.setItem("role", data.role);

  return data;
};

// ===== REGISTER =====
export const register = async (userData) => {
  const res = await axios.post(`${BASE_URL}/register`, userData);
  return res.data;
};

// ===== LOGOUT =====
export const logout = () => {
  localStorage.clear();
};
