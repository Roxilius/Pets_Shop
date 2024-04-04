/* eslint-disable react/jsx-no-undef */
/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import { CircleX } from "lucide-react";
export default function Popup({ popUp, children, closePopup, title }) {
  return (
    <div className="w-screen h-screen fixed top-0 left-0 place-items-center place-content-center backdrop-blur-sm">
      <div
        className="w-full m-auto rounded-lg shadow border md:mt-8 sm:max-w-md xl:p-0 bg-slate-400 transition-shadow border-gray-700"
      >
        <CircleX
          color="orange"
          size={26}
          onClick={closePopup}
          className="m-2 cursor-pointer right-0"
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
