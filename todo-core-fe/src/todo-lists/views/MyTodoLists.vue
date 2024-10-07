<script setup lang="ts">
import TodoListCard from "@/todo-lists/components/TodoListCard.vue";
import {useMyTodoListsStore} from "@/todo-lists/stores/myTodoLists";
import {useTodoListsStore} from "@/todo-lists/stores/todoLists";
import {onBeforeMount} from "vue";
import DeleteTodoListModal from "@/todo-lists/components/DeleteTodoListModal.vue";

const viewStore = useMyTodoListsStore();
const listsStore = useTodoListsStore();

onBeforeMount(async () => {
  await viewStore.initView();
});
</script>

<template>
  <DeleteTodoListModal />
  <div v-if="viewStore.loaded" class="container-fluid mw-320">
    <ul class="list-group">
      <li v-for="list in listsStore.listsArray" class="list-group-item d-flex align-items-center justify-content-between" :key="list.id">
        <TodoListCard :item="list"/>
      </li>
    </ul>
  </div>
  <div v-else class="container-fluid">
    <div class="spinner-border" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>
</template>

<style scoped>
.mw-320 {
  min-width: 320px;
}
</style>
