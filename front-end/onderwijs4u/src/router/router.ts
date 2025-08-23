import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/HomeView.vue';
import Projects from '../views/ProjectsView.vue';
import Project from '../views/ProjectView.vue';
import Rubrics from '../views/RubricsView.vue';
import TeacherProjects from '../views/TeacherProjectsView.vue';
import Student from '../views/StudentView.vue';
import TeacherProject from '../views/TeacherProjectView.vue';
import Roles from '../views/RolesView.vue';
import Game from '../views/GameView.vue';
import Coach from '../views/CoachView.vue';
import RubricDetail from '../views/RubricView.vue';
import Graduation from '../views/GraduationView.vue';
import NewProject from '../views/NewProjectView.vue';

const routes = [
  { path: '/', component: Home },
  { path: '/projects', component: Projects },
  { path: '/graduation', component: Graduation },
  { path: '/teacherprojects', component: TeacherProjects },
  { path: '/teacherproject/:project', component: TeacherProject },
  { path: '/student/:project/:student', component: Student },
  { path: '/rubrics', component: Rubrics },
  { path: '/game', component: Game },
  { path: '/roles', component: Roles },
  { path: '/coach', component: Coach},
  { path: '/rubric/:rubric', component: RubricDetail}, // Add dynamic route
  { path: '/project/:project', component: Project}, // Add dynamic route
  { path: '/newproject', component: NewProject}, // Add dynamic route

];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
