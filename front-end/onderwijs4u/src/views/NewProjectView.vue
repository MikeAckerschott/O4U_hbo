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
      <select id="courseSelection" v-model="selectedYear" class="form-select">
        <option v-for="course in courses" :key="course.id" :value="course">
          {{ course.description }}
        </option>
      </select>
    </div>

    <!-- Criteria Selection and Selected Courses Box -->
    <div v-if="selectedYear" class="mb-4 d-flex align-items-start">
      <!-- Criteria Selection -->
      <div class="flex-grow-1 me-3">
        <h2 class="h5">Course: </h2>
        <div class="list-group">
          <div v-for="course in selectedYear.courses" :key="course.id" class="list-group-item">
            <div class="form-check">
              <input class="form-check-input" type="checkbox" :id="'course-' + course.id" :value="course"
                v-model="selectedCourses" />
              <label class="form-check-label" :for="'course-' + course.id">
                {{ course.beoordelingscriteria }}
              </label>
            </div>
            <!-- Criteria Selection for the Course -->
            <div v-if="selectedCourses.includes(course)" class="mt-2">
              <table class="table table-bordered">
                <thead>
                  <tr>
                    <th>Criteria</th>
                    <th>Criteriabeschrijving</th>
                    <th>Selecteer</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(subCriterion, subIndex) in course.criteria" :key="subIndex">
                    <td style="white-space: pre-wrap;">{{ subCriterion.name }}</td>
                    <td style="white-space: pre-wrap;">{{ subCriterion.verantwoording || 'N/A' }}</td>
                    <td>
                      <input class="form-check-input" type="checkbox" :id="'subCriterion-' + subCriterion.id"
                        :value="subCriterion" v-model="selectedCriteria" />
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <!-- Selected Courses Box -->
      <div class="selection-holders">
        <!-- <div v-if="selectedCourses.length > 0" class="selected-courses-box mb-4">
          <h2 class="h5">Selected Courses</h2>
          <ul class="list-group">
            <li v-for="(course, index) in selectedCourses" :key="index"
              class="list-group-item d-flex justify-content-between align-items-center">
              <span class="me-3">{{ course.beoordelingscriteria }}</span>
              <button @click="removeCourse(index)" class="btn btn-sm btn-danger">Remove</button>
            </li>
          </ul>
        </div> -->

        <!-- Selected Criteria Box -->
        <div v-if="selectedCriteria.length > 0" class="selected-courses-box">
          <h2 class="h5">Selected Criteria</h2>
          <ul class="list-group">
            <li v-for="(criterion, index) in selectedCriteria" :key="index"
              class="list-group-item d-flex justify-content-between align-items-center">
              <span class="me-3" @click="openOverlay(criterion)" style="cursor: pointer;">
                {{ criterion.name }}
              </span>

              <!-- Overlay -->
              <div v-if="overlayVisible" class="overlay" @click="closeOverlay">
                <div class="overlay-content" @click.stop>
                  <h3 class="h5">{{ overlayContent.name }}</h3>
                  <p>{{ overlayContent.verantwoording }}</p>
                  <button @click="closeOverlay" class="btn btn-secondary mt-3">Close</button>
                </div>
              </div>
              <button @click="removeCriteria(index)" class="btn btn-sm btn-danger">Remove</button>
            </li>
          </ul>
        </div>
      </div>
    </div>




    <!-- Save Button -->
    <button @click="saveProject" class="btn btn-primary">Save Project</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { student_projects, school_criteria } from '@/dummydata/dummydata.js';

import { useRouter } from 'vue-router';
const router = useRouter();

const newProject = ref({
  name: '',
  running: true,
  criteriaToReach: {},
});

const courses = school_criteria.value[0].years; // Assuming "Jaar 1" is the first year
const selectedYear = ref(courses[0]); // Default to the first year
const selectedCourses = ref([]);
const selectedCriteria = ref([]);

const removeCourse = (index) => {
  selectedCourses.value.splice(index, 1);
};

const removeCriteria = (index) => {
  selectedCriteria.value.splice(index, 1);
};

const saveProject = () => {
  if (!newProject.value.name) {
    alert('Please enter a project name.');
    return;
  }

  if (selectedCriteria.value.length === 0) {
    alert('Minimaal 1 criteria moet geselecteerd worden.');
    return;
  }

  // Map selected criteria to the required format
  const criteriaToReach = {};
  selectedCourses.value.forEach((criterion) => {
    criteriaToReach[criterion.project] = {
      studentVerantwoording: '',
      feedback: '',
      grade: '',
    };
  });

  // Computed selected criteria. Criteria should be key-value pairs. key is criteria name and value is an object with studentVerantwoording, feedback, and grade.
  selectedCriteria.value.forEach((criterion) => {
    criteriaToReach[criterion.name] = {
      studentVerantwoording: 'Vul hier je bewijslast in.',
      feedback: 'Leraar feedback',
      grade: 'Onvoldoende',
      awaitingTeacher: true,
    };
  });

  delete criteriaToReach[undefined]
  console.log(selectedCriteria.value)
  // remove value of key undefined from criteriaToReach
  selectedCriteria.value = criteriaToReach;

  // Save the new project
  student_projects.value[newProject.value.name] = {
    id: Object.keys(student_projects.value).length + 1,
    running: false,
    description: 'Newly created project',
    criteriaToReach: selectedCriteria.value,
    awaitingTeacher: true,
  };

  router.push(`/projects/`);

  // Reset form
  newProject.value.name = '';
  newProject.value.running = true;
  selectedYear.value = null;
  selectedCourses.value = [];
  selectedCriteria.value = [];

  



};

const overlayVisible = ref(false);
const overlayContent = ref('');
const openOverlay = (content) => {
  overlayContent.value = content;
  overlayVisible.value = true;
};
const closeOverlay = () => {
  overlayVisible.value = false;
  overlayContent.value = '';
};


</script>

<style scoped>
/* Add any custom styles here if needed */
.selected-courses-box {
  padding: 10px;
  border-radius: 10px;
  background-color: #f7f7f7;
  margin-left: 20px;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.overlay-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  max-width: 600px;
  width: 100%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  white-space: pre-wrap;
}
</style>