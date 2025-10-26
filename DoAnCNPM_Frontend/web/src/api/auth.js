import axios from "axios";
const BASE_URL = "http://localhost:8081/api/auth";

export const login = async (email, password) => {
  const res = await axios.post(`${BASE_URL}/login`, { email, password });
  const data = res.data;
  localStorage.setItem("token", data.token);
  localStorage.setItem("role", data.role);
  return data;
};

export const register = async (userData) => {
  const res = await axios.post(`${BASE_URL}/register`, userData);
  return res.data;
};

export const logout = () => {
  localStorage.clear();
};
