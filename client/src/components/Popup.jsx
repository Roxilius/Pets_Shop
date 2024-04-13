/* eslint-disable no-unused-vars */
import { AuthContext } from "@/pages";
import { CircleX, CircleChevronLeft } from "lucide-react";
import { useContext } from "react";
/* eslint-disable react/prop-types */
export default function Popup({ children, closePopup, title }) {

  const {login, setLogin, verifyEmail, setVerifyEmail, verifyOtp, setVerifyOtp, changePassword} = useContext(AuthContext)
  const handleback = () =>{
    if (verifyEmail) {
      setLogin(true);
      setVerifyEmail(false);
    } else if (verifyOtp){
      setVerifyEmail(true);
      setVerifyOtp(false);
    } else if(changePassword){
      setLogin(true);
      setVerifyEmail(false);
    }
  }
  return (
    <div className="w-screen h-screen fixed top-0 left-0 place-items-center place-content-center backdrop-blur-sm">
      <div className=" w-full m-auto rounded-lg shadow border md:mt-8 sm:max-w-md xl:p-0 bg-orange-100 transition-shadow border-gray-700">
        <div className="flex w-full justify-between p-0">
          {!login &&
            <CircleChevronLeft
              size={26}
              onClick={handleback}
              className="m-2 cursor-pointer text-orange-400 hover:text-violet-400"
            />
          }
          <CircleX
            size={26}
            onClick={closePopup}
            className="m-2 cursor-pointer text-orange-400 hover:text-violet-400"
          />
        </div>
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
