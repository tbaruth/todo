<script setup lang="ts">
import {computed} from "vue";
import {useUserStore} from "@/stores/user";

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

const formatDate = (dateString: string): string => {
  const date = new Date(dateString);
  return date.toLocaleDateString('UTC', {year: 'numeric', month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric'});
}
</script>

<template>
  <div>
    <div class="fw-bold fs-3 mb-1">{{ props.item.title }}</div>
    <ul class="list-group list-group-horizontal-lg borderless">
      <li class="list-group-item pe-4"><strong>Completed: </strong> {{props.item.numCompleted}}/{{props.item.numItems}}</li>
      <li class="list-group-item"><strong>Created: </strong> {{ createdDate }}</li>
      <li class="list-group-item"><strong>Last Updated: </strong> {{ updatedDate }}</li>
    </ul>
  </div>
  <div class="buttons">
    <button class="btn btn-primary m-1">Edit</button>
    <button class="btn btn-danger m-1">Delete</button>
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
    padding-right: 1.5rem
  }
}
</style>
