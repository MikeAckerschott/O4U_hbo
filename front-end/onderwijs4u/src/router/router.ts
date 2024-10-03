import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/HomeView.vue';
import Project from '../views/ProjectView.vue';
import Rubrics from '../views/RubricsView.vue';
import RubricDetail from '../views/RubricView.vue';

const routes = [
  { path: '/', component: Home },
  { path: '/project', component: Project },
  { path: '/rubrics', component: Rubrics },
  { path: '/rubric/:rubric', component: RubricDetail}, // Add dynamic route

];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
