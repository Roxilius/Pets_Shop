/* eslint-disable no-unused-vars */
import { CircleX } from "lucide-react";
import Button from "./Button";
import { motion } from "framer-motion";
/* eslint-disable react/prop-types */
export default function Popup({ popUp, children, closePopup, title }) {
  return (
    <div className="w-screen h-screen fixed top-0 left-0 place-items-center place-content-center backdrop-blur-sm">
      <div className=" w-full m-auto rounded-lg shadow border md:mt-8 sm:max-w-md xl:p-0 bg-orange-100 transition-shadow border-gray-700">
        <CircleX
          size={26}
          onClick={closePopup}
          className="m-2 cursor-pointer text-orange-400 right-0 hover:text-violet-400"
        />
        <div className="p-7 pt-3">
          <h1 className="text-xl font-bold leading-tight tracking-tight md:text-2xl text-black text-center">
            {title}
          </h1>
          {children}
        </div>
      </div>
    </div>
  );
}
