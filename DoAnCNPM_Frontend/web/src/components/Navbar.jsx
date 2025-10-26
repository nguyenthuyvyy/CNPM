import { useState } from "react";
import LoginModal from "./LoginModal";

export default function Navbar() {
  const [showLogin, setShowLogin] = useState(false);

  return (
    <>
      <nav className="flex justify-between items-center px-12 py-4 bg-pink-800 text-white shadow-md">
        <h1 className="text-3xl font-bold italic">HuongVyFood</h1>

        <div className="flex items-center gap-10 text-2xl">
          <i
            className="fa-solid fa-user hover:text-pink-400 cursor-pointer transition-all duration-200 hover:scale-110"
            onClick={() => setShowLogin(true)}
          ></i>
          <i className="fa-solid fa-cart-shopping hover:text-pink-400 cursor-pointer transition-all duration-200 hover:scale-110"></i>
        </div>
      </nav>

      {showLogin && <LoginModal onClose={() => setShowLogin(false)} />}
    </>
  );
}
