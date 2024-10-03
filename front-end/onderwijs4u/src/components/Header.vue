<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <a class="navbar-brand" href="#">Navbar</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav">
          <li class="nav-item active">
            <RouterLink to="/">Go to Home</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink to="/project">Go to project</RouterLink>
          </li>
        </ul>
      </div>
    </nav>
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'Navbar'
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000; /* Ensures the navbar is above other elements */
}

body {
  padding-top: 56px; /* Adjust this value based on the height of your navbar */
}
</style>
