<script setup lang="ts">
import {RouterLink} from "vue-router";
import {useUserStore} from "@/stores/user";
import {setTheme} from "@/composables/theme-manager";
import {computed} from "vue";
import {useMyTodoListsStore} from "@/todo-lists/stores/myTodoLists";

const userStore = useUserStore();
const myTodoListsStore = useMyTodoListsStore();

//Theme handling is a bit wonky because of the need to values on the html tag--this is therefore handled with a special method
const toggleDarkMode = async () => {
  await userStore.toggleDarkMode(!userStore.user?.darkMode);
  setTheme(userStore.user!.darkMode);
}

const themeButtonClass = computed(() => {
  return userStore.user?.darkMode ? 'btn-outline-success' : 'btn-success';
});
</script>

<template>
  <header class="sticky-top">
    <nav class="navbar navbar-expand-lg mb-3 bg-body-tertiary">
      <div class="container-fluid">
        <a class="navbar-brand" href="/">
          <img alt="Hastily-created chatgpt logo" class="logo" src="@/assets/logo.svg" height="50" width="125" />
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbar-content" aria-controls="navbar-content" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbar-content">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <RouterLink to="/" class="nav-link active" aria-current="page">Home</RouterLink>
            </li>
          </ul>
          <div v-if="userStore.user" class="form-check form-switch dark-mode me-2 mb-3 mb-lg-0">
            <input class="form-check-input" type="checkbox" id="dark-mode-switch" :checked="userStore.user.darkMode" @change="toggleDarkMode()">
            <label class="form-check-label" for="dark-mode-switch">Dark Mode</label>
          </div>
          <div class="d-grid gap-2">
            <button class="btn" :class="themeButtonClass" @click="myTodoListsStore.showEditModal()">Create Todo List</button>
          </div>
        </div>
      </div>
    </nav>
  </header>
</template>

<style scoped>
.form-switch.dark-mode .form-check-input:focus {
  border-color: rgba(0, 0, 0, 0.25);
  outline: 0;
  box-shadow: 0 0 0 .25rem rgba(255, 165, 0, .25);
  background-image: url("data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='-4 -4 8 8'%3e%3ccircle r='3' fill='rgba(0,0,0,0.25)'/></svg>");
}
.form-switch.dark-mode .form-check-input:checked {
  background-color: #FFA500;
  border-color: #FFA500;
  border: none;
  background-image: url("data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='-4 -4 8 8'%3e%3ccircle r='3' fill='rgba(33,37,41,1.0)'/></svg>");
}
</style>
