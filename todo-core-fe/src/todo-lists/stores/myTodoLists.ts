import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import {useTodoListsStore} from "@/todo-lists/stores/todoLists";
import {useBusyStore} from "@/stores/busy";
import {useErrorStore} from "@/stores/error";

export const useMyTodoListsStore = defineStore('myTodoLists', () => {
    const form = ref({
        title: null as null | string,
        id: null as null | number
    });
    const loaded = ref(null as null | boolean);
    const initView = async () => {
        if (loaded.value == null) {
            loaded.value = false;
            const busyStore = useBusyStore();
            const blockingUuid = busyStore.addBlocking();
            try {
                const todoListsStore = useTodoListsStore();
                await todoListsStore.loadTodoLists();
                loaded.value = true;
            } catch (error) {
                useErrorStore().addError(error);
                loaded.value = null;
            } finally {
                busyStore.removeBlocking(blockingUuid);
            }
        }
    }
    return { form, loaded, initView };
});
