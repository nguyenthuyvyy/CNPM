import { useState } from "react";

export default function AddRestaurant() {
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    cuisineType: "",
    openingHours: "Mon-Sun: 9:00 AM - 9:00 PM",
    streetAddress: "",
    city: "",
    state: "",
    postalCode: "",
    country: "",
    email: "",
    mobile: "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Restaurant data:", formData);
    // TODO: Gửi dữ liệu lên API backend
  };

  return (
    <div className="max-w-3xl mx-auto mt-10 p-6 bg-gray-900 text-white rounded-lg shadow-lg">
      <h1 className="text-2xl font-bold mb-6 text-center">Add New Restaurant</h1>
      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Image upload */}
        <div className="flex items-center justify-center border-2 border-dashed border-gray-600 p-4 rounded-lg cursor-pointer hover:border-pink-400">
          <span>Upload Image</span>
          <input
            type="file"
            name="image"
            className="hidden"
            // onChange={handleImageUpload}
          />
        </div>

        {/* Name */}
        <input
          type="text"
          name="name"
          placeholder="Name"
          value={formData.name}
          onChange={handleChange}
          className="w-full p-3 rounded-lg bg-gray-800 border border-gray-700"
        />

        {/* Description */}
        <input
          type="text"
          name="description"
          placeholder="Description"
          value={formData.description}
          onChange={handleChange}
          className="w-full p-3 rounded-lg bg-gray-800 border border-gray-700"
        />

        {/* Cuisine Type & Opening Hours */}
        <div className="flex gap-4">
          <input
            type="text"
            name="cuisineType"
            placeholder="Cuisine Type"
            value={formData.cuisineType}
            onChange={handleChange}
            className="flex-1 p-3 rounded-lg bg-gray-800 border border-gray-700"
          />
          <input
            type="text"
            name="openingHours"
            value={formData.openingHours}
            onChange={handleChange}
            className="flex-1 p-3 rounded-lg bg-gray-800 border border-gray-700"
          />
        </div>

        {/* Address */}
        <input
          type="text"
          name="streetAddress"
          placeholder="Street Address"
          value={formData.streetAddress}
          onChange={handleChange}
          className="w-full p-3 rounded-lg bg-gray-800 border border-gray-700"
        />

        {/* City, State, Postal Code */}
        <div className="flex gap-4">
          <input
            type="text"
            name="city"
            placeholder="City"
            value={formData.city}
            onChange={handleChange}
            className="flex-1 p-3 rounded-lg bg-gray-800 border border-gray-700"
          />
          <input
            type="text"
            name="state"
            placeholder="State/Province"
            value={formData.state}
            onChange={handleChange}
            className="flex-1 p-3 rounded-lg bg-gray-800 border border-gray-700"
          />
          <input
            type="text"
            name="postalCode"
            placeholder="Postal Code"
            value={formData.postalCode}
            onChange={handleChange}
            className="flex-1 p-3 rounded-lg bg-gray-800 border border-gray-700"
          />
        </div>

        {/* Country */}
        <input
          type="text"
          name="country"
          placeholder="Country"
          value={formData.country}
          onChange={handleChange}
          className="w-full p-3 rounded-lg bg-gray-800 border border-gray-700"
        />

        {/* Email & Mobile */}
        <div className="flex gap-4">
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={formData.email}
            onChange={handleChange}
            className="flex-1 p-3 rounded-lg bg-gray-800 border border-gray-700"
          />
          <input
            type="text"
            name="mobile"
            placeholder="Mobile"
            value={formData.mobile}
            onChange={handleChange}
            className="flex-1 p-3 rounded-lg bg-gray-800 border border-gray-700"
          />
        </div>

        <button
          type="submit"
          className="w-full bg-pink-500 hover:bg-pink-600 p-3 rounded-lg font-bold"
        >
          Add Restaurant
        </button>
      </form>
    </div>
  );
}
