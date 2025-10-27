import axios from 'axios';
const API_URL = 'http://localhost:8085/order';

export const createOrder = async (order, token) => {
  const res = await axios.post(`${API_URL}`, order, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return res.data;
};

export const getOrders = async (token) => {
  const res = await axios.get(`${API_URL}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return res.data;
};
