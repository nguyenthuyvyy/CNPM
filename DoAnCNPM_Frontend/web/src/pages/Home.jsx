import React from "react";
import Navbar from "../components/Navbar";
import Hero from "../components/Hero";
import Categories from "../components/Categories";
import TopMeals from "../components/TopMeals";

export default function Home() {
  return (
    <>
      <Navbar />
      <Hero />
      <Categories />
      <TopMeals />
    </>
  );
}
