"use client"
import { useEffect, useState } from "react";
import { assets } from "@/assets/assets";
import ProductCard from "@/components/ProductCard";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import Image from "next/image";
import { useParams } from "next/navigation";
import Loading from "@/components/Loading";
import { useAppContext } from "@/context/AppContext";
import React from "react";

const Product = () => {
    const { id } = useParams();
    const { products, router, addToCart } = useAppContext();

    const [mainImage, setMainImage] = useState(null);
    const [productData, setProductData] = useState(null);
    const [loading, setLoading] = useState(true);

    const fetchProductData = async () => {
        try {
            setLoading(true);
            const res = await fetch(`http://localhost:8080/api/products/${id}`);
            if (res.ok) {
                const data = await res.json();
                setProductData(data);
                if (data.images && data.images.length > 0) {
                    setMainImage(data.images[0]);
                }
            } else {
                setProductData(null);
            }
        } catch (error) {
            console.error("Failed to fetch product from Spring Boot:", error);
            setProductData(null);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (id) {
            fetchProductData();
        }
    }, [id]);

    if (loading) return <Loading />;
    if (!productData) return (
        <>
            <Navbar />
            <div className="text-center py-24 text-gray-500">Product not found.</div>
            <Footer />
        </>
    );

    const imageList = productData.images && productData.images.length > 0
        ? productData.images
        : [assets.upload_area];

    return (
        <>
            <Navbar />
            <div className="px-6 md:px-16 lg:px-32 pt-14 space-y-10">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-16">
                    <div className="px-5 lg:px-16 xl:px-20">
                        <div className="rounded-lg overflow-hidden bg-gray-500/10 mb-4 h-[380px] flex items-center justify-center p-4">
                            <img
                                src={mainImage || imageList[0]}
                                alt={productData.name}
                                className="w-full h-full object-contain mix-blend-multiply"
                            />
                        </div>

                        <div className="grid grid-cols-4 gap-4">
                            {imageList.map((img, index) => (
                                <div
                                    key={index}
                                    onClick={() => setMainImage(img)}
                                    className={`cursor-pointer rounded-lg overflow-hidden bg-gray-500/10 p-2 h-20 flex items-center justify-center border-2 ${
                                        (mainImage || imageList[0]) === img ? "border-orange-500" : "border-transparent"
                                    }`}
                                >
                                    <img
                                        src={img}
                                        alt={`Thumbnail ${index}`}
                                        className="w-full h-full object-contain mix-blend-multiply"
                                    />
                                </div>
                            ))}
                        </div>
                    </div>

                    <div className="flex flex-col">
                        <h1 className="text-3xl font-medium text-gray-800/90 mb-4">
                            {productData.name}
                        </h1>

                        <div className="flex items-center gap-2">
                            <div className="flex items-center gap-0.5">
                                <Image className="h-4 w-4" src={assets.star_icon} alt="star_icon" />
                                <Image className="h-4 w-4" src={assets.star_icon} alt="star_icon" />
                                <Image className="h-4 w-4" src={assets.star_icon} alt="star_icon" />
                                <Image className="h-4 w-4" src={assets.star_icon} alt="star_icon" />
                                <Image className="h-4 w-4" src={assets.star_dull_icon} alt="star_dull_icon" />
                            </div>
                            <p>({productData.rating || 4.5})</p>
                        </div>

                        <p className="text-gray-600 mt-3">
                            {productData.description}
                        </p>

                        <p className="text-3xl font-medium mt-6">
                            ${productData.offerPrice || productData.price}
                            {productData.offerPrice && (
                                <span className="text-base font-normal text-gray-800/60 line-through ml-2">
                                    ${productData.price}
                                </span>
                            )}
                        </p>

                        <hr className="bg-gray-600 my-6" />

                        <div className="overflow-x-auto">
                            <table className="table-auto border-collapse w-full max-w-72">
                                <tbody>
                                    <tr>
                                        <td className="text-gray-600 font-medium py-1">Brand</td>
                                        <td className="text-gray-800/70">{productData.brand || "Generic"}</td>
                                    </tr>
                                    <tr>
                                        <td className="text-gray-600 font-medium py-1">Color</td>
                                        <td className="text-gray-800/70">{productData.color || "Standard"}</td>
                                    </tr>
                                    <tr>
                                        <td className="text-gray-600 font-medium py-1">Category</td>
                                        <td className="text-gray-800/70">{productData.category}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <div className="flex items-center mt-10 gap-4">
                            <button
                                onClick={() => addToCart(productData.id)}
                                className="w-full py-3.5 bg-gray-100 text-gray-800/80 hover:bg-gray-200 transition font-medium rounded"
                            >
                                Add to Cart
                            </button>
                            <button
                                onClick={() => {
                                    addToCart(productData.id);
                                    router.push('/cart');
                                }}
                                className="w-full py-3.5 bg-orange-500 text-white hover:bg-orange-600 transition font-medium rounded"
                            >
                                Buy now
                            </button>
                        </div>
                    </div>
                </div>

                <div className="flex flex-col items-center">
                    <div className="flex flex-col items-center mb-4 mt-16">
                        <p className="text-3xl font-medium">Featured <span className="font-medium text-orange-600">Products</span></p>
                        <div className="w-28 h-0.5 bg-orange-600 mt-2"></div>
                    </div>
                    <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6 mt-6 pb-14 w-full">
                        {products.slice(0, 5).map((prod, index) => <ProductCard key={index} product={prod} />)}
                    </div>
                </div>
            </div>
            <Footer />
        </>
    );
};

export default Product;