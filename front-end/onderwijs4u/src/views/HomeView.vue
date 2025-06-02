<template>
  <div class="container">
    <button @click="enterAsStudent" class="left-button">Studentenportaal</button>
    <div class="image-container">
      <img src="@/assets/onderwijs4u_center.png" alt="Spinning Image" class="spinning-image" />
    </div>
    <button @click="enterAsTeacher" class="right-button">Leraarportaal</button>
  </div>
</template>

<script>
export default {
  name: 'HomePage',
  created() {
    // Clear session variable when entering the homepage
    sessionStorage.removeItem('role');
    sessionStorage.removeItem('navcolor');
  },
  methods: {
    enterAsStudent() {
      sessionStorage.setItem('role', 'student');
      this.$router.push('/rubrics').then(() => {
        // window.location.reload();
      });
    },
    enterAsTeacher() {
      sessionStorage.setItem('role', 'teacher');
      this.$router.push('/roles').then(() => {
        // window.location.reload();
      });
    }
  }
};
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
  animation: spin 5s linear infinite;
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
