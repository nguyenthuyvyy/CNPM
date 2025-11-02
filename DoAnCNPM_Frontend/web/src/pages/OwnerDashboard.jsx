import { useParams } from "react-router-dom";

export default function OwnerDashboard() {
  const { restaurantId } = useParams();

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <h1 className="text-3xl font-bold mb-6">🍔 Quản lý nhà hàng #{restaurantId}</h1>
      <p>Chào mừng bạn! Đây là trang quản lý menu và đơn hàng của nhà hàng của bạn.</p>
      {/* TODO: bạn có thể thêm component MenuManagement và OrderManagement tại đây */}
    </div>
  );
}
