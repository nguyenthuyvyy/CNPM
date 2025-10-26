import { useEffect, useState } from "react";
import { logout } from "../api/auth";
import { getAllProducts } from "../api/product";
import { getAllOrders, updateOrderStatus, deleteOrder } from "../api/order";
import ProductsTable from "../components/ProductsTable";
import OrdersTable from "../components/OrdersTable";

export default function OwnerDashboard() {
  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    getAllProducts().then(setProducts).catch(console.error);
    getAllOrders().then(setOrders).catch(console.error);
  }, []);

  const handleLogout = () => {
    logout();
    window.location.href = "/";
  };

  const handleUpdateOrderStatus = (id, status) => {
    updateOrderStatus(id, status).then(() =>
      getAllOrders().then(setOrders).catch(console.error)
    );
  };

  const handleDeleteOrder = (id) => {
    deleteOrder(id).then(() =>
      getAllOrders().then(setOrders).catch(console.error)
    );
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Owner Dashboard</h1>
      <button
        onClick={handleLogout}
        className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 mb-4"
      >
        Logout
      </button>
      <h2 className="text-xl font-semibold mt-4 mb-2">Products</h2>
      <ProductsTable products={products} />
      <h2 className="text-xl font-semibold mt-6 mb-2">Orders</h2>
      <OrdersTable
        orders={orders}
        onUpdateStatus={handleUpdateOrderStatus}
        onDelete={handleDeleteOrder}
      />
    </div>
  );
}
