import axios from "axios";

const API_URL = "http://localhost:8081/api/users"; // User service URL

export const getAllUsers = async () => {
  try {
    const token = localStorage.getItem("token"); // nếu backend yêu cầu token
    const res = await axios.get(API_URL, {
      headers: token ? { Authorization: `Bearer ${token}` } : {},
    });
    return res.data; // trả về mảng users
  } catch (err) {
    console.error("Error fetching users:", err);
    return [];
  }
};
