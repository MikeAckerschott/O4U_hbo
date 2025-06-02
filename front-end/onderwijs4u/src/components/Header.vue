<template>
  <nav :class="['navbar', 'navbar-expand-lg']" :style="{ backgroundColor: navbarColorClass }">
    <div class="container-fluid">
      <RouterLink class="nav-link" @click.prevent="resetHeaderBar" to="/"> <!-- reset header bar  -->
        <img src="@/assets/onderwijs4u.png" alt="Onderwijs4U" class="navbar-logo" />
      </RouterLink>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item" v-if="role === 'student'">
            <RouterLink class="nav-link" to="/rubrics">Courses</RouterLink>
          </li>

          <li class="nav-item dropdown" v-if="role === 'student'">
            <a class="nav-link dropdown-toggle" href="#" id="notificationDropdown" role="button"
              data-bs-toggle="dropdown" aria-expanded="false">
              Projecten
            </a>
            <ul class="dropdown-menu" aria-labelledby="notificationDropdown">
              <li>
                <RouterLink class="dropdown-item" to="/projects">Jouw Projecten</RouterLink>
              </li>
              <li>
                <RouterLink class="dropdown-item" to="/newproject">Nieuw Project</RouterLink>
              </li>
            </ul>
          </li>
          <li class="nav-item" v-if="role === 'student'">
            <RouterLink class="nav-link" to="/game">Rewards</RouterLink>
          </li>
          <li class="nav-item" v-if="role === 'teacher'">
            <RouterLink class="nav-link" to="/roles">Portal</RouterLink>
          </li>
          <li class="nav-item" v-if="role === 'teacher' && teacherrole === 'productowner'">
            <RouterLink class="nav-link" to="/teacherprojects">Projecten</RouterLink>
          </li>
          <li class="nav-item" v-if="role === 'teacher' && teacherrole === 'loopbaancoach'">
            <RouterLink class="nav-link" to="/coach">Studenten</RouterLink>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'Navbar',
  data() {
    return {
      role: null,
      navcolor: null,
      teacherrole: null
    }
  },
  created() {
    this.getRoleFromSession();
    this.getNavColorFromSession();
  },
  watch: {
    $route() {
      this.getRoleFromSession();
      this.getNavColorFromSession();
    }
  },
  methods: {
    getRoleFromSession() {
      this.role = sessionStorage.getItem('role') || 'guest';
      this.teacherrole = sessionStorage.getItem('teacherrole') || 'loopbaancoach';
    },
    getNavColorFromSession() {
      this.navcolor = sessionStorage.getItem('navcolor') || '#f8f9fa';
    },
    reloadPage() {
      this.$router.push('/').then(() => {
        window.location.reload();
      });
    },
    resetHeaderBar() {
      sessionStorage.setItem('role', 'guest');
      sessionStorage.setItem('teacherrole', 'loopbaancoach');
      sessionStorage.setItem('navcolor', '#f8f9fa');
      this.getRoleFromSession();
      this.getNavColorFromSession();
      this.$router.push('/');
    }
  },
  computed: {
    navbarColorClass() {
      return this.navcolor || '#f8f9fa';
    }
  }
}
</script>

<style scoped>
.navbar {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  z-index: 1000;
  /* Ensures the navbar is above other elements */
}

.navbar-logo {
  height: 56px;
  /* Adjust this value based on the height of your navbar */
  width: 56px;
  /* Make it a square */
  border-radius: 50%;
  /* Make it circular */
}

body {
  padding-top: 56px;
  /* Adjust this value based on the height of your navbar */
}

.nav-link {
  position: relative;
  display: inline-block;
}

.badge {
  position: absolute;
  top: 0;
  right: 0;
  border-radius: 50%;
  background-color: red;
  color: white;
}

.fa-bell {
  padding-top: 8px;
}
</style>
