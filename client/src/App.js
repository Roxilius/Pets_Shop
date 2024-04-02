/* eslint-disable no-unused-expressions */
import { useEffect, useState } from "react";

function App() {
  const [products, setProducts] = useState([]);
  
  // const test = ()=>{
    
  // }
  
  
  useEffect(() =>{
    fetch(`http://localhost:8080/products/get-all-products`).then(res=>res.json()).then(res => {
      setProducts(res.data)
    })
  })
  return (
    <>
      {products.map((product => (
        <div key={product.id}>
          {product.name}
          <img src={product.image} alt="gambar" />
        </div>
      )))}
    </>
  );
}
export default App;
