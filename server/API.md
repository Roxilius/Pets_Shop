# User API Spec

## Register User

Endpoint : POST /api/register

Request Body :
```json
{
  "fullName": "",
  "gender": "",
  "phoneNumber": "",
  "email": "",
  "password": ""
}
```
Response Body(success) :
```json
{
  "success": true,
  "message": "Successfully Register New User",
  "data": {
    "fullName": "",
    "gender": "",
    "phoneNumber": "",
    "email": "",
    "password": ""
  }
}
```
Response Body(Failed) :
```json
{
  "success": false,
  "message": "Email Sudah Terdaftar",
  "data": null
}
```

## Login User

Endpoint : POST /api/auth/login

Request Body :
```json
{
  "email": "",
  "password": ""
}
```

Response Body(success) :
```json
{
  "success": true,
  "message": "Successfully login",
  "data": {
    "userName": "",
    "role": "USER",
    "token": "token"
  }
}
```
Response Body(failed) :
```json
{
  "success": false,
  "message": "Invalid Username or Password",
  "data": null
}
```
