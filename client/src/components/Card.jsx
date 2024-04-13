/* eslint-disable no-unused-vars */
/* eslint-disable react-refresh/only-export-components */
/* eslint-disable react/prop-types */
import { ShoppingBag } from "lucide-react";
import axios from "axios";
import { useCookies } from "react-cookie";

export default function Card({ product }) {
  const [cookies, setCookies] = useCookies();
  function addTocart(id) {
    const request = {
      productId: id,
      quantity: 1,
    };
    axios
      .post("http://localhost:8080/cart", request, {
        headers: { Authorization: `Bearer ${cookies.user.token}` },
      })
      .then((res) => {
        console.log(res.data);
      });
  }

  return (
    <div
      key={product.id}
      className="p-3 border rounded hover:border-orange-400 flex flex-col gap-2 group cursor-pointer"
    >
      <div className="cursor-pointer relative overflow-hidden ">
        <img src={`data:image/png;base64,${product.image}`} />
        <div className="absolute w-full opacity-0 bottom-0 group-hover:opacity-100 transition-all duration-300">
          <div
            onClick={() => addTocart(product.id)}
            className="flex p-1 gap-1 rounded-full hover:bg-orange-400 transition-all bg-violet-500 items-center justify-center"
          >
            <ShoppingBag size={20} /> Add To Cart
          </div>
        </div>
      </div>
      <h4 className="text-violet-700">
        {product.price.toLocaleString("id-ID", {
          style: "currency",
          currency: "IDR",
        })}
      </h4>
      <h4 className="hover:text-orange-400 font-bold">{product.name}</h4>
    </div>
  );
}
