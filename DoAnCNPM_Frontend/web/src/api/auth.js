import axios from "axios";

const API_URL = "http://localhost:8081/api/auth";

export const login = async (email, password) => {
  try {
    const res = await axios.post(`${API_URL}/login`, { email, password }, {
      headers: {
        "Content-Type": "application/json"
      }
    });
    return res.data; // { token, fullname, email, phone, role }
  } catch (err) {
    console.error(err.response?.data || err.message);
    throw err;
  }
};
