<template>
  <div class="container mt-5">
    <h1 class="mb-4">Create a New Project</h1>

    <!-- Project Name Input -->
    <div class="mb-4">
      <label for="projectName" class="form-label h5">Project Name</label>
      <input id="projectName" v-model="newProject.name" type="text" class="form-control"
        placeholder="Enter project name" />
    </div>

    <!-- Course Selection -->
    <div class="mb-4">
      <label for="courseSelection" class="form-label h5">Jaar: </label>
      <select id="courseSelection" v-model="selectedCourse" class="form-select">
        <option v-for="course in courses" :key="course.id" :value="course">
          {{ course.description }}
        </option>
      </select>
    </div>

    <!-- Criteria Selection -->
    <div v-if="selectedCourse" class="mb-4">
      <h2 class="h5">Course: </h2>
      <div class="list-group">
        <div v-for="criterion in selectedCourse.criteria" :key="criterion.id" class="list-group-item">
          <div class="form-check">
            <input class="form-check-input" type="checkbox" :id="'criterion-' + criterion.project" :value="criterion"
              v-model="selectedCriteria" />
            <label class="form-check-label" :for="'criterion-' + criterion.project">
              {{ criterion.beoordelingscriteria }}
            </label>
          </div>
        </div>
      </div>
    </div>

    <!-- Selected Criteria Details -->
    <div v-if="selectedCriteria.length > 0" class="mb-4">
      <h2 class="h5">Selected Criteria Details</h2>
      <table class="table table-bordered">
        <thead>
          <tr>
            <th>Beoordelingscriteria</th>
            <th>Subcriteria</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="(criterion, index) in selectedCriteria" :key="criterion.project">
            <!-- First row with rowspan -->
            <tr v-if="criterion.criteria && criterion.criteria.length > 0">
              <td :rowspan="criterion.criteria.length">
                {{ criterion.beoordelingscriteria }}
              </td>
              <td style="white-space: pre-wrap;">{{ criterion.criteria[0].verantwoording }}</td>
            </tr>
            <!-- Additional rows for remaining subcriteria -->
            <tr v-for="(subCriterion, subIndex) in criterion.criteria.slice(1)" :key="subIndex">
              <td >{{ subCriterion.verantwoording }}</td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>

    <!-- Save Button -->
    <button @click="saveProject" class="btn btn-primary">Save Project</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { student_projects, school_criteria } from '@/dummydata/dummydata.js';

const newProject = ref({
  name: '',
  running: true,
  criteriaToReach: {},
});

const courses = school_criteria.value[0].werkproces; // Assuming "Jaar 1" is the first year
const selectedCourse = ref(null);
const selectedCriteria = ref([]);

const saveProject = () => {
  if (!newProject.value.name) {
    alert('Please enter a project name.');
    return;
  }

  if (selectedCriteria.value.length === 0) {
    alert('Please select at least one criterion.');
    return;
  }

  // Map selected criteria to the required format
  const criteriaToReach = {};
  selectedCriteria.value.forEach((criterion) => {
    criteriaToReach[criterion.project] = {
      studentVerantwoording: '',
      feedback: '',
      grade: '',
    };
  });

  // Save the new project
  student_projects.value[newProject.value.name] = {
    id: Object.keys(student_projects.value).length + 1,
    running: newProject.value.running,
    description: 'Newly created project',
    criteriaToReach,
  };

  alert('Project saved successfully!');
  // Reset form
  newProject.value.name = '';
  newProject.value.running = true;
  selectedCourse.value = null;
  selectedCriteria.value = [];
};
</script>

<style scoped>
/* Add any custom styles here if needed */
</style>