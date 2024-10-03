import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import SignupViewVue from '@/views/SignupView.vue'
import LoginViewVue from '@/views/LoginView.vue'
import store from '@/store';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/signup',
      name: 'signup',
      component: SignupViewVue
    },
    {
      path: '/login',
      name: 'login',
      component: LoginViewVue
    },
  ]
})

router.beforeEach((to, from, next) => {
  store.dispatch('clearErrorMessage');
  const isAuthenticated = store.getters.isAuthenticated;
  const redirectUrl = localStorage.getItem('redirectUrl');

  if (!isAuthenticated && to.path !== '/signup' && to.path !== '/login') {
    localStorage.setItem('redirectUrl', to.fullPath);
    next('/signup');
  } else if (isAuthenticated && redirectUrl) {
    localStorage.removeItem('redirectUrl');
    next(redirectUrl);
  } else {
    next();
  }
});

export default router
