<script setup lang="ts">

</script>

<template>
  <div class="overlay d-flex align-items-center justify-content-center">
    <div class="spinner-border big-spinner" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>
</template>

<style scoped>
.overlay {
  z-index: 1200;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  display: block;
  cursor: not-allowed;
}
.big-spinner {
  width: 8rem;
  height: 8rem;
}
</style>
