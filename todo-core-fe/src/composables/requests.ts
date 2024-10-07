import axios from "axios";
import type { AxiosRequestConfig } from "axios";

//TODO tbaruth feature/2 inject base api url
export const apiRequest = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
    timeout: 2000
});

const loadUser = (): AxiosRequestConfig => {
    return {
        method: 'get',
        url: '/users'
    };
};

const toggleDarkMode = (enabled: boolean): AxiosRequestConfig => {
    return {
        method: 'put',
        url: '/users',
        withCredentials: true,
        data: {
            enabled
        }
    };
};

export const UserRequests = {
    loadUser,
    toggleDarkMode
};
