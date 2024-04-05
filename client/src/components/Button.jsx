/* eslint-disable react/prop-types */
export default function Button({ children, className, onClick }) {
  return (
    <div
      className={`cursor-pointer flex items-center gap-2 bg-orange-400 hover:bg-violet-400 text-white p-2 rounded [&>svg]:w-4 [&>svg]:h-4 ${className}`}
      onClick={onClick}
    >
      {children}
    </div>
  );
}
