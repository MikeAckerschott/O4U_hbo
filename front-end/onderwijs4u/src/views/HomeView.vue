<template>
  <div class="container">

    <router-link to="/game">
    <button class="left-button">Play game</button>
    </router-link>
    <div class="image-container">
      <img src="@/assets/onderwijs4u.png" alt="Spinning Image" class="spinning-image" />
    </div>
  </div>
</template>

<script>
</script>

<style scoped>
.container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100vh;
}

.image-container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-grow: 1;
}

.spinning-image {
  border-radius: 50%;
  animation: spin 15s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
