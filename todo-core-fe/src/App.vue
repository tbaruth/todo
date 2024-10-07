<script setup lang="ts">
import {RouterView} from 'vue-router'
import {computed, onBeforeMount} from "vue";
import {useUserStore} from "@/stores/user";
import TheFooter from "@/components/TheFooter.vue";
import TheHeader from "@/components/TheHeader.vue";
import {setTheme} from "@/composables/theme-manager";
import EditTodoListModal from "@/todo-lists/components/EditTodoListModal.vue";
import {useBusyStore} from "@/stores/busy";
import ProcessingOverlay from "@/components/ProcessingOverlay.vue";

const userStore = useUserStore();
const busyStore = useBusyStore();

const cursorClass = computed(() => {
  return busyStore.isBlocking ? 'cursor-blocking' : busyStore.isLoading ? 'cursor-working' : '';
});

onBeforeMount(async () => {
  await userStore.loadUser();
  setTheme(userStore.user!.darkMode);
});
</script>

<template>
  <ProcessingOverlay v-if="busyStore.isBlocking" />
  <div :class="cursorClass">
  <TheHeader />
  <EditTodoListModal />
  <Suspense>
    <RouterView />
    <template #fallback>
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </template>
  </Suspense>
  <TheFooter/>
  </div>
</template>

<style scoped>
.cursor-working {
  cursor: progress;
}
.cursor-blocking {
  cursor: wait;
}
</style>
