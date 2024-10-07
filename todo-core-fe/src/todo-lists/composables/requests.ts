import type {AxiosRequestConfig} from "axios";

const loadTodoLists = (): AxiosRequestConfig => {
    return {
        method: 'get',
        url: '/todo-lists'
    };
};

const createTodoList = (title: string): AxiosRequestConfig => {
    return {
        method: 'post',
        url: '/todo-lists',
        withCredentials: true,
        data: {
            title
        }
    };
};

const updateTodoList = (listId: number, title: string): AxiosRequestConfig => {
    return {
        method: 'put',
        url: '/todo-lists/' + listId,
        withCredentials: true,
        data: {
            title
        }
    };
};

const deleteTodoList = (listId: number): AxiosRequestConfig => {
    return {
        method: 'delete',
        url: '/todo-lists/' + listId,
        withCredentials: true
    };
};

export const TodoListsRequests = {
    loadTodoLists,
    createTodoList,
    updateTodoList,
    deleteTodoList
};
