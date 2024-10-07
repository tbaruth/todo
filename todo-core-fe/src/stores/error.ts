import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import type {AxiosError} from "axios";
import request from 'axios';

//TODO tbaruth feature/2 need to clean up the errors store--this is really sloppy
export const useErrorStore = defineStore('error', () => {
    const errors = ref([] as string[]);
    const addError = (err: any) => {
        const newError = err as AxiosError | Error;
        if (request.isAxiosError(newError)) {
            const axiosError = newError as AxiosError;
            if (axiosError.response) {
                errors.value.push(axiosError.response?.data as string);
            } else {
                errors.value.push(axiosError.code + '');
            }
        } else {
            errors.value.push('An error occurred');
        }
    };
    const removeError = (): void => {
        errors.value.pop();
    }
    return { errors, addError, removeError };
});
