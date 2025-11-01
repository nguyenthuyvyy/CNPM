import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/autoplay";
import { Autoplay } from "swiper/modules";

const meals = [
  { name: "Burger", img: "https://images.unsplash.com/photo-1550547660-d9450f859349" },
  { name: "Pizza", img: "https://images.unsplash.com/photo-1613564834361-9436948817d1?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=743" },
  { name: "Sushi", img: "https://images.unsplash.com/photo-1579871494447-9811cf80d66c" },
  { name: "Steak", img: "https://images.unsplash.com/photo-1633436375795-12b3b339712f?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=687" },
  { name: "Tacos", img: "https://images.unsplash.com/photo-1627308595229-7830a5c91f9f" },
];

export default function TopMeals() {
  return (
    <section className="py-20 text-center bg-gradient-to-b from-black via-[#2b0a17] to-pink-800">
      {/* Tiêu đề */}
     <h2 className="text-4xl font-bold mb-10 text-white drop-shadow-[0_0_10px_rgba(255,192,203,0.4)] flex items-center gap-3 ml-12 -mt-6">
  <span className="text-5xl text-pink-300"></span> Top Meals
</h2>

      {/* Carousel */}
      <Swiper
        modules={[Autoplay]}
        slidesPerView={3}
        spaceBetween={30}
        autoplay={{ delay: 2000, disableOnInteraction: false }}
        loop={true}
        className="max-w-6xl mx-auto px-8"
        breakpoints={{
          0: { slidesPerView: 1 },
          640: { slidesPerView: 2 },
          1024: { slidesPerView: 3 },
        }}
      >
        {meals.map((meal, index) => (
          <SwiperSlide key={index}>
            <div className="group relative rounded-3xl overflow-hidden shadow-xl hover:shadow-2xl transition-all duration-500 transform hover:-translate-y-2">
              <img
                src={meal.img}
                alt={meal.name}
                className="w-full h-64 object-cover brightness-90 group-hover:brightness-110 transition-all duration-500"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-transparent flex items-end justify-center pb-4">
                <h3 className="text-2xl font-semibold text-white group-hover:text-pink-300 transition-colors duration-300 drop-shadow-lg">
                  {meal.name}
                </h3>
              </div>
            </div>
          </SwiperSlide>
        ))}
      </Swiper>
    </section>
  );
}
