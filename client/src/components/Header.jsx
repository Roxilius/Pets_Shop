/* eslint-disable react/prop-types */
import { useNavigate } from "react-router-dom";

const Header = ({children}) => {
  const navigate = useNavigate();

  return (
    <header className="bg-gray-50 flex justify-between z-50 text-black lg:py-2 px-3 sticky top-0 border border-y-gray-400">
      <div
        className="text-3xl font-bold cursor-pointer"
        onClick={() => {
          navigate('/')
        }}
      >
        LOGO
      </div>
      {children}
    </header>
  );
};

export default Header;
