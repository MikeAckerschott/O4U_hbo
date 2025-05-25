<template>
  <div class="container mt-5">
    <h1 class="mb-4">Vraag een nieuw project aan</h1>

    <!-- Project Name Input -->
    <div class="mb-4">
      <label for="projectName" class="form-label h5">Projectnaam</label>
      <input id="projectName" v-model="newProject.name" type="text" class="form-control"
        placeholder="" />
    </div>

    <!-- Project Description Input -->
    <div class="mb-4">
      <label for="projectDescription" class="form-label h5">Projectbeschrijving</label>
      <textarea id="projectDescription" v-model="newProject.description" class="form-control"
        placeholder="In dit project maak ik..." rows="4"></textarea>
    </div>

    <!-- Year Selection -->
    <div class="mb-4">
      <label for="yearSelection" class="form-label h5">Jaar: </label>
      <select id="yearSelection" v-model="selectedYear" class="form-select">
        <option v-for="year in student_progression" :key="year.description" :value="year">
          {{ year.description }}
        </option>
      </select>
    </div>

    <!-- Course Selection and Selected Courses Box -->
    <div v-if="selectedYear" class="mb-4 d-flex align-items-start">
      <!-- Course Selection -->
      <div class="flex-grow-1 me-3">
        <h2 class="h5">Course: </h2>
        <div class="list-group">
          <div v-for="(course, courseIndex) in selectedYear.courses" :key="course.id" class="list-group-item">
            <div class="form-check" @click="toggleCourseSelection(course)">
              <input class="form-check-input" type="checkbox" :id="'course-' + course.id" :value="course"
                v-model="selectedCourses" />
              <label @click="toggleCourseSelection(course)" class="form-check-label" :for="'course-' + course.id" >
                {{ course.beoordelingscriteria }}
              </label>
            </div>
            <!-- Criteria Selection for the Course -->
            <div v-if="selectedCourses.includes(course)" class="mt-2">
              <table class="table table-bordered table-fixed">
                <thead>
                  <tr>
                    <th style="width: 10%;">Criteria</th>
                    <th style="width: 60%;">Criteriabeschrijving</th>
                    <th style="width: 10%;">Eindbeoordeling</th>
                    <th style="width: 5%;">Selecteer</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(criterion, criterionIndex) in course.criteriaProgress" :key="criterion.id">
                    <td style="white-space: pre-wrap;">{{ criterion.name }}</td>
                    <td style="white-space: pre-wrap;">{{ criterion.verantwoording || 'N/A' }}</td>
                    <td :class="getBadgeClass(criterion.grade)">{{ criterion.grade }}</td>
                    <td @click="toggleCriterionSelection(criterion)" style="cursor: pointer;">
                      <input class="form-check-input select-criteria-box" type="checkbox" :id="'criterion-' + criterion.id"
                      :value="criterion" v-model="selectedCriteria" @click.stop />
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <!-- Selected Criteria Box -->
      <div class="selection-holders">
        <div v-if="selectedCriteria.length > 0" class="selected-courses-box">
          <h2 class="h5">Geselecteerde criteria</h2>
          <ul class="list-group">
            <li v-for="(criterion, index) in selectedCriteria" :key="index"
              class="list-group-item d-flex justify-content-between align-items-center">
              <span class="me-3 nav-link" @click="openOverlay(criterion)" style="cursor: pointer;">
                {{ criterion.name }}
              </span>
              <button @click="removeCriteria(index)" class="btn btn-sm btn-danger">Verwijder</button>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <!-- Overlay -->
    <div v-if="overlayVisible" class="overlay" @click="closeOverlay">
      <div class="overlay-content position-relative" @click.stop>
      <button @click="closeOverlay" class="btn btn-close position-absolute top-0 end-0 m-2" aria-label="Close"></button>
      <h3 class="h5 mt-2">{{ overlayContent.name }}</h3>
      <p>{{ overlayContent.verantwoording }}</p>
      </div>
    </div>

    <!-- Save Button -->
    <button @click="saveProject" class="btn btn-primary">Verstuur aanvraag</button>
  </div>
</template>

<style scoped></style>

<script setup>
import { ref } from 'vue';
import { student_projects, student_progression } from '@/dummydata/dummydata.js';

import { useRouter } from 'vue-router';
const router = useRouter();

const newProject = ref({
  name: '',
  description: '',
  running: true,
  criteriaToReach: {},
});

const selectedYear = ref(null);
const selectedCourses = ref([]);
const selectedCriteria = ref([]);

const removeCriteria = (index) => {
  selectedCriteria.value.splice(index, 1);
};

const saveProject = () => {
  if (!newProject.value.name) {
    alert('Please enter a project name.');
    return;
  }

  if (!newProject.value.description) {
    alert('Please enter a project description.');
    return;
  }

  if (selectedCriteria.value.length === 0) {
    alert('Minimaal 1 criteria moet geselecteerd worden.');
    return;
  }

  // Map selected criteria to the required format
  const criteriaToReach = {};
  selectedCriteria.value.forEach((criterion) => {
    criteriaToReach[criterion.name] = {
      studentVerantwoording: 'Vul hier je bewijslast in.',
      feedback: 'Leraar feedback',
      grade: 'Onvoldoende',
      awaitingTeacher: true,
    };
  });

  // Save the new project
  student_projects.value[newProject.value.name] = {
    id: Object.keys(student_projects.value).length + 1,
    running: false,
    description: newProject.value.description,
    criteriaToReach,
    awaitingTeacher: true,
  };

  router.push(`/projects/`);

  // Reset form
  newProject.value.name = '';
  newProject.value.description = '';
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

const getBadgeClass = (beoordeling) => {
  // if(isProjectAwaitingTeacher) {
  //   return 'badge bg-warning text-white';
  // }
  if (beoordeling === "Goed") {
    return 'badge bg-success text-white';
  } else if (beoordeling === "Voldoende") {
    return 'badge bg-info text-white';
  } else {
    return 'badge bg-danger text-white';
  }
};

const getCriteriumClass = (beoordeling) => {
  if (beoordeling === "Goed") {
    return 'bg-success-subtle';
  } else if (beoordeling === "Voldoende") {
    return 'bg-info-subtle';
  } else {
    return 'bg-danger-subtle';
  }
};

const getRowClass = (beoordeling) => {
  switch (beoordeling) {
    case 'Goed':
      return 'table-success';
    case 'Voldoende':
      return 'table-info';
    case 'Onvoldoende':
      return 'table-danger';
  }
};

const toggleCriterionSelection = (criterion) => {
  const index = selectedCriteria.value.indexOf(criterion);
  if (index > -1) {
    selectedCriteria.value.splice(index, 1);
  } else {
    selectedCriteria.value.push(criterion);
  }
};

const toggleCourseSelection = (course) => {
  const index = selectedCourses.value.indexOf(course);
  if (index > -1) {
    selectedCourses.value.splice(index, 1);
  } else {
    selectedCourses.value.push(course);
  }
};

</script>

<style scoped>
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