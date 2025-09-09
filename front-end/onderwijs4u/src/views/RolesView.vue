<template>
  <div class="container d-flex justify-content-between align-items-center vh-100">
    <button @click="enterAsLoopbaancoach" class="left-button">SLB-portaal</button>
        <div class="image-container">
      <img src="@/assets/onderwijs4u_center.png" alt="Spinning Image" class="spinning-image" />
    </div>
    <button @click="enterAsProductowner" class="right-button">Opdrachtgeversportaal</button>
  </div>
</template>

<script>
export default {
  name: 'HomePage',
  created() {
    // Clear session variable when entering the homepage
    sessionStorage.removeItem('teacherrole');
  },
  methods: {
    enterAsLoopbaancoach() {
      sessionStorage.setItem('teacherrole', 'loopbaancoach');
      this.$router.push('/O4U_hbo/coach').then(() => {
        // window.location.reload();
      });
    },
    enterAsProductowner() {
      sessionStorage.setItem('teacherrole', 'productowner');
      this.$router.push('/O4U_hbo/teacherprojects').then(() => {
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

.btn {
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}

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

