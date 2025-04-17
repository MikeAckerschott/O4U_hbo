<template>
  <div class="container mt-5">
    <h1 class="mb-4">Review Work from {{ student }} - {{ project }}</h1>

    <div v-if="criteria.length > 0">
      <div v-for="criterion in criteria" :key="criterion.id" class="card mb-4">
        <div class="card-header bg-light">
          <h2 class="h5 mb-0">{{ criterion.name }}</h2>
        </div>
        <div class="card-body">
          <h3 class="h6 mb-3">Student's Justification:</h3>
          <p class="mb-3">{{ criterion.text }}</p>

          <h3 class="h6 mb-3">Teacher's Review:</h3>
          <div class="mb-3">
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="radio"
                :id="'agree-' + criterion.id"
                :name="'review-' + criterion.id"
                value="agree"
                v-model="criterion.teacherReview"
              >
              <label class="form-check-label" :for="'agree-' + criterion.id">
                Agree
              </label>
            </div>
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="radio"
                :id="'disagree-' + criterion.id"
                :name="'review-' + criterion.id"
                value="disagree"
                v-model="criterion.teacherReview"
              >
              <label class="form-check-label" :for="'disagree-' + criterion.id">
                Disagree
              </label>
            </div>
          </div>

          <div class="mb-3">
            <label :for="'feedback-' + criterion.id" class="form-label">Feedback:</label>
            <textarea
              :id="'feedback-' + criterion.id"
              v-model="criterion.teacherFeedback"
              class="form-control"
              rows="3"
              placeholder="Provide your feedback here..."
            ></textarea>
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const project = route.params.project
const student = route.params.student

// Initialize criteria with student data
const criteria = ref([])

onMounted(() => {
  // Simulating data fetch or prop passing
  criteria.value = [
    { id: 1, name: 'Research', text: 'I conducted extensive research using various academic sources...', teacherReview: '', teacherFeedback: '' },
    { id: 2, name: 'Analysis', text: 'My analysis involved comparing different methodologies...', teacherReview: '', teacherFeedback: '' },
    { id: 5, name: 'Teamwork', text: 'Our team collaborated effectively by holding regular meetings...', teacherReview: '', teacherFeedback: '' },
  ]
})

const saveFeedback = (criterion) => {
  // In a real application, you would send this data to a backend
  console.log(`Saving feedback for ${criterion.name}:`, {
    review: criterion.teacherReview,
    feedback: criterion.teacherFeedback
  })

  // Show a success message to the user
  alert(`Feedback for ${criterion.name} has been saved!`)
}
</script>

<style scoped>
/* Custom styles can be added here if needed */
</style>