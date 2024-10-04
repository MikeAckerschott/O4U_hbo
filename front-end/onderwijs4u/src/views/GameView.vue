<template>
  <div class="container">
    <h2 class="text-center mb-4">Select Your Emblem</h2>
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-4">
      <div v-for="emblem in emblems" :key="emblem.id" class="col">
        <div
          class="card h-100 emblem-container"
          :class="{ 'border-primary': emblem.unlocked && selectedEmblem === emblem.id, 'border-secondary': !emblem.unlocked }"
          @click="selectEmblem(emblem)"
        >
          <div class="card-img-top emblem-image" :style="{ backgroundColor: emblem.color }">
            <span class="emblem-text">{{ emblem.name.charAt(0) }}</span>
          </div>
          <div class="card-body">
            <h5 class="card-title">{{ emblem.name }}</h5>
            <p v-if="emblem.unlocked" class="card-text text-success">Unlocked</p>
            <p v-else class="card-text text-muted">{{ emblem.requirement }}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="text-center mt-4" v-if="selectedEmblem">
      <p class="lead">You've selected: {{ getSelectedEmblemName() }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const emblems = ref([
  { id: 1, name: 'Bronze Border', color: '#CD7F32', unlocked: true, requirement: 'Haal 1 keer de beoordeling goed' },
  { id: 2, name: 'Silver Border', color: '#C0C0C0', unlocked: true, requirement: 'Haal 5 keer de beoordeling goed' },
  { id: 3, name: 'Gold Border', color: '#FFD700', unlocked: true, requirement: 'Haal 10 keer de beoordeling goed' },
  { id: 4, name: 'Diamond Border', color: '#B9F2FF', unlocked: false, requirement: 'Haal 20 keer de beoordeling goed' },
])

const selectedEmblem = ref(null)

const selectEmblem = (emblem) => {
  if (emblem.unlocked) {
    selectedEmblem.value = emblem.id
    sessionStorage.setItem('navcolor', emblem.color)
  }
  location.reload()
}

const getSelectedEmblemName = () => {
  const selected = emblems.value.find(e => e.id === selectedEmblem.value)
  return selected ? selected.name : ''
}
</script>

<style scoped>

.container{
padding-top: 100px;
}

.emblem-container {
  cursor: pointer;
  transition: all 0.3s ease;
}

.emblem-container:not(.border-secondary):hover {
  box-shadow: 0 0 10px rgba(0, 123, 255, 0.5);
}

.border-secondary {
  opacity: 0.6;
  cursor: not-allowed;
}

.emblem-image {
  height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.emblem-text {
  font-size: 48px;
  font-weight: bold;
  color: white;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}
</style>
