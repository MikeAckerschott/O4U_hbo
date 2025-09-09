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
  { path: '/O4U_hbo/', component: Home },
  { path: '/O4U_hbo/projects', component: Projects },
  { path: '/O4U_hbo/graduation', component: Graduation },
  { path: '/O4U_hbo/teacherprojects', component: TeacherProjects },
  { path: '/O4U_hbo/teacherproject/:project', component: TeacherProject },
  { path: '/O4U_hbo/student/:project/:student', component: Student },
  { path: '/O4U_hbo/rubrics', component: Rubrics },
  { path: '/O4U_hbo/game', component: Game },
  { path: '/O4U_hbo/roles', component: Roles },
  { path: '/O4U_hbo/coach', component: Coach},
  { path: '/O4U_hbo/rubric/:rubric', component: RubricDetail}, // Add dynamic route
  { path: '/O4U_hbo/project/:project', component: Project}, // Add dynamic route
  { path: '/O4U_hbo/newproject', component: NewProject}, // Add dynamic route

];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
