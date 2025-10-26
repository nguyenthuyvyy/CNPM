const ORDER_URL = "http://localhost:8082/api/orders"; // order-service

export async function getAllOrders() {
  const res = await fetch(ORDER_URL);
  return res.json();
}

export async function updateOrderStatus(id, status) {
  const res = await fetch(`${ORDER_URL}/${id}/status?status=${status}`, {
    method: "PUT",
  });
  return res.json();
}

export async function deleteOrder(id) {
  await fetch(`${ORDER_URL}/${id}`, { method: "DELETE" });
}
