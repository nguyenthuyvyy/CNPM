import { useState } from "react";
import { login } from "../api/auth";

export default function LoginModal({ onClose }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const data = await login(email, password);

      localStorage.setItem("token", data.token);

      if (data.role === "CUSTOMER") {
        alert("Tài khoản khách hàng chỉ đăng nhập trên mobile app");
        localStorage.removeItem("token");
      } else if (data.role === "ADMIN") {
        window.location.href = "/admin";
      } else if (data.role === "RESTAURANT_OWNER") {
        window.location.href = "/owner";
      } else {
        alert("Role không hợp lệ");
        localStorage.removeItem("token");
      }

      onClose();
    } catch (err) {
      alert("Login failed: " + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/60 z-50">
      <div className="bg-black text-white p-8 rounded-lg w-full max-w-sm relative">
        <button
          className="absolute top-2 right-2 text-white text-xl"
          onClick={onClose}
        >
          ✕
        </button>
        <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="email"
            placeholder="Email Address"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full px-3 py-2 rounded bg-gray-800 border border-gray-700 text-white"
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full px-3 py-2 rounded bg-gray-800 border border-gray-700 text-white"
            required
          />
          <button
            type="submit"
            className="w-full bg-pink-600 py-2 rounded hover:bg-pink-700 disabled:opacity-50"
            disabled={loading}
          >
            {loading ? "Logging in..." : "LOGIN"}
          </button>
        </form>
        <div className="mt-4 text-center text-sm">
          <span>Don't have an account? </span>
          <a href="/register" className="text-pink-500">REGISTER</a>
        </div>
        <div className="mt-2 text-center text-sm">
          <a href="/forgot-password" className="text-pink-500">FORGOT PASSWORD</a>
        </div>
      </div>
    </div>
  );
}
