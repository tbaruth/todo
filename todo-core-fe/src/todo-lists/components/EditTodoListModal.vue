<script setup lang="ts">
import { Modal } from "bootstrap";
import {useMyTodoListsStore} from "@/todo-lists/stores/myTodoLists";
import {computed, onMounted, onUnmounted, type Ref, ref, watch} from "vue";
import {useUserStore} from "@/stores/user";

const myTodoListsStore = useMyTodoListsStore();
const userStore = useUserStore();

let bsModal: Modal | undefined;
const modal: Ref<null | string | Element> = ref(null);

const modalTitle = computed(() => {
  return myTodoListsStore.form.id ? 'Edit Todo List' : 'Create Todo List';
});

const saveButtonText = computed(() => {
  return myTodoListsStore.form.id ? 'Save Changes' : 'Create List';
});

const cancelButtonThemeClass = computed(() => {
  return userStore.user?.darkMode ? 'btn-outline-secondary' : 'btn-secondary';
});

const saveButtonThemeClass = computed(() => {
  return userStore.user?.darkMode ? 'btn-outline-primary' : 'btn-primary';
});

onMounted(async () => {
  bsModal = new Modal(modal.value as string | Element);
  (modal.value as Element).addEventListener('hidden.bs.modal', myTodoListsStore.hideEditModal);
});

onUnmounted(() => {
  if (modal.value != null) {
    (modal.value as Element).removeEventListener('hidden.bs.modal', myTodoListsStore.hideEditModal);
  }
});

watch(() => myTodoListsStore.editModalShown, (newValue, oldValue) => {
  if (newValue) {
    bsModal?.show();
  } else {
    bsModal?.hide();
  }
});
</script>

<template>
  <div class="modal" tabindex="-1" id="edit-modal" ref="modal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">{{modalTitle}}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" @click="myTodoListsStore.hideEditModal()"></button>
        </div>
        <div class="modal-body">
          <form>
            <div class="mb-3">
              <label for="title" class="form-label">Title:</label>
              <input type="text" class="form-control" id="title" aria-describedby="title-help" v-model="myTodoListsStore.form.title" />
              <div id="title-help" class="form-text">Enter a quick descriptor.</div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn" :class="cancelButtonThemeClass" data-bs-dismiss="modal" @click="myTodoListsStore.hideEditModal()">Cancel</button>
          <button type="button" class="btn" :class="saveButtonThemeClass" @click="myTodoListsStore.saveTodoList()">{{saveButtonText}}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>



