/* eslint-disable react/prop-types */
export default function Header({ children }) {
  return (
    <header className="bg-orange-500 flex justify-between z-50 text-black lg:py-2 px-3 sticky top-0">
      <div className="text-3xl font-bold cursor-pointer"
      >
        LOGO
      </div>
      {children}
    </header>
  );
}
