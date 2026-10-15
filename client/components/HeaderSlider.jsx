"use client";
import React, { useState, useEffect } from "react";
import { assets } from "@/assets/assets";
import Image from "next/image";
import { useRouter } from "next/navigation";

const defaultSlides = [
  {
    id: 1,
    productId: 2,
    title: "Experience Pure Sound - Your Perfect Headphones Awaits!",
    offer: "Limited Time Offer 30% Off",
    buttonText1: "Buy now",
    buttonText2: "Find more",
    imgSrc: assets.header_headphone_image,
  },
  {
    id: 2,
    productId: 5,
    title: "Next-Level Gaming Starts Here - Discover PlayStation 5 Today!",
    offer: "Hurry up only few lefts!",
    buttonText1: "Shop Now",
    buttonText2: "Explore Deals",
    imgSrc: assets.header_playstation_image,
  },
  {
    id: 3,
    productId: 7,
    title: "Power Meets Elegance - Apple MacBook Pro is Here for you!",
    offer: "Exclusive Deal 40% Off",
    buttonText1: "Order Now",
    buttonText2: "Learn More",
    imgSrc: assets.header_macbook_image,
  },
];

const HeaderSlider = () => {
  const router = useRouter();
  const [sliderData, setSliderData] = useState(defaultSlides);
  const [currentSlide, setCurrentSlide] = useState(0);

  useEffect(() => {
    const fetchBanners = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/banners");
        if (res.ok) {
          const data = await res.json();
          // Filter the top carousel banners (id 1 to 3)
          const slides = data.filter((item) => item.id <= 3);
          if (slides.length > 0) {
            const mappedSlides = slides.map((item, idx) => {
              const fallback = defaultSlides[idx] || defaultSlides[0];
              return {
                id: item.id,
                productId: item.productId || fallback.productId,
                title: `${item.title} - ${item.subtitle}`,
                offer: item.badgeText || fallback.offer,
                buttonText1: fallback.buttonText1,
                buttonText2: fallback.buttonText2,
                imgSrc: item.imageUrl || fallback.imgSrc,
              };
            });
            setSliderData(mappedSlides);
          }
        }
      } catch (err) {
        console.error("Using fallback slides, could not fetch banners:", err);
      }
    };

    fetchBanners();
  }, []);

  useEffect(() => {
    if (sliderData.length === 0) return;
    const interval = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % sliderData.length);
    }, 4000);
    return () => clearInterval(interval);
  }, [sliderData.length]);

  const handleSlideChange = (index) => {
    setCurrentSlide(index);
  };

  const handleNavigate = (productId) => {
    if (productId) {
      router.push(`/product/${productId}`);
    }
  };

  return (
    <div className="overflow-hidden relative w-full">
      <div
        className="flex transition-transform duration-700 ease-in-out"
        style={{
          transform: `translateX(-${currentSlide * 100}%)`,
        }}
      >
        {sliderData.map((slide, index) => (
          <div
            key={slide.id || index}
            className="flex flex-col-reverse md:flex-row items-center justify-between bg-[#E6E9F2] py-8 md:px-14 px-5 mt-6 rounded-xl min-w-full"
          >
            <div className="md:pl-8 mt-10 md:mt-0">
              <p className="md:text-base text-orange-600 pb-1 font-medium">
                {slide.offer}
              </p>
              <h1 className="max-w-lg md:text-[40px] md:leading-[48px] text-2xl font-semibold text-gray-800">
                {slide.title}
              </h1>
              <div className="flex items-center mt-4 md:mt-6 gap-3">
                <button
                  onClick={() => handleNavigate(slide.productId)}
                  className="md:px-10 px-7 md:py-2.5 py-2 bg-orange-600 rounded-full text-white font-medium hover:bg-orange-700 transition"
                >
                  {slide.buttonText1}
                </button>
                <button
                  onClick={() => handleNavigate(slide.productId)}
                  className="group flex items-center gap-2 px-6 py-2.5 font-medium text-gray-800 hover:text-orange-600 transition"
                >
                  {slide.buttonText2}
                  <Image
                    className="group-hover:translate-x-1 transition"
                    src={assets.arrow_icon}
                    alt="arrow_icon"
                  />
                </button>
              </div>
            </div>
            <div className="flex items-center flex-1 justify-center p-4">
              {typeof slide.imgSrc === "string" ? (
                <img
                  className="md:w-72 w-48 object-contain"
                  src={slide.imgSrc}
                  alt={slide.title}
                />
              ) : (
                <Image
                  className="md:w-72 w-48 object-contain"
                  src={slide.imgSrc}
                  alt={`Slide ${index + 1}`}
                />
              )}
            </div>
          </div>
        ))}
      </div>

      <div className="flex items-center justify-center gap-2 mt-8">
        {sliderData.map((_, index) => (
          <div
            key={index}
            onClick={() => handleSlideChange(index)}
            className={`h-2 rounded-full cursor-pointer transition-all ${
              currentSlide === index ? "bg-orange-600 w-6" : "bg-gray-500/30 w-2"
            }`}
          />
        ))}
      </div>
    </div>
  );
};

export default HeaderSlider;