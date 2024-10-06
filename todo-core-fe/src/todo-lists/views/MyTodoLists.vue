<script setup lang="ts">
import TodoListItem from "@/todo-lists/components/TodoListCard.vue";
import {useMyTodoListsStore} from "@/todo-lists/stores/myTodoLists";
import {useTodoListsStore} from "@/todo-lists/stores/todoLists";

const viewStore = useMyTodoListsStore();
const listsStore = useTodoListsStore();

await viewStore.initView();
</script>

<template>
  <div class="container-fluid">
    <ul class="list-group">
      <li v-for="list in listsStore.listsArray" class="list-group-item d-flex" :key="list.id">
        <TodoListItem :item="list" />
      </li>
    </ul>
  </div>
</template>

<style scoped>
.item-wrapper {
  border: black 1px solid;
}
@media (min-width: 1024px) {
  .item-wrapper {
    min-width: 100vw;
    display: flex;
    align-items: center;
    gap: 30px;
    padding: 10px;
  }
}
</style>
