<script setup lang="ts">
import { Modal } from "bootstrap";
import {useMyTodoListsStore} from "@/todo-lists/stores/myTodoLists";
import {computed, onMounted, onUnmounted, type Ref, ref, watch} from "vue";
import {useUserStore} from "@/stores/user";

const myTodoListsStore = useMyTodoListsStore();
const userStore = useUserStore();

let bsModal: Modal | undefined;
const modal: Ref<null | string | Element> = ref(null);

const cancelButtonThemeClass = computed(() => {
  return userStore.user?.darkMode ? 'btn-outline-secondary' : 'btn-secondary';
});

const deleteButtonThemeClass = computed(() => {
  return userStore.user?.darkMode ? 'btn-outline-danger' : 'btn-danger';
});

onMounted(async () => {
  bsModal = new Modal(modal.value as string | Element);
  (modal.value as Element).addEventListener('hidden.bs.modal', myTodoListsStore.hideDeleteModal);
});

onUnmounted(() => {
  if (modal.value != null) {
    (modal.value as Element).removeEventListener('hidden.bs.modal', myTodoListsStore.hideDeleteModal);
  }
});

watch(() => myTodoListsStore.deleteModalShown, (newValue, oldValue) => {
  if (newValue) {
    bsModal?.show();
  } else {
    bsModal?.hide();
  }
});
</script>

<template>
  <div class="modal" tabindex="-1" id="delete-modal" ref="modal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Delete This List?</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" @click="myTodoListsStore.hideDeleteModal()"></button>
        </div>
        <div class="modal-body">
          <p>Are you sure you want to delete this list?</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn" :class="cancelButtonThemeClass" data-bs-dismiss="modal" @click="myTodoListsStore.hideDeleteModal()">Cancel</button>
          <button type="button" class="btn" :class="deleteButtonThemeClass" @click="myTodoListsStore.deleteTodoList()">Delete</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>



