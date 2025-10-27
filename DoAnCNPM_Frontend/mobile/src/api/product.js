import axios from 'axios';
const API_URL = 'http://localhost:8085/product';

export const getAllProducts = async () => {
  const res = await axios.get(`${API_URL}`);
  return res.data;
};
