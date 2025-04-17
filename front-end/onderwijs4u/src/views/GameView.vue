<template>
  <div class="relative w-full h-screen bg-gray-100 overflow-hidden">
    <div class="absolute top-4 left-4 bg-white p-4 rounded-lg shadow-md">
      <h2 class="text-2xl font-bold text-gray-800">Score: {{ score }}</h2>
    </div>
    <button
        @click="onClick"
        :style="buttonStyle"
        class="movingbutton">
      Click me!
    </button>
  </div>
</template>

<script setup>
import {ref, computed} from 'vue'

let score = 0
let buttonPosition = ref({x: 50, y: 50})
let borderStyle = ""
let colorStyle = "#000000"

const buttonStyle = computed(() => ({
  left: `${buttonPosition.value.x}%`,
  top: `${buttonPosition.value.y}%`,
  "background-color": "#FFFFFF",
  border: borderStyle,
  color: colorStyle,
  transform: 'translate(-50%, -50%)'

}))
function moveButton(){
  buttonPosition.value = {
    x: Math.random() * 80 + 10, // Keep button within 10-90% of the screen width
    y: Math.random() * 80 + 10  // Keep button within 10-90% of the screen height
  }
}

function onClick (){
  score++
  console.log(score)
  switch (score) {
    case 5: borderStyle = "none"; break
    case 10: colorStyle = "#FFFFFF" ; break
  }
  moveButton()
}
</script>

<style scoped>
.movingbutton {
  position: absolute;
  transition: all 0.5s ease-out;
}
</style>