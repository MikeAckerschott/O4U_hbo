<template>
  <div class="container mt-5">
    <h1 class="mb-4"> {{ student }} - {{ project }}</h1>
    <div v-if="criteria.length > 0">
      <div v-for="criterion in criteria" :key="criterion.name" class="card mb-4">
        <div class="card-header bg-light">
          <h2 class="h5 mb-0" @click="toggleCriterion(criterion.name)" style="cursor:pointer;">
            {{ criterion.name }}
          </h2>
          <p v-if="expandedCriterion === criterion.name" style="white-space: pre-line;">{{
            mappedCriteria[criterion.name] }}</p>
        </div>
        <div class="card-body">
          <h3 class="h6 mb-3">Criteriaverdediging:</h3>
          <p class="mb-3">{{ criterion.studentVerantwoording }}</p>

          <h3 class="h6 mb-3">Beoordeling:</h3>
          <div class="mb-3">
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" :id="'onvoldoende-' + criterion.name"
                :name="'review-' + criterion.name" value="onvoldoende" v-model="criterion.teacherReview"
                :disabled="!projectRunning">
              <label class="form-check-label" :for="'onvoldoende-' + criterion.name">
                Onvoldoende
              </label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" :id="'voldoende-' + criterion.name"
                :name="'review-' + criterion.name" value="voldoende" v-model="criterion.teacherReview"
                :disabled="!projectRunning">
              <label class="form-check-label" :for="'voldoende-' + criterion.name">
                Voldoende
              </label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" :id="'goed-' + criterion.name"
                :name="'review-' + criterion.name" value="goed" v-model="criterion.teacherReview"
                :disabled="!projectRunning">
              <label class="form-check-label" :for="'goed-' + criterion.name">
                Goed
              </label>
            </div>
          </div>

          <div class="mb-3">
            <label :for="'feedback-' + criterion.name" class="form-label">Feedback:</label>
            <textarea :id="'feedback-' + criterion.name" v-model="criterion.teacherFeedback" class="form-control"
              rows="3" placeholder="" :disabled="!projectRunning"></textarea>

          </div>

          <button @click="saveFeedback(criterion)" class="btn btn-primary" :disabled="!projectRunning">
            <i class="bi bi-save me-2"></i> Feedback Opslaan
          </button>
        </div>
      </div>
    </div>
    <div v-else class="text-center py-5">
      <i class="bi bi-clipboard-x display-1 text-muted"></i>
      <p class="mt-3 text-muted">No criteria available for review</p>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { student_projects, mappedCriteria } from '@/dummydata/dummydata.js'

const route = useRoute()
const project = route.params.project
const student = route.params.student

const criteria = ref([])
const awaitingTeacher = ref(false)
const projectDescription = ref('')
const criteriaToReach = ref({})
const expandedCriterion = ref(null) // Track which criterion is expanded

const toggleCriterion = (name) => {
  expandedCriterion.value = expandedCriterion.value === name ? null : name
}

const projectRunning = ref(false);

onMounted(() => {
  // Get the project data from dummydata
  const projectData = student_projects.value[project]
  if (projectData) {
    awaitingTeacher.value = !!projectData.awaitingTeacher
    projectDescription.value = projectData.description || ''
    criteriaToReach.value = projectData.criteriaToReach || {}
    projectRunning.value = !!projectData.running

    if (!awaitingTeacher.value && projectData.criteriaToReach) {
      criteria.value = Object.entries(projectData.criteriaToReach).map(([name, crit]) => ({
        name,
        studentVerantwoording: crit.studentVerantwoording,
        teacherReview: crit.grade ? crit.grade.toLowerCase() : '',
        teacherFeedback: crit.feedback || ''
      }))
    } else {
      criteria.value = []
    }
  } else {
    criteria.value = []
    criteriaToReach.value = {}
    projectDescription.value = ''
    projectRunning.value = false
  }
})

const saveFeedback = (criterion) => {
  if (!projectRunning.value) {
    alert('Feedback en beoordeling kunnen niet worden opgeslagen omdat het project niet actief is.')
    return
  }
  // Find the project and update the correct criterion
  const projectData = student_projects.value[project]
  if (projectData && projectData.criteriaToReach && projectData.criteriaToReach[criterion.name]) {
    // Update the feedback and grade in the dummydata
    projectData.criteriaToReach[criterion.name].feedback = criterion.teacherFeedback
    // Capitalize the first letter for consistency with dummydata
    projectData.criteriaToReach[criterion.name].grade = criterion.teacherReview
      ? criterion.teacherReview.charAt(0).toUpperCase() + criterion.teacherReview.slice(1)
      : ''
  }
  alert(`Feedback voor ${criterion.name} is opgeslagen!`)
}

const requestInfo = () => {
  // In a real app, notify the student for more info
  alert('Requested additional information from the student.')
}
</script>