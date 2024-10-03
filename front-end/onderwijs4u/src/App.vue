<template>
  <div class="root-container">
    <header>
  <Header></Header>
    </header>
    <main>
         <RouterView />
      <div class="blob"></div>
<div class="blob"></div>
<div class="blob"></div>

    </main>
    <footer>
      <p>© 2024 My Website</p>
    </footer>
  </div>
</template>

<script setup lang="ts">
import Header from "@/components/Header.vue";
</script>

<style scoped>
.root-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

header, footer {
  background-color: #f8f9fa;
  padding: 1rem;
  text-align: center;
}

main {
  flex: 1;
  padding: 1rem;
}
</style>
