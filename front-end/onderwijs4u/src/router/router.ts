import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/HomeView.vue';
import Project from '../views/ProjectView.vue';

const routes = [
  { path: '/', component: Home },
  { path: '/project', component: Project },
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
