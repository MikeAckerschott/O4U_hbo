<template>
  <div class="container mt-5">
    <h1 class="mb-4">Project {{ project }} </h1>

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
      <h2 class="h4 mb-3">Justify Your Work</h2>
      <div v-for="criterion in activeCriteria" :key="criterion.id" class="card mb-3">
        <div class="card-body">
          <h3 class="card-title h5">{{ criterion.name }}</h3>
          <p class="card-text" v-html="getCriteriaDescription(criterion.name).replace(/\n/g, '<br>')"></p>
          <div class="mb-3">
            <textarea v-model="criterion.text"
              :placeholder="`Explain why you deserve a good grade for ${criterion.name.toLowerCase()}...`" rows="3"
              class="form-control"></textarea>
          </div>
          <button @click="saveText(criterion)" class="btn btn-primary">
            <i class="bi bi-save me-2"></i> Save
          </button>
        </div>
      </div>
    </div>

    <div v-else class="text-center py-5">
      <i class="bi bi-clipboard-check display-1 text-muted"></i>
      <p class="mt-3 text-muted">Select criteria to start your evaluation</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'

import student_projects from '@/dummydata/studentProjects.js'
import school_criteria from '@/dummydata/courseCriteria.js'

const route = useRoute()
const project = route.params.project

console.log(project)
console.log(student_projects)



// Initialize criteria with existing data
let projectInfo = Object.entries(student_projects.value[project].criteriaToReach).map(([key, item]) => {
  return {
    id: key,
    name: key,
    active: true,
    text: item.studentVerantwoording
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
  const allCriteria = school_criteria.value.flatMap(year =>
    year.werkproces.flatMap(course =>
      course.criteria.flatMap(courseCriteria => courseCriteria.criteria)
    )
  );

  // Find and return the matching criterion's description
  const foundCriterion = allCriteria.find(courseCriteria => courseCriteria.project === criterion);
  return foundCriterion ? foundCriterion.verantwoording : '';
};

const saveText = (criterion) => {
  // Emit an event to the parent component with the criterion and its text
  emit('save-criterion', { id: criterion.id, text: criterion.text })

  // For demonstration purposes, we'll just log to the console
  console.log(`Saving for ${criterion.name}:`, criterion.text)
}

// Define the emit function for use in the template
const emit = defineEmits(['save-criterion'])
</script>

<style scoped>
/* Custom styles can be added here if needed */
</style>
