<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
                  <RouterLink class="nav-link" to="/">Onderwijs4U</RouterLink>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" to="/rubrics">Rubrics</RouterLink>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Projects
            </a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
            <RouterLink class="nav-link" to="/currentproject">Current project</RouterLink>
            <RouterLink class="nav-link" to="/allprojects">All projects</RouterLink>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'Navbar'
}
</script>

<style scoped>
.navbar {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  z-index: 1000; /* Ensures the navbar is above other elements */
}

.nav-link {
  transition: transform 0.5s ease; /* Smooth transition for the transform property */
}

.nav-link:hover {
  transform: scale(1.1); /* Grow the nav item on hover */
  background: transparent;
}

body {
  padding-top: 56px; /* Adjust this value based on the height of your navbar */
}
</style>
