import { useState } from "react";

export default function AdminNavbar({ activeTab, setActiveTab }) {
  return (
    <nav className="bg-pink-800 text-white p-4 flex gap-4">
      <button
        className={`px-4 py-2 rounded ${activeTab === "users" ? "bg-pink-600" : ""}`}
        onClick={() => setActiveTab("users")}
      >
        Quản lý Users
      </button>
      <button
        className={`px-4 py-2 rounded ${activeTab === "products" ? "bg-pink-600" : ""}`}
        onClick={() => setActiveTab("products")}
      >
        Quản lý Products
      </button>
      <button
        className={`px-4 py-2 rounded ${activeTab === "orders" ? "bg-pink-600" : ""}`}
        onClick={() => setActiveTab("orders")}
      >
        Quản lý Orders
      </button>
    </nav>
  );
}
