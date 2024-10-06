<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import {computed, onBeforeMount, onMounted} from "vue";
import {useUserStore} from "@/stores/user";

const userStore = useUserStore();

const navbarClass = computed(() => {
  if (userStore.user?.darkMode) {
    return 'navbar-dark bg-dark';
  } else {
    return 'navbar-light logo-color';
  }
});

//Theme handling is a bit wonky because of the need to values on the html tag--this is therefore handled with a special method
const toggleDarkMode = async () => {
  await useUserStore().toggleDarkMode(!userStore.user?.darkMode);
  setTheme(userStore.user!.darkMode);
}

const setTheme = (useDark: boolean) => {
  if (useDark) {
    document.documentElement.setAttribute('data-bs-theme', 'dark');
  } else {
    document.documentElement.setAttribute('data-bs-theme', 'light');
  }
}

onBeforeMount(async () => {
  await userStore.loadUser();
  setTheme(userStore.user!.darkMode);
});
</script>

<template>
  <header>
    <div class="wrapper">
      <nav class="navbar navbar-expand-lg mb-3 sticky-top" :class="navbarClass">
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
  <!--            <li class="nav-item">-->
  <!--              <RouterLink to="/" class="nav-link active" aria-current="page">Analytics</RouterLink>-->
  <!--            </li>-->
            </ul>
            <div v-if="userStore.user" class="form-check form-switch dark-mode">
              <input class="form-check-input" type="checkbox" id="dark-mode-switch" :checked="userStore.user.darkMode" @change="toggleDarkMode()">
              <label class="form-check-label" for="dark-mode-switch">Dark Mode</label>
            </div>
          </div>
        </div>
      </nav>
    </div>
  </header>
  <Suspense>
    <RouterView />
    <template #fallback>
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </template>
  </Suspense>
  <div class="container-fluid">
    <footer class="d-flex flex-wrap justify-content-between align-items-center py-3 my-4 border-top">
      <p class="col-md-4 mb-0 text-body-secondary">© 2024 Tristan Berg-Baruth</p>

      <a href="/" class="col-md-4 d-flex align-items-center justify-content-center mb-3 mb-md-0 me-md-auto link-body-emphasis text-decoration-none">
        <svg class="bi me-2" width="40" height="32"><use xlink:href="#bootstrap"></use></svg>
      </a>

      <ul class="nav col-md-4 justify-content-end">
        <li class="nav-item"><a href="#" class="nav-link px-2 text-body-secondary">Home</a></li>
        <li class="nav-item"><a href="https://github.com/tbaruth/todo" class="nav-link px-2 text-body-secondary" target="_blank">Github</a></li>
      </ul>
    </footer>
  </div>
</template>

<style scoped>
.logo-color {
  background-color: #e8f5e9;
}
input[type=checkbox]{
  accent-color: green;
}
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
