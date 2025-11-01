import hero from "../assets/hero.jpg"; // đảm bảo ảnh nằm đúng trong thư mục assets

export default function Hero() {
  return (
    <section
      className="relative h-[90vh] flex items-center justify-center bg-cover bg-center"
      style={{ backgroundImage: `url(${hero})` }}
    >
      {/* Lớp phủ tối mờ toàn màn hình */}
      <div className="absolute inset-0 bg-black/50"></div>

      {/* Lớp chuyển mờ ở phần dưới để hòa với TopMeals */}
      <div className="absolute bottom-0 left-0 right-0 h-40 bg-gradient-to-b from-transparent via-black/80 to-black"></div>

      {/* Nội dung Hero */}
      <div className="relative z-10 text-center text-white px-4">
        <h1 className="text-6xl font-extrabold mb-4 tracking-wide drop-shadow-[0_2px_10px_rgba(0,0,0,0.8)] animate-fadeIn">
          HuongVyFood
        </h1>
        <p className="text-2xl font-light drop-shadow-[0_2px_8px_rgba(0,0,0,0.7)] animate-fadeIn delay-200">
          Taste the Convenience: Food, Fast and Delivered.
        </p>
      </div>
    </section>
  );
}
