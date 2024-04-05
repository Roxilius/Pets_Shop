/* eslint-disable no-unused-vars */
import Button from "@/components/Button";
import Card from "@/components/Card";
import Header from "@/components/Header";
import Popup from "@/components/Popup";
import axios from "axios";
import { PawPrint } from "lucide-react";
import { ShoppingBasket } from "lucide-react";
import { CircleUserRound } from "lucide-react";
import { LogIn, LogOut } from "lucide-react";
import { useEffect } from "react";
import { useState } from "react";
import { useCookies } from "react-cookie";
import { useNavigate } from "react-router-dom";

export default function Home() {
  const [cookies, setCookies] = useCookies();
  const navigate = useNavigate();

  const [products, setProducts] = useState([]);

  const [request, setRequest] = useState({
    email: "",
    password: "",
  });
  const handleChange = (e) => {
    setRequest({
      ...request,
      [e.target.name]: e.target.value,
    });
  };

  const [popup, setPopup] = useState(false);
  const openPopup = () => {
    // console.log(cookies.auth)
    cookies.auth ? 0 : setPopup(true);
  };
  const closePopup = () => {
    setPopup(false);
  };

  const [page, setPage] = useState(1);
  const [category, setCategory] = useState("");
  const [name, setName] = useState("");
  function getProducts() {
    fetch(
      `http://localhost:8080/products/get-all-products?page=${page}&name=${name}&category=${category}`
    )
    .then((res) => res.json())
    .then((res) => {
      setProducts(res.data.items);
    });
  }


  const submit = (e) => {
    e.preventDefault();
    axios.post("http://localhost:8080/auth/login", request).then((res) => {
      if (res.status === 200) {
        setCookies("user", res.data.data, { path: "/" });
        if (res.data.data.role === "USER") {
          navigate("/shop");
        } else if(res.data.data.role === "ADMIN"){
          navigate("/Admin");
        }
      }
    });
  };
  useEffect(() => {
    submit,
    getProducts
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
            onClick={openPopup}
          />
        </div>
      </Header>
      <main className="p-2 h-screen m-auto flex gap-5 w-4/5"></main>
      {popup && (
        <Popup closePopup={closePopup} title={"Sign in"}>
          <form className="space-y-4 md:space-y-6">
            <div>
              <label
                htmlFor="email"
                className="block mb-2 text-sm font-medium text-black"
              >
                Email
              </label>
              <input
                name="email"
                type="email"
                className="sm:text-sm rounded-lg block w-full p-2.5 bg-violet-500 placeholder-slate-50 text-white"
                placeholder="Email"
                required
                onChange={handleChange}
              />
            </div>
            <div>
              <label
                htmlFor="password"
                className="block mb-2 text-sm font-medium text-black"
              >
                Password
              </label>
              <input
                type="password"
                name="password"
                placeholder="••••••••"
                className="sm:text-sm rounded-lg block w-full p-2.5 bg-violet-500 placeholder-slate-50 text-white"
                required
                onChange={handleChange}
              />
            </div>
            <Button
              className={`focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 justify-center w-full`}
              onClick={submit}
            >
              Sign In
            </Button>
            <p>dont have account? <span>Sign Up</span></p>
          </form>
        </Popup>
      )}
    </>
  );
}
