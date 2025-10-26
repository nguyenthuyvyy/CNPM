export default function UsersTable({ users }) {
  return (
    <table className="w-full border mt-4">
      <thead className="bg-gray-200">
        <tr>
          <th className="border px-3 py-2">ID</th>
          <th className="border px-3 py-2">Fullname</th>
          <th className="border px-3 py-2">Role</th>
          <th className="border px-3 py-2">Email</th>
          <th className="border px-3 py-2">Phone</th>
        </tr>
      </thead>
      <tbody>
        {users.map(u => (
          <tr key={u.id}>
            <td className="border px-3 py-2">{u.id}</td>
            <td className="border px-3 py-2">{u.fullname}</td>
            <td className="border px-3 py-2">{u.role}</td>
            <td className="border px-3 py-2">{u.email}</td>
            <td className="border px-3 py-2">{u.phone}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
