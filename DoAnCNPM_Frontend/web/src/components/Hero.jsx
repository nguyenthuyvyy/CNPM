import hero from "../assets/hero.jpg"; // Đảm bảo ảnh này đúng đường dẫn

export default function Hero() {
  return (
    <section
      className="relative h-[80vh] flex items-center justify-center bg-cover bg-center"
      style={{ backgroundImage: `url(${hero})` }}
    >
      {/* Lớp phủ tối */}
      <div className="absolute inset-0 bg-black/50"></div>

      {/* Nội dung chính */}
      <div className="relative z-10 text-center text-white px-4">
        <h1 className="text-6xl font-extrabold mb-4 tracking-wide text-white drop-shadow-[0_2px_10px_rgba(0,0,0,0.8)]">
  HuongVyFood
</h1>
<p className="text-2xl font-light text-white drop-shadow-[0_2px_8px_rgba(0,0,0,0.7)]">
  Taste the Convenience: Food, Fast and Delivered.
</p>

      </div>
    </section>
  );
}
