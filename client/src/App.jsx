/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable react-hooks/rules-of-hooks */
/* eslint-disable no-undef */
/* eslint-disable no-unused-vars */
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSeparator,
  InputOTPSlot,
} from "@/components/ui/input-otp";
import { Button } from "./components/ui/button";
import { useState } from "react";
import { data } from "autoprefixer";
import { useEffect } from "react";
import axios from "axios";
import { any } from "zod";


function App() {
  const handleClick = (e)=>{
    e.preventDefault()
    sessionStorage.setItem("email", "asdqwe");
    const email = sessionStorage.getItem("email");
    const data = {
      email: email,
      password: "asd"
    }
    fetch(`http://localhost:8080/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    }).then(Response => Response.json()).then(res =>{
      console.log(res);
    })
  }

  useEffect(() =>{
    handleClick
  },[]);
  
  return (
    <>
      <form action="POST" onSubmit={handleClick} className="m-auto bg-red-200 justify-center">
        <InputOTP maxLength={6} onChange={() => setOtp(otp)}>
          <InputOTPGroup>
            <InputOTPSlot index={0} />
            <InputOTPSlot index={1} />
            <InputOTPSlot index={2} />
          </InputOTPGroup>
          <InputOTPSeparator />
          <InputOTPGroup>
            <InputOTPSlot index={3} />
            <InputOTPSlot index={4} />
            <InputOTPSlot index={5} />
          </InputOTPGroup>
        </InputOTP>
        <Button type={"submit"}>
          Verify
        </Button>
      </form>
    </>
  );
}

export default App;
