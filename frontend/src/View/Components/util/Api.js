import axios from 'axios';
import {SERVER_URL} from "./Constants";
import jwt_decode from "jwt-decode";

const instance = axios.create({
    baseURL: SERVER_URL
})
instance.interceptors.request.use(config => {
    console.log(config)
    if (localStorage.getItem("Token") != null) {
        const token = localStorage.getItem("Token");
        const decoded = jwt_decode(token);
        config.headers.common.Authorization = `Bearer ${token}`;
        config.headers['Accept'] = "application/json";
        config.headers['Content-Type'] = "application/json";
    }
    return config;
},error => Promise.reject(error));
export default instance;
