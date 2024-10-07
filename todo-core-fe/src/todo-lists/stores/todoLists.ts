import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import {useBusyStore} from "@/stores/busy";
import axios, {AxiosError} from "axios";
import {apiRequest} from "@/composables/requests";
import {TodoListsRequests} from "@/todo-lists/composables/requests";
import {useErrorStore} from "@/stores/error";

export const useTodoListsStore = defineStore('todoLists', () => {
    const lists = ref(new Map<number, TodoList>());
    const listsArray = computed(() => {
        return Array.from(lists.value.values());
    })
    const getListById = (listId: number) => {
        return lists.value.get(listId);
    };
    const addList = (list: TodoList) => {
        lists.value.set(list.id, list);
    };
    const removeList = (listId: number) => {
        lists.value.delete(listId);
    };
    const loadTodoLists = async () => {
        const blockId = useBusyStore().addBlocking();
        try {
            const response = await apiRequest.request<TodoList[]>(TodoListsRequests.loadTodoLists());
            for (const todoList of response.data) {
                lists.value.set(todoList.id, todoList);
            }
        } catch (error) {
            useErrorStore().addError(error as Error | AxiosError);
        } finally {
            useBusyStore().removeBlocking(blockId);
        }
    };
    const saveTodoList = async (title: string, listId?: number | null) => {
        const blockId = useBusyStore().addBlocking();
        try {
            let response;
            if (listId != null) {
                response = await apiRequest.request<TodoList>(TodoListsRequests.updateTodoList(listId, title));
            } else {
                response = await apiRequest.request<TodoList>(TodoListsRequests.createTodoList(title));
            }
            lists.value.set(response.data.id, response.data);
        } catch (error) {
            useErrorStore().addError(error as Error | AxiosError);
        } finally {
            useBusyStore().removeBlocking(blockId);
        }
    }
    return { lists, listsArray, getListById, addList, removeList, loadTodoLists, saveTodoList };
});
