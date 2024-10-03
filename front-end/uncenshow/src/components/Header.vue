<script setup lang="ts">
import { RouterLink } from 'vue-router';
import store from '@/store';
</script>

<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light shadow">
    <div class="container-fluid">
      <a class="navbar-brand text-white" href="#" style="font-weight: bold;">Education4U</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNavDropdown">
        <ul class="navbar-nav me-auto">
          <li class="nav-item">
            <router-link class="nav-link" to="/" v-if="store && store.getters.isAuthenticated">Home</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/signup" v-if="!store || !store.getters.isAuthenticated">Signup</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/login" v-if="!store || !store.getters.isAuthenticated">Login</router-link>
          </li>
          <li class="nav-item" v-if="store && store.getters.isAuthenticated">
            <a class="nav-link text-orange" @click.prevent="store.dispatch('logout')">Logout</a>
          </li>
        </ul>

        <!-- User Profile Dropdown with External Image -->
        <ul class="navbar-nav ms-auto">
          <li class="nav-item dropdown" >
            <a class="nav-link dropdown-toggle" href="#" id="profileDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              <img src="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fyt3.ggpht.com%2Fa%2FAGF-l7_Fk9PfNwgM55wZ3IP-y31KqrEavzZ9DoOHqA%3Ds900-c-k-c0xffffffff-no-rj-mo&f=1&nofb=1&ipt=67faa5336aec87e631f7cd4a575dc9af9230ae29b5b72d630efb81ef44e4c0b9&ipo=images"
                   class="profileIcon rounded-circle"
                   alt="User Profile" />
              <span class="username ms-2">Username</span>
            </a>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="profileDropdown">
              <li><router-link class="dropdown-item" to="/profile">Profile</router-link></li>
              <li><router-link class="dropdown-item" to="/settings">Settings</router-link></li>
              <li><a class="dropdown-item" @click.prevent="store.dispatch('logout')">Logout</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<style scoped>
/* Styling for the Navbar */
.navbar {
  background: linear-gradient(to right, #ff9800, #5e35b1) !important;  color: #5e35b1; /* Purple text */
  padding: 0.75rem 1rem;
  font-family: 'Arial', sans-serif;
}
/* Navbar link hover effect */
.nav-link:hover {
  background: linear-gradient(to right, #ff9800, #5e35b1) !important;  color: #5e35b1; /* Purple text */
  transition: background-color 0.3s ease, color 0.3s ease; /* Smooth transition */
}

/* Profile Icon */
.profileIcon {
  width: 35px;
  height: 35px;
  object-fit: cover;
  border: 2px solid #5e35b1; /* Purple border */
}


/* Dropdown menu */
.dropdown-menu {
  background-color: #ff9800; /* Orange dropdown background */
  color: white;
}

.dropdown-item {
  color: #5e35b1; /* Purple text */
}

.dropdown-item:hover {
  background-color: #f57c00; /* Darker orange on hover */
  color: white;
}

/* Make the navbar fixed at the top */
.navbar {
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000;
}

.container-fluid {
  max-width: 1200px;
  margin: 0 auto;
}

body, .container-fluid {
  margin: 0;
  padding: 0;
}
</style>
