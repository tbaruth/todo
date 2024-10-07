<script setup lang="ts">
import {RouterView} from 'vue-router'
import {onBeforeMount} from "vue";
import {useUserStore} from "@/stores/user";
import TheFooter from "@/components/TheFooter.vue";
import TheHeader from "@/components/TheHeader.vue";
import {setTheme} from "@/composables/theme-manager";
import EditTodoListModal from "@/todo-lists/components/EditTodoListModal.vue";

const userStore = useUserStore();

onBeforeMount(async () => {
  await userStore.loadUser();
  setTheme(userStore.user!.darkMode);
});
</script>

<template>
  <TheHeader/>
  <EditTodoListModal />
  <Suspense>
    <RouterView/>
    <template #fallback>
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </template>
  </Suspense>
  <TheFooter/>
</template>

<style scoped>

</style>
