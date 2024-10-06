<script setup lang="ts">
const props = defineProps<{
  item: TodoList
}>()
</script>

<template>
  <h3>{{ props.item.title }}</h3>
  <ul class="no-bullets">
    <li><strong>Completed: </strong> {{props.item.numCompleted}}/{{props.item.numItems}}</li>
    <li><strong>Created: </strong> {{ props.item.created }}</li>
    <li><strong>Last Updated: </strong> {{ props.item.updated }}</li>
  </ul>
</template>

<style scoped>
.no-bullets {
  list-style-type: none;
}
</style>
