import hero from "../assets/hero.jpg";

export default function Hero() {
  return (
    <div
      className="relative h-[80vh] bg-cover bg-center"
      style={{ backgroundImage: `url(${hero})` }}
    >
      {/* Lớp phủ mờ để chữ nổi hơn */}
      <div className="absolute inset-0 bg-black bg-opacity-40"></div>

      {/* Text */}
      <div className="relative z-10 flex flex-col items-center justify-center h-full text-center text-white">
        <h1 className="text-5xl font-bold mb-4 drop-shadow-lg">
          HuongVyFood
        </h1>
        <p className="text-lg drop-shadow-md">
          Taste the Convenience: Food, Fast and Delivered.
        </p>
      </div>
    </div>
  );
}
