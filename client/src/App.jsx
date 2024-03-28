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


function App() {
  const username = 'username';
  const password = '3ee85351-a96d-40a6-8fdb-11724ff001af';
  const basicAuthCredentials = btoa(`${username}:${password}`);
  const authHeader = {
    headers: {
      Authorization: `Basic ${basicAuthCredentials}`
    }
  };

  const handleClick = (e)=>{
    e.preventDefault()
    sessionStorage.setItem("email", "asd");
    const email = sessionStorage.getItem("email");
    fetch(`http://localhost:8080/auth/verify-email/${email}`, {
      method: 'POST',
      headers:{
        'Authorization': basicAuthCredentials
      }
    }).then((res) =>{
      console.log("berhasil");
    })
  }
  
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
