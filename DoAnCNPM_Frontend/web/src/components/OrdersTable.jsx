export default function OrdersTable({ orders, onUpdateStatus, onDelete }) {
  return (
    <table className="w-full border mt-4">
      <thead className="bg-gray-200">
        <tr>
          <th className="border px-3 py-2">ID</th>
          <th className="border px-3 py-2">UserID</th>
          <th className="border px-3 py-2">Fullname</th>
          <th className="border px-3 py-2">Phone</th>
          <th className="border px-3 py-2">Address</th>
          <th className="border px-3 py-2">Total</th>
          <th className="border px-3 py-2">Status</th>
          <th className="border px-3 py-2">Actions</th>
        </tr>
      </thead>
      <tbody>
        {orders.map(o => (
          <tr key={o.id}>
            <td className="border px-3 py-2">{o.id}</td>
            <td className="border px-3 py-2">{o.userId}</td>
            <td className="border px-3 py-2">{o.fullname}</td>
            <td className="border px-3 py-2">{o.phone}</td>
            <td className="border px-3 py-2">{o.address}</td>
            <td className="border px-3 py-2">{Number(o.total).toLocaleString('vi-VN')}₫</td>
            <td className="border px-3 py-2">{["Processing","Delivery","Done"][o.status]}</td>
            <td className="border px-3 py-2 flex gap-2">
              {o.status < 2 && (
                <button
                  className="bg-green-500 px-2 py-1 text-white rounded"
                  onClick={() => onUpdateStatus(o.id, o.status + 1)}
                >
                  Next
                </button>
              )}
              <button
                className="bg-red-500 px-2 py-1 text-white rounded"
                onClick={() => onDelete(o.id)}
              >
                Delete
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
