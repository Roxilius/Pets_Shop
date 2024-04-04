/* eslint-disable no-unused-vars */
import Button from "@/components/Button";
import Header from "@/components/Header";
import Popup from "@/components/Popup";
import { useState } from "react";
import axios from "axios";
import { useCookies } from "react-cookie";
import { useNavigate } from "react-router-dom";

export default function Home() {
  const navigate = useNavigate();
  const [popUp, setPopUp] = useState(false);
  const [cookies, setCookies] = useCookies({});
  function openPopup() {
    setPopUp(true);
  }
  function closePopup() {
    setPopUp(false);
  }

  const [request, setRequest] = useState({
    email: "",
    password: ""
  });
  const handleChange = (e) => {
    setRequest({
      ...request,
      [e.target.name]: e.target.value
    })
  }
  const submit = (e) => {
    e.preventDefault();
    axios.post("http://localhost:8080/auth/login", request).then((res) => {
      if (res.status === 200) {
        setCookies("auth", res.data.data, { path: "/" });
        if (res.data.data.role === "ADMIN") {
          navigate("/home-admin");
        }
      }
    });
  };

  return (
    <>
      <Header>
        <Button onClick={openPopup}>Login</Button>
      </Header>
      {popUp && (
        <Popup closePopup={closePopup} title={"Sign in to your account"}>
          <form className="space-y-4 md:space-y-6">
            <div>
              <label
                htmlFor="email"
                className="block mb-2 text-sm font-medium text-white"
              >
                Email
              </label>
              <input
                name="email"
                type="email"
                required
                className="sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 bg-gray-700 border-gray-600 placeholder-gray-400 text-white"
                placeholder="email"
                onChange={handleChange}
              />
            </div>
            <div>
              <label
                htmlFor="password"
                className="block mb-2 text-sm font-medium text-white"
              >
                Password
              </label>
              <input
                type="password"
                name="password"
                placeholder="••••••••"
                className="sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 bg-gray-700 border-gray-600 placeholder-gray-400 text-white"
                required
                onChange={handleChange}
              />
            </div>
            <Button
              className={`focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 justify-center`}
              onClick={submit}
            >
              Sign In
            </Button>
          </form>
        </Popup>
      )}
    </>
  );
}
