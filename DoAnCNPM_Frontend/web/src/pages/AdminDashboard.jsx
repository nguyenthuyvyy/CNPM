import { useEffect, useState } from "react";
import AdminNavbar from "../components/AdminNavbar";
import UsersTable from "../components/UsersTable";
import ProductsTable from "../components/ProductsTable";
import OrdersTable from "../components/OrdersTable";
import { getAllUsers } from "../api/user";
import { getAllProducts } from "../api/product";
import { getAllOrders, updateOrderStatus, deleteOrder } from "../api/order";

export default function AdminDashboard() {
  const [activeTab, setActiveTab] = useState("users");
  const [users, setUsers] = useState([]);
  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);

  const fetchUsers = () => getAllUsers().then(setUsers).catch(console.error);
  const fetchProducts = () => getAllProducts().then(setProducts).catch(console.error);
  const fetchOrders = () => getAllOrders().then(setOrders).catch(console.error);

  useEffect(() => {
    fetchUsers();
    fetchProducts();
    fetchOrders();
  }, []);

  const handleUpdateOrderStatus = (id, status) => {
    updateOrderStatus(id, status).then(fetchOrders);
  };

  const handleDeleteOrder = (id) => {
    deleteOrder(id).then(fetchOrders);
  };

  return (
    <div className="p-6">
      <AdminNavbar activeTab={activeTab} setActiveTab={setActiveTab} />
      {activeTab === "users" && <UsersTable users={users} />}
      {activeTab === "products" && <ProductsTable products={products} />}
      {activeTab === "orders" && (
        <OrdersTable orders={orders} onUpdateStatus={handleUpdateOrderStatus} onDelete={handleDeleteOrder} />
      )}
    </div>
  );
}
