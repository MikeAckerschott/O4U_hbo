<template>
  <div class="container mt-5">
    <h1 class="mb-4">Project {{ project }} </h1>

    <!-- Criteria Switches -->
    <div class="mb-4">
      <h2 class="h4 mb-3">Select Criteria</h2>
      <div class="list-group">
        <div v-for="criterion in criteria" :key="criterion.id" class="list-group-item">
          <div class="form-check form-switch">
            <input
              class="form-check-input"
              type="checkbox"
              :id="'criterion-' + criterion.id"
              v-model="criterion.active"
            >
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
          <div class="mb-3">
            <textarea
              v-model="criterion.text"
              :placeholder="`Explain why you deserve a good grade for ${criterion.name.toLowerCase()}...`"
              rows="3"
              class="form-control"
            ></textarea>
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
import {ref, computed} from 'vue'
import {useRoute} from 'vue-router'

const route = useRoute()
const project = route.params.project

const criteria = ref([
  {id: 1, name: 'Research', active: false, text: ''},
  {id: 2, name: 'Analysis', active: false, text: ''},
  {id: 3, name: 'Presentation', active: false, text: ''},
  {id: 4, name: 'Innovation', active: false, text: ''},
  {id: 5, name: 'Teamwork', active: false, text: ''},
])

const activeCriteria = computed(() => {
  return criteria.value.filter(criterion => criterion.active)
})

const saveText = (criterion) => {
  // Emit an event to the parent component with the criterion and its text
  emit('save-criterion', {id: criterion.id, text: criterion.text})

  // For demonstration purposes, we'll just log to the console
  console.log(`Saving for ${criterion.name}:`, criterion.text)
}

// Define the emit function for use in the template
const emit = defineEmits(['save-criterion'])
</script>

<style scoped>
/* Custom styles can be added here if needed */
</style>
