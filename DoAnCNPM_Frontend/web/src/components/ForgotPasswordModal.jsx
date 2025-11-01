import { useState } from "react";
import axios from "axios";

export default function ForgotPasswordModal({ onClose }) {
  const [email, setEmail] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await axios.post(
        `http://localhost:8081/api/auth/forgot-password?email=${email}&newPassword=${newPassword}`
      );
      alert("Password updated successfully! You can now login with the new password.");
      onClose();
    } catch (err) {
      alert("Failed to reset password: " + (err.response?.data?.message || err.message));
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
        <h2 className="text-2xl font-bold mb-6 text-center">Forgot Password</h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="email"
            placeholder="Your Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full px-3 py-2 rounded bg-gray-800 border border-gray-700 text-white"
            required
          />
          <input
            type="password"
            placeholder="New Password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            className="w-full px-3 py-2 rounded bg-gray-800 border border-gray-700 text-white"
            required
          />

          <button
            type="submit"
            className="w-full bg-pink-600 py-2 rounded hover:bg-pink-700 disabled:opacity-50"
            disabled={loading}
          >
            {loading ? "Updating..." : "RESET PASSWORD"}
          </button>
        </form>
      </div>
    </div>
  );
}
