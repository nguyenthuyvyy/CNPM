import { useState } from "react";
import LoginModal from "./LoginModal";
import RegisterModal from "./RegisterModal";
import ForgotPasswordModal from "./ForgotPasswordModal";

export default function Navbar() {
  const [showLogin, setShowLogin] = useState(false);
  const [showRegister, setShowRegister] = useState(false);
  const [showForgot, setShowForgot] = useState(false);

  const openLogin = () => {
    setShowRegister(false);
    setShowForgot(false);
    setShowLogin(true);
  };

  const openRegister = () => {
    setShowLogin(false);
    setShowForgot(false);
    setShowRegister(true);
  };

  const openForgot = () => {
    setShowLogin(false);
    setShowRegister(false);
    setShowForgot(true);
  };

  return (
    <>
      <nav className="flex justify-between items-center px-12 py-4 bg-pink-800 text-white shadow-md">
        <h1 className="text-3xl font-bold italic">FastFood</h1>

        <div className="flex items-center gap-10 text-2xl">
          <i
            className="fa-solid fa-user hover:text-pink-400 cursor-pointer transition-all duration-200 hover:scale-110"
            onClick={openLogin}
          ></i>
        </div>
      </nav>

      {showLogin && (
        <LoginModal
          onClose={() => setShowLogin(false)}
          onRegister={openRegister}
          onForgot={openForgot}
        />
      )}

      {showRegister && (
        <RegisterModal
          onClose={() => setShowRegister(false)}
          onLogin={openLogin} // 👉 truyền để gọi lại form Login
        />
      )}

      {showForgot && <ForgotPasswordModal onClose={() => setShowForgot(false)} />}
    </>
  );
}
