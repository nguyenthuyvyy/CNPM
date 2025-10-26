import axios from "axios";
const API_URL = "http://localhost:8081/api/users";

export const getAllUsers = async () => {
  const token = localStorage.getItem("token");
  const res = await axios.get(API_URL, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
};
