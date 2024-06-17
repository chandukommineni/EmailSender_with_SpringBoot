import { customAxios } from "./helper";
import axios from "axios";
export async function sendEmail(emailData){

   const result= (await customAxios.post("/send",emailData)).data
   return result

}

export async function sendEmailWithFile(formData){
   const result=await axios.post('http://localhost:8080/api/email/sendwithfile', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
   return result
}