"use client";
import React, { useEffect, useState } from "react";
import { assets } from "@/assets/assets";
import Image from "next/image";
import { useRouter } from "next/navigation";

const Banner = () => {
  const router = useRouter();
  const [banner, setBanner] = useState(null);

  useEffect(() => {
    const fetchBanner = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/banners");
        if (res.ok) {
          const data = await res.json();
          // Find the gaming promo banner or use the first record
          const promo = data.find((b) => b.id === 4) || data[0];
          setBanner(promo);
        }
      } catch (err) {
        console.error("Failed to load promo banner:", err);
      }
    };
    fetchBanner();
  }, []);

  const productId = banner ? banner.productId : 5;

  return (
    <div className="flex flex-col md:flex-row items-center justify-between md:pl-20 py-14 md:py-0 bg-[#E6E9F2] my-16 rounded-xl overflow-hidden">
      <Image
        className="max-w-56"
        src={assets.jbl_soundbox_image}
        alt="jbl_soundbox_image"
      />
      <div className="flex flex-col items-center justify-center text-center space-y-2 px-4 md:px-0">
        <h2 className="text-2xl md:text-3xl font-semibold max-w-[290px]">
          {banner ? banner.title : "Level Up Your Gaming Experience"}
        </h2>
        <p className="max-w-[343px] font-medium text-gray-800/60">
          {banner
            ? banner.subtitle
            : "From immersive sound to precise controls—everything you need to win"}
        </p>
        <button
          onClick={() => router.push(`/product/${productId}`)}
          className="group flex items-center justify-center gap-1 px-12 py-2.5 bg-orange-600 rounded text-white hover:bg-orange-700 transition"
        >
          Buy now
          <Image
            className="group-hover:translate-x-1 transition"
            src={assets.arrow_icon_white}
            alt="arrow_icon_white"
          />
        </button>
      </div>
      <Image
        className="hidden md:block max-w-80"
        src={assets.md_controller_image}
        alt="md_controller_image"
      />
      <Image
        className="md:hidden"
        src={assets.sm_controller_image}
        alt="sm_controller_image"
      />
    </div>
  );
};

export default Banner;