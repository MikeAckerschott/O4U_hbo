<template>
  <div class="container mt-4">
    <h2 class="mb-4">{{ project }} Dashboard</h2>

    <!-- Students Row -->
    <div class="row mb-4">
      <h3 class="mb-3">Project deelnemers</h3>
      <div v-for="student in students" :key="student.id" class="col-md-2 col-sm-4 col-6 mb-3 text-center">
        <div class="d-flex flex-column align-items-center">
          <div class="rounded-circle bg-primary d-flex justify-content-center align-items-center"
            style="width: 100px; height: 100px;">
            <template v-if="!awaitingTeacher">
              <RouterLink class="nav-link" :to="'/O4U_hbo/student/' + project + '/' + student.name">
                <img src="@/assets/user.jpg" alt="Onderwijs4U" class="navbar-logo"
                  style="width: 100px; height: 100px; border-radius: 50%;" />
              </RouterLink>
            </template>
            <template v-else>
              <img src="@/assets/user.jpg" alt="Onderwijs4U" class="navbar-logo"
                style="width: 100px; height: 100px; border-radius: 50%;" />
            </template>
          </div>
          <span class="mt-2">{{ student.name }}</span>
        </div>
      </div>
    </div>

    <!-- Project Timeline -->
    <div class="row">
      <h3 class="mb-3">Project Timeline</h3>
      <div class="col-12">
        <div class="progress" style="height: 30px;">
          <div class="progress-bar" role="progressbar" :style="{ width: `${progressPercentage}%` }"
            :aria-valuenow="progressPercentage" aria-valuemin="0" aria-valuemax="100">
            {{ progressPercentage }}%
          </div>
        </div>
        <div class="d-flex justify-content-between mt-2">
          <span>Startdatum: {{ formatDate(projectStartDate) }}</span>
          <span>Einddatum: {{ formatDate(projectEndDate) }}</span>
        </div>
        <p class="text-center mt-3">
          <strong>{{ daysRemaining }} dagen over</strong>
        </p>
      </div>
    </div>

    <!-- Teacher Project Review (only if awaitingTeacher is true) -->
    <div v-if="awaitingTeacher" class="mb-5">
      <h1 class="mb-4">Project Review: {{ project }}</h1>
      <div class="card mb-4">
        <div class="card-header bg-light">
          <h2 class="h5 mb-0">Projectbeschrijving</h2>
        </div>
        <div class="card-body">
          <p>{{ projectDescription }}</p>
        </div>
      </div>
      <div class="card mb-4">
        <div class="card-header bg-light">
          <h2 class="h5 mb-0">Te behalen criteria</h2>
        </div>
        <div class="card-body">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th>Naam</th>
                <th>Omschrijving</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(crit, name) in criteriaToReach" :key="name">
                <td>{{ name }}</td>
                <td>{{ mappedCriteria[name] }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <button class="btn btn-success me-2" @click="allowProject">Project goedkeuren</button>
      <button class="btn btn-warning" @click="requestInfo">Extra informatie vereist</button>
    </div>


  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { student_projects, mappedCriteria } from '@/dummydata/dummydata.js';

const route = useRoute();
const project = route.params.project;

// Mock data for students
const students = ref([
  { id: 1, name: 'Jan' },
]);

// Mock data for project timeline
const projectStartDate = ref(new Date('2025-06-12'));
const projectEndDate = ref(new Date('2025-06-30'));

// Calculate days remaining
const daysRemaining = computed(() => {
  const today = new Date();
  const timeDiff = projectEndDate.value.getTime() - today.getTime();
  return Math.ceil(timeDiff / (1000 * 3600 * 24));
});

// Calculate progress percentage
const progressPercentage = computed(() => {
  const totalDuration = projectEndDate.value.getTime() - projectStartDate.value.getTime();
  const elapsedDuration = new Date().getTime() - projectStartDate.value.getTime();
  const percentage = (elapsedDuration / totalDuration) * 100;
  return Math.min(Math.max(percentage, 0), 100).toFixed(1);
});

// Datum formatteren naar Nederlands
const formatDate = (date) => {
  return date.toLocaleDateString('nl-NL', { year: 'numeric', month: 'short', day: 'numeric' });
};

// --- Teacher Project Review logic ---
const awaitingTeacher = ref(false);
const projectDescription = ref('');
const criteriaToReach = ref({});

onMounted(() => {
  const projectData = student_projects.value[project];
  if (projectData) {
    awaitingTeacher.value = !!projectData.awaitingTeacher;
    projectDescription.value = projectData.description || '';
    criteriaToReach.value = projectData.criteriaToReach || {};
    // if awaitingTeacher is true, projectStartDate is today
    if (awaitingTeacher.value) {
      projectStartDate.value = new Date();
    }
    if(!projectData.running && !awaitingTeacher.value) {
      projectEndDate.value = new Date();
    }
  } else {
    awaitingTeacher.value = false;
    projectDescription.value = '';
    criteriaToReach.value = {};
  }
});




const allowProject = () => {
  const projectData = student_projects.value[project];
  if (projectData) {
    projectData.awaitingTeacher = false;
    awaitingTeacher.value = false;
    projectData.running = true;
  }
  alert('Project is nu draaiende.');
};

const requestInfo = () => {
  alert('Extra informatie is opgevraagd bij de student.');
};
</script>