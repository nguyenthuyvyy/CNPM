export default function Navbar() {
  return (
    <nav className="flex items-center justify-between px-10 py-4 bg-white shadow-md">
      {/* Logo bên trái */}
      <h1 className="text-2xl font-bold italic text-orange-600">
        HuongVyFood
      </h1>

      {/* Icon bên phải */}
      <div className="flex items-center gap-6 text-2xl text-gray-800">
        <i className="fa-solid fa-cart-shopping cursor-pointer hover:text-orange-500 transition"></i>
        <i className="fa-solid fa-user cursor-pointer hover:text-orange-500 transition"></i>
      </div>
    </nav>
  );
}
