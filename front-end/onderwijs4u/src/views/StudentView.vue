<template>
  <div class="container mt-5">
    <template v-if="awaitingTeacher">
      <h1 class="mb-4">Project Review: {{ project }}</h1>
      <div class="card mb-4">
        <div class="card-header bg-light">
          <h2 class="h5 mb-0">Project Description</h2>
        </div>
        <div class="card-body">
          <p>{{ projectDescription }}</p>
        </div>
      </div>
      <div class="card mb-4">
        <div class="card-header bg-light">
          <h2 class="h5 mb-0">Criteria To Reach</h2>
        </div>
        <div class="card-body">
          <ul>
            <li v-for="(crit, name) in criteriaToReach" :key="name">
              <strong>{{ name }}:</strong> {{ crit.studentVerantwoording }}
            </li>
          </ul>
        </div>
      </div>
      <button class="btn btn-success me-2" @click="allowProject">Allow Project</button>
      <button class="btn btn-warning" @click="requestInfo">Request Additional Information</button>
    </template>

    <template v-else>
      <h1 class="mb-4">Review Work from {{ student }} - {{ project }}</h1>
      <div v-if="criteria.length > 0">
        <div v-for="criterion in criteria" :key="criterion.name" class="card mb-4">
          <div class="card-header bg-light">
            <h2 class="h5 mb-0">{{ criterion.name }}</h2>
          </div>
          <div class="card-body">
            <h3 class="h6 mb-3">Criteriaverdediging:</h3>
            <p class="mb-3">{{ criterion.studentVerantwoording }}</p>

            <h3 class="h6 mb-3">Beoordeling:</h3>
            <div class="mb-3">
              <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" :id="'onvoldoende-' + criterion.name"
                  :name="'review-' + criterion.name" value="onvoldoende" v-model="criterion.teacherReview">
                <label class="form-check-label" :for="'onvoldoende-' + criterion.name">
                  Onvoldoende
                </label>
              </div>
              <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" :id="'voldoende-' + criterion.name"
                  :name="'review-' + criterion.name" value="voldoende" v-model="criterion.teacherReview">
                <label class="form-check-label" :for="'voldoende-' + criterion.name">
                  Voldoende
                </label>
              </div>
              <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" :id="'goed-' + criterion.name"
                  :name="'review-' + criterion.name" value="goed" v-model="criterion.teacherReview">
                <label class="form-check-label" :for="'goed-' + criterion.name">
                  Goed
                </label>
              </div>
            </div>

            <div class="mb-3">
              <label :for="'feedback-' + criterion.name" class="form-label">Feedback:</label>
              <textarea :id="'feedback-' + criterion.name" v-model="criterion.teacherFeedback" class="form-control"
                rows="3" placeholder=""></textarea>
            </div>

            <button @click="saveFeedback(criterion)" class="btn btn-primary">
              <i class="bi bi-save me-2"></i> Save Feedback
            </button>
          </div>
        </div>
      </div>
      <div v-else class="text-center py-5">
        <i class="bi bi-clipboard-x display-1 text-muted"></i>
        <p class="mt-3 text-muted">No criteria available for review</p>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { student_projects } from '@/dummydata/dummydata.js'

const route = useRoute()
const project = route.params.project
const student = route.params.student

const criteria = ref([])
const awaitingTeacher = ref(false)
const projectDescription = ref('')
const criteriaToReach = ref({})

onMounted(() => {
  // Get the project data from dummydata
  const projectData = student_projects.value[project]
  if (projectData) {
    awaitingTeacher.value = !!projectData.awaitingTeacher
    projectDescription.value = projectData.description || ''
    criteriaToReach.value = projectData.criteriaToReach || {}

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
  }
})

const saveFeedback = (criterion) => {
  // In a real application, you would send this data to a backend
  console.log(`Saving feedback for ${criterion.name}:`, {
    review: criterion.teacherReview,
    feedback: criterion.teacherFeedback
  })
  alert(`Feedback for ${criterion.name} has been saved!`)
}

const allowProject = () => {
  // In a real app, update the backend here
  const projectData = student_projects.value[project]
  if (projectData) {
    projectData.awaitingTeacher = false
    awaitingTeacher.value = false
    projectData.running = true
    // Optionally reload criteria for review
    if (projectData.criteriaToReach) {
      criteria.value = Object.entries(projectData.criteriaToReach).map(([name, crit]) => ({
        name,
        studentVerantwoording: crit.studentVerantwoording,
        teacherReview: crit.grade ? crit.grade.toLowerCase() : '',
        teacherFeedback: crit.feedback || ''
      }))
    }
  }
  alert('Project is now allowed to be run.')
}

const requestInfo = () => {
  // In a real app, notify the student for more info
  alert('Requested additional information from the student.')
}
</script>