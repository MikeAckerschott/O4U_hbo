<script setup lang="ts">
import { RouterLink, useRoute } from 'vue-router';
import store from '@/store';

const route = useRoute();

// Function that returns the image folder path based on the number of slashes in the route path
const getImgPath = (routePath: string) => {
  const slashes = routePath.split('/').length - 1;
  let path = 'src\\assets\\Images\\standard-profile-icon.jpg';
  for (let i = 0; i < slashes; i++) {
    path = '..\\' + path;
  }
  return path;
};
</script>

<template>
  <header>
    <img
        alt="Profile icon"
        class="profileIcon"
        :src="store.getters.getUser?.image || getImgPath(route.path)"
        v-if="store && store.getters.isAuthenticated"
    />
    <p class="username">{{ store.getters.getUser?.username }}</p>
    <router-link class="router-link" to="/" v-if="store && store.getters.isAuthenticated">Home</router-link>
    <router-link class="router-link" to="/signup" v-if="!store || !store.getters.isAuthenticated">Signup</router-link>
    <router-link class="router-link" to="/login" v-if="!store || !store.getters.isAuthenticated">Login</router-link>
    <a class="router-link" v-if="store && store.getters.isAuthenticated" @click.prevent="store.dispatch('logout')">Logout</a>
  </header>
</template>

<style scoped>
header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 50px;
  background-color: #3f51b5;
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
}
.username {
  position: absolute;
  left: 50px;
}
.router-link {
  color: white;
  font-size: 15px;
  margin-left: 20px;
  float: right;
}
.profileIcon {
  position: absolute;
  left: 10px;
  width: 30px;
  height: 30px;
  border-radius: 50%;
}
</style>
