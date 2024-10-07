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
    const editModalShown = ref(false as boolean);
    const deleteModalShown = ref(false as boolean);
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
    const saveTodoList = async () => {
        const todoListStore = useTodoListsStore();
        await todoListStore.saveTodoList(form.value.title!, form.value.id);
        hideEditModal();
    }
    const deleteTodoList = async () => {
        const todoListStore = useTodoListsStore();
        await todoListStore.deleteTodoList(form.value.id!);
        hideDeleteModal();
    }
    const showEditModal = (id?: number) => {
        deleteModalShown.value = false;
        form.value = {
            title: null,
            id: id ?? null
        };
        editModalShown.value = true;
    }
    const hideEditModal = () => {
        form.value = {
            title: null,
            id: null
        };
        editModalShown.value = false;
    }
    const showDeleteModal = (id?: number) => {
        editModalShown.value = false;
        form.value = {
            title: null,
            id: id ?? null
        };
        deleteModalShown.value = true;
    }
    const hideDeleteModal = () => {
        form.value = {
            title: null,
            id: null
        };
        deleteModalShown.value = false;
    }
    return { form, editModalShown, deleteModalShown, loaded, initView, saveTodoList, deleteTodoList, showEditModal, hideEditModal, showDeleteModal, hideDeleteModal };
});
