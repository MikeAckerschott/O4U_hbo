<template>
  <div class="container mt-4">
    <h2 class="mb-4">{{ project }} Dashboard</h2>

    <!-- Students Row -->
    <div class="row mb-4">
      <h3 class="mb-3">Project deelnemers</h3>
      <div v-for="student in students" :key="student.id" class="col-md-2 col-sm-4 col-6 mb-3 text-center">
        <div class="d-flex flex-column align-items-center">
          <div class="rounded-circle bg-primary d-flex justify-content-center align-items-center" style="width: 100px; height: 100px;">
            <RouterLink class="nav-link" :to="'/student/' + project + '/' +  student.name">
              <img src="@/assets/user.jpg" alt="Onderwijs4U" class="navbar-logo" style="width: 100px; height: 100px; border-radius: 50%;" />
            </RouterLink>
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
          <div
            class="progress-bar"
            role="progressbar"
            :style="{ width: `${progressPercentage}%` }"
            :aria-valuenow="progressPercentage"
            aria-valuemin="0"
            aria-valuemax="100"
          >
            {{ progressPercentage }}%
          </div>
        </div>
        <div class="d-flex justify-content-between mt-2">
          <span>Start Date: {{ formatDate(projectStartDate) }}</span>
          <span>End Date: {{ formatDate(projectEndDate) }}</span>
        </div>
        <p class="text-center mt-3">
          <strong>{{ daysRemaining }} days remaining</strong>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import {useRoute} from 'vue-router'

const route = useRoute()

const project = route.params.project

// Mock data for students
const students = ref([
  { id: 1, name: 'Jan' },
  { id: 2, name: 'Sanne' },
  { id: 3, name: 'Pieter' },
  { id: 4, name: 'Lisa' },
  { id: 5, name: 'Tom' },
  { id: 6, name: 'Emma' },
]);

// Mock data for project timeline
const projectStartDate = ref(new Date('2024-03-01'));
const projectEndDate = ref(new Date('2024-11-30'));

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

// Format date function
const formatDate = (date) => {
  return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
};
</script>

<style scoped>
/* Add any additional custom styles here */
</style>
