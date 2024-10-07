<script setup lang="ts">
import {computed} from "vue";
import {useUserStore} from "@/stores/user";
import {useMyTodoListsStore} from "@/todo-lists/stores/myTodoLists";

const myTodoListsStore = useMyTodoListsStore();
const userStore = useUserStore();

const props = defineProps<{
  item: TodoList
}>()

const createdDate = computed(() => {
  if (props.item.created) {
    return formatDate(props.item.created);
  }
  return '';
});

const updatedDate = computed(() => {
  if (props.item.updated) {
    return formatDate(props.item.updated);
  }
  return '';
});

const viewButtonThemeClass = computed(() => {
  return userStore.user?.darkMode ? 'btn-outline-info' : 'btn-info';
});

const editButtonThemeClass = computed(() => {
  return userStore.user?.darkMode ? 'btn-outline-primary' : 'btn-primary';
});

const deleteButtonThemeClass = computed(() => {
  return userStore.user?.darkMode ? 'btn-outline-danger' : 'btn-danger';
});

const formatDate = (dateString: string): string => {
  const date = new Date(dateString);
  return date.toLocaleDateString('UTC', {year: 'numeric', month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric'});
}
</script>

<template>
  <div>
    <div class="fw-bold fs-3 mb-1">{{ props.item.title }}</div>
    <ul class="list-group list-group-horizontal-lg borderless">
      <li class="list-group-item"><strong>Completed: </strong> {{props.item.numCompleted}}/{{props.item.numItems}}</li>
      <li class="list-group-item"><strong>Created: </strong> {{ createdDate }}</li>
      <li class="list-group-item"><strong>Last Updated: </strong> {{ updatedDate }}</li>
    </ul>
  </div>
  <div class="buttons">
    <button class="btn m-1" :class="viewButtonThemeClass" disabled>View Todos--Not yet implemented :(</button>
    <button class="btn m-1" :class="editButtonThemeClass" @click="myTodoListsStore.showEditModal(props.item.id)">Edit</button>
    <button class="btn m-1" :class="deleteButtonThemeClass" @click="myTodoListsStore.showDeleteModal(props.item.id)">Delete</button>
  </div>
</template>

<style scoped>
.no-bullets {
  list-style-type: none;
}
ul.borderless li {
  border: none;

  &.list-group-item {
    padding-left: 0;
    padding-right: 1.5rem;
  }
}
@media (max-width: 992px) {
  ul.borderless li.list-group-item {
    padding-top: 0;
    padding-bottom: 0;
  }
}
</style>
