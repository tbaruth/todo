import {ref} from 'vue'
import {defineStore} from 'pinia'
import {useBusyStore} from "@/stores/busy";
import {apiRequest, UserRequests} from "@/composables/requests";
import {useErrorStore} from "@/stores/error";
import {AxiosError} from "axios";

export const useUserStore = defineStore('user', () => {
    const user = ref(null as null | User);
    const loadUser = async () => {
        const blockId = useBusyStore().addBlocking();
        try {
            const response = await apiRequest.request<User>(UserRequests.loadUser());
            user.value = response.data;
        } catch (error) {
            useErrorStore().addError(error as Error | AxiosError);
        } finally {
            useBusyStore().removeBlocking(blockId);
        }
    };
    const toggleDarkMode = async (darkModeEnabled: boolean) => {
        const blockId = useBusyStore().addBlocking();
        try {
            const response = await apiRequest.request<User>(UserRequests.toggleDarkMode(darkModeEnabled));
            user.value = response.data;
        } catch (error) {
            useErrorStore().addError(error as Error | AxiosError);
        } finally {
            useBusyStore().removeBlocking(blockId);
        }
    };
    return {user, loadUser, toggleDarkMode};
});
