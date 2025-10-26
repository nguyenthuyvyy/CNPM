import { useEffect, useState } from "react";
import { getAllUsers } from "../api/user";
import UsersTable from "../components/UsersTable";

export default function AdminDashboard() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    getAllUsers().then(setUsers).catch(console.error);
  }, []);

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Admin Dashboard</h1>
      <UsersTable users={users} />
    </div>
  );
}
