<template>
  <div class="container d-flex justify-content-between align-items-center vh-100">
    <button @click="enterAsLoopbaancoach" class="left-button">Enter App as Loopbaancoach</button>
    <button @click="enterAsProductowner" class="right-button">Enter App as Productowner</button>
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
      this.$router.push('/coach').then(() => {
        window.location.reload();
      });
    },
    enterAsProductowner() {
      sessionStorage.setItem('teacherrole', 'productowner');
      this.$router.push('/teacherprojects').then(() => {
        window.location.reload();
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
</style>
