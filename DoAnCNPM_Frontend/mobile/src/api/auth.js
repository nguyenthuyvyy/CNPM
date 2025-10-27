import axios from 'axios';
const API_URL = 'http://localhost:8085/api/auth';

export const login = async (email, password) => {
  const res = await axios.post(`${API_URL}/login`, { email, password });
  return res.data;
};

export const register = async (fullname, email, phone, password) => {
  const res = await axios.post(`${API_URL}/register`, {
    fullname,
    email,
    phone,
    password
  });
  return res.data;
};
