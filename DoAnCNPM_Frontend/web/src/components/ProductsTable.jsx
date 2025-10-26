export default function ProductsTable({ products }) {
  return (
    <table className="w-full border mt-4">
      <thead className="bg-gray-200">
        <tr>
          <th className="border px-3 py-2">ID</th>
          <th className="border px-3 py-2">Name</th>
          <th className="border px-3 py-2">Price</th>
          <th className="border px-3 py-2">Quantity</th>
        </tr>
      </thead>
      <tbody>
        {products.map(p => (
          <tr key={p.id}>
            <td className="border px-3 py-2">{p.id}</td>
            <td className="border px-3 py-2">{p.name}</td>
            <td className="border px-3 py-2">{Number(p.price).toLocaleString('vi-VN')}₫</td>
            <td className="border px-3 py-2">{p.quantity}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
