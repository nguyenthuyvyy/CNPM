import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://localhost:8085", // API Gateway port
});

axiosClient.interceptors.response.use(
  (response) => response.data,
  (error) => Promise.reject(error)
);

export default axiosClient;
