import axios from "axios";

//TODO tbaruth feature/2 inject base api url
export const apiRequest = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
    timeout: 2000
});
