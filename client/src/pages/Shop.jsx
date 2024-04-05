/* eslint-disable no-unused-vars */
import {
  PawPrint,
  ShoppingBasket,
  CircleUserRound,
  LayoutGrid,
  LayoutList,
} from "lucide-react";
import meowMix from "@/assets/moew-mix.png";
import Header from "@/components/Header";
import { useEffect } from "react";
import { useState } from "react";
import { useCookies } from "react-cookie";
import Card from "@/components/Card";
import CardList from "@/components/CardList";

export default function Shop() {
  const [cookies, setCookies] = useCookies({});
  const [grid, setGrid] = useState(true);
  const [list, setList] = useState(false);

  function formatRupiah(price) {
    const reverse = price.toString().split("").reverse().join("");
    const ribuan = reverse.match(/\d{1,3}/g);
    const konver = ribuan.join(".").split("").reverse().join("");
    return "Rp. " + konver;
  }

  function handleGrid() {
    setGrid(true);
    setList(false);
  }
  function handleList() {
    setGrid(false);
    setList(true);
  }

  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(1);
  const [category, setCategory] = useState("");
  const [name, setName] = useState("");
  const [orderBy, setOrderBy] = useState("");
  const [minPrice, setMinPrice] = useState(0);
  const [maxPrice, setMaxPrice] = useState(999999999);

  function handleClickCategory(e) {
    setName("");
    setOrderBy("")
    setCategory(e.target.innerText);
    getProducts();
  }

  function handleOrder(e) {
    setOrderBy(e.target.value);
    console.log(orderBy)
    getProducts();
  }

  function getProducts() {
    fetch(
      `http://localhost:8080/products/get-all-products?page=${page}&name=${name}&category=${category}&sortOrder=${orderBy}&sortBy=price&minPrice=${minPrice}&maxPrice=${maxPrice}`
    )
      .then((res) => res.json())
      .then((res) => {
        setProducts(res.data.items);
      });
  }

  useEffect(() => {
    getProducts();
  });

  return (
    <>
      <Header>
        <div className="flex gap-2 items-center">
          <ShoppingBasket
            className="hover:text-orange-400"
            cursor={"pointer"}
          />
          <CircleUserRound
            className="hover:text-orange-400"
            cursor={"pointer"}
          />
        </div>
      </Header>
      <section className="w-10/12 m-auto p-3 flex gap-2">
        <div className="rounded p-3 flex items-center bg-orange-500 w-3/6  overflow-hidden group">
          <div className="text-white flex flex-col gap-3">
            <p className="text-4xl">Mediciend & Pet Supplies</p>
          </div>
          <img
            src={meowMix}
            width={200}
            className="group-hover:translate-x-7 transition-all"
          />
        </div>
        <div className="rounded p-3 flex items-center bg-violet-600 w-3/6  overflow-hidden group">
          <div className="text-white flex flex-col gap-3">
            <p className="text-4xl">Premium Cat Food</p>
          </div>
          <img
            src={meowMix}
            width={200}
            className="group-hover:translate-x-7 transition-all"
          />
        </div>
      </section>
      <main className="p-2 h-screen m-auto flex gap-5 w-10/12">
        <div className="w-1/4 ">
          <div>
            <h2 className="p-2 ">Product Categories</h2>
            <hr />
            <ul className="p-1 gap-3 flex flex-col">
              <li className=" hover:text-orange-400 items-center ">
                <pre
                  className="text-sm flex gap-1 cursor-pointer"
                  onClick={handleClickCategory}
                >
                  <PawPrint size={20} />
                  Food
                </pre>
              </li>
              <li className=" hover:text-orange-400 items-center ">
                <pre
                  className="text-sm flex gap-1 cursor-pointer"
                  onClick={handleClickCategory}
                >
                  <PawPrint size={20} />
                  Obat - Obatan
                </pre>
              </li>
              <li className=" hover:text-orange-400 items-center ">
                <pre
                  className="text-sm flex gap-1 cursor-pointer"
                  onClick={handleClickCategory}
                >
                  <PawPrint size={20} />
                  Perlengkapan
                </pre>
              </li>
            </ul>
          </div>
          <div className="mt-5">
            <h2 className="p-2 text-xl">Filter By Price</h2>
            <hr />
          </div>
        </div>

        <div className="w-3/4 flex flex-col gap-2">
          <div className="flex gap-1">
            <LayoutGrid
              size={20}
              cursor={"pointer"}
              color={grid ? "orange" : "black"}
              onClick={handleGrid}
            />
            <LayoutList
              size={20}
              cursor={"pointer"}
              color={list ? "orange" : "black"}
              onClick={handleList}
            />
            <select onChange={handleOrder} name="shorting" className="text-sm min-w-min">
              <option value="">default Shorting</option>
              <option value="asc">Sort by price: low to high</option>
              <option value="dsc">Sort by price: high to low</option>
            </select>
          </div>
          {grid && (
            <div className="grid grid-cols-3 gap-3">
              {products &&
                products.map((product) => (
                  <Card
                    key={product.id}
                    product={product}
                    formatRupiah={formatRupiah}
                  />
                ))}
            </div>
          )}
          {list && (
            <div className="flex flex-wrap gap-2">
              {products &&
                products.map((product) => (
                  <CardList
                    key={product.id}
                    product={product}
                    formatRupiah={formatRupiah}
                  />
                ))}
            </div>
          )}
        </div>
      </main>
    </>
  );
}
