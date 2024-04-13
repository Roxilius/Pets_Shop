/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import { ShoppingBag } from "lucide-react";
import axios from "axios";
import { useCookies } from "react-cookie";

const CardList = ({ product, formatRupiah }) => {
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
      className="p-3 border rounded hover:border-orange-400 w-full flex gap-2 group cursor-pointer h-56"
    >
      <img src={`data:image/png;base64,${product.image}`} width={200} />
      <div className="flex flex-col gap-3">
        <h1 className="text-orange-400">
          {product.price.toLocaleString("id-ID", {
            style: "currency",
            currency: "IDR",
          })}
        </h1>
        <h1 className="hover:text-orange-400 text-lg font-bold">
          {product.name}
        </h1>
        <h1 className="text-slate-400 overflow-auto">{product.description}</h1>
        <div
          onClick={() => addTocart(product.id)}
          className="flex p-2 gap-1 w-2/5 items-center justify-center rounded-full hover:bg-orange-400 transition-all bg-violet-500"
        >
          <ShoppingBag size={20} /> Add To Cart
        </div>
      </div>
    </div>
  );
};

export default CardList;
