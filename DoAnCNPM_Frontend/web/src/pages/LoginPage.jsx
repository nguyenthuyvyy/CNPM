import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Select, Card } from "antd";

const { Option } = Select;

export default function LoginPage() {
  const [role, setRole] = useState("");
  const navigate = useNavigate();

  const handleLogin = () => {
    if (role === "restaurant") navigate("/restaurant");
    else if (role === "admin") navigate("/admin");
  };

  return (
    <div className="h-screen flex items-center justify-center bg-gray-100">
      <Card title="FoodFast Delivery Web Portal" className="w-96 text-center shadow-lg">
        <Select
          placeholder="Chọn vai trò của bạn"
          className="w-full mb-4"
          onChange={(value) => setRole(value)}
        >
          <Option value="restaurant">Nhà hàng</Option>
          <Option value="admin">Quản trị viên</Option>
        </Select>
        <Button type="primary" className="w-full" onClick={handleLogin}>
          Đăng nhập
        </Button>
      </Card>
    </div>
  );
}
