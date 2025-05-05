<template>
  <div class="container mt-5">
    <h1 class="mb-4 d-flex justify-content-between align-items-center">
      <span>Project {{ project }}</span>
      <span v-if="isProjectAwaitingTeacher" class="badge bg-warning text-dark text-white">Afwachtend op
        goedkeuring</span>
    </h1>

    <!-- Criteria Switches -->
    <div class="mb-4">
      <h2 class="h4 mb-3">Select Criteria</h2>
      <div class="list-group">
        <div v-for="criterion in onderbouwdeCriteria" :key="criterion.id" class="list-group-item">
          <div class="form-check form-switch">
            <input class="form-check-input" type="checkbox" :id="'criterion-' + criterion.id"
              v-model="criterion.active">
            <label class="form-check-label" :for="'criterion-' + criterion.id">
              {{ criterion.name }}
            </label>
          </div>
        </div>
      </div>
    </div>

    <!-- Dynamic Text Boxes -->
    <div v-if="activeCriteria.length > 0">
      <h2 class="h4 mb-3" v-if="isProjectActive">Justify Your Work</h2>
      <h2 class="h4 mb-3" v-if="!isProjectActive">Your Work</h2>
      <div v-for="criterion in activeCriteria" :key="criterion.id"
        :class="['card', 'mb-3', getCriteriumClass(criterion.grade)]">
        <div class="card-body">
          <div class="d-flex justify-content-between align-items-center">
            <h3 @click="openOverlay(criterion.name)" :class="['card-title h5', getBadgeClass(criterion.grade)]">{{
              criterion.name }}</h3>
            <h3 v-if="!isProjectAwaitingTeacher" :class="['card-title h5', getBadgeClass(criterion.grade)]">{{ criterion.grade }}</h3>
          </div>
          <p class="card-text" v-html="getCriteriaDescription(criterion.name).replace(/\n/g, '<br>')"></p>
          <div class="mb-3" v-if="!isProjectAwaitingTeacher">
            <textarea v-model="criterion.text" :disabled="!isProjectActive"
              :placeholder="`Explain why you deserve a good grade for ${criterion.name.toLowerCase()}...`" rows="3"
              :class="['form-control']"></textarea>
          </div>
          <div v-if="criterion.feedback != '' && !isProjectAwaitingTeacher" class="mb-3">
            <div class="p-3 rounded bg-light text-muted border">
              <strong class="d-block mb-1">Feedback:</strong>
              <p class="mb-0">{{ criterion.feedback }}</p>
            </div>
          </div>
          <button @click="saveText(criterion)" v-if="isProjectActive" class="btn btn-primary">
            Save
          </button>
        </div>
      </div>
    </div>

    <div v-else class="text-center py-5">
      <i class="bi bi-clipboard-check display-1 text-muted"></i>
      <p class="mt-3 text-muted">Select criteria to start your evaluation</p>
    </div>
  </div>

  <!-- Overlay -->
  <div v-if="overlayVisible" class="overlay" @click="closeOverlay">
    <div class="overlay-content" @click.stop>
      <h3 class="h5">{{ overlayContentHeader }}</h3>
      <p>{{ overlayContentDescription }}</p>
      <button @click="closeOverlay" class="btn btn-secondary mt-3">Close</button>
    </div>
  </div>

</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'

import { student_projects, school_criteria } from '@/dummydata/dummydata.js'

const route = useRoute()
const project = route.params.project

const isProjectActive = student_projects.value[project].running

const isProjectAwaitingTeacher = student_projects.value[project].awaitingTeacher

// Initialize criteria with existing data
let projectInfo = Object.entries(student_projects.value[project].criteriaToReach).map(([key, item]) => {
  return {
    id: key,
    name: key,
    active: true,
    text: item.studentVerantwoording,
    grade: item.grade,
    feedback: item.feedback,
    awaitingTeacher: item.awaitingTeacher,
  }
})

const onderbouwdeCriteria = ref([
  ...projectInfo
])

const activeCriteria = computed(() => {
  return onderbouwdeCriteria.value.filter(criterion => criterion.active)
})

const getCriteriaDescription = (criterion) => {
  // Flatten the criteria structure for easier searching
  const allCriteria = school_criteria.value.flatMap(studentProgres =>
    studentProgres.years.flatMap(course => {
      return course.courses.flatMap(courseCriteria => courseCriteria.criteria);
    })
  );

  // Find and return the matching criterion's description
  const foundCriterion = allCriteria.find(courseCriteria => courseCriteria.project === criterion);
  return foundCriterion ? foundCriterion.verantwoording : '';
};

const getBadgeClass = (beoordeling) => {
  if(isProjectAwaitingTeacher) {
    return 'badge bg-warning text-white';
  }
  if (beoordeling === "Goed") {
    return 'badge bg-success';
  } else if (beoordeling === "Voldoende") {
    return 'badge bg-info';
  } else {
    return 'badge bg-danger';
  }
};

const getCriteriumClass = (beoordeling) => {
  if(isProjectAwaitingTeacher) {
    return 'bg-warning-subtle';
  }
  if (beoordeling === "Goed") {
    return 'bg-success-subtle';
  } else if (beoordeling === "Voldoende") {
    return 'bg-info-subtle';
  } else {
    return 'bg-danger-subtle';
  }
};

const getCriteriaDescriptionFromName = (name) => {
  for (const studentProgress of school_criteria.value) {
    const criteria = studentProgress.years.flatMap(year =>
      year.courses.flatMap(course => course.criteria)
    );
    const found = criteria.find(criterion => criterion.name === name);
    if (found) return found.verantwoording;
  }
  return 'Description not found.';
};

const saveText = (criterion) => {
  // Emit an event to the parent component with the criterion and its text
  emit('save-criterion', { id: criterion.id, text: criterion.text })

  // For demonstration purposes, we'll just log to the console
  console.log(`Saving for ${criterion.name}:`, criterion.text)
}

// Define the emit function for use in the template
const emit = defineEmits(['save-criterion'])

const overlayVisible = ref(false);
const overlayContentHeader = ref('');
const overlayContentDescription = ref('');

if (isProjectAwaitingTeacher) {
  overlayVisible.value = true;
  overlayContentHeader.value = 'Afwachtend op goedkeuring';
  overlayContentDescription.value = 'Je project is afwachtend op goedkeuring van de leraar. Je project draait dus nog NIET.';
}

const openOverlay = (header, description) => {
  overlayContentHeader.value = header;
  overlayContentDescription.value = description || getCriteriaDescriptionFromName(header);
  overlayVisible.value = true;
};
const closeOverlay = () => {
  overlayVisible.value = false;
  overlayContentHeader.value = '';
  overlayContentDescription.value = '';
};
</script>

<style scoped>
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
