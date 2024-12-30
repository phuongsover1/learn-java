import {useState} from "react";

export default function useToken() {
  const getToken = () => {
    const tokenResponse = localStorage.getItem("tokenResponse");
    return tokenResponse ? JSON.parse(tokenResponse) : "";
  };

  const [token, setToken] = useState(getToken());

  const saveToken = (tokenResponse) => {
    localStorage.setItem("tokenResponse", JSON.stringify(tokenResponse));
    setToken(tokenResponse);
  };

  return {
    setToken: saveToken,
    token,
  };
}
