/* eslint-disable react/prop-types */
export default function Button({ children, className, onClick }) {
  return (
    <button
      className={`cursor-pointer flex items-center gap-2 bg-orange-400 hover:bg-orange-300 text-white p-2 rounded [&>svg]:w-4 [&>svg]:h-4 ${className}`}
      onClick={onClick}
    >
      {children}
    </button>
  );
}
