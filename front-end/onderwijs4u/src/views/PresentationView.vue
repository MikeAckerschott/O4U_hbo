<template>
  <div class="container-fluid bg-light py-5">
    <div class="row justify-content-center">
      <div class="col-md-10">
        <div class="card shadow-lg border-0 rounded-lg overflow-hidden">
          <div class="card-header bg-primary text-white p-4">
            <h2 class="mb-0 text-center">
              <i class="bi bi-graph-up-arrow me-2"></i>Core Features
            </h2>
          </div>
          <div class="card-body p-4">
            <div class="row g-4">
              <!-- Gamification Section -->
              <div class="col-md-6">
                <div class="card h-100 border-0 bg-info bg-opacity-10">
                  <div class="card-body">
                    <h3 class="card-title text-info">
                      <i class="bi bi-trophy me-2"></i>Gamification
                    </h3>
                    <p class="card-text">Earn "goed" points and unlock exciting app themes!</p>
                  </div>
                </div>
              </div>

              <!-- Student Progress Section -->
              <div class="col-md-6">
                <div class="card h-100 border-0 bg-success bg-opacity-10">
                  <div class="card-body">
                    <h3 class="card-title text-success">
                      <i class="bi bi-person-check me-2"></i>Student Progress
                    </h3>
                    <p class="card-text">Track individual student progress throughout their study journey.</p>
                    <div class="progress" style="height: 25px;">
                      <div class="progress-bar bg-success" role="progressbar" style="width: 75%;" aria-valuenow="75" aria-valuemin="0" aria-valuemax="100">75% Complete</div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Insights Visualization Section -->
              <div class="col-md-8">
                <div class="card h-70 border-0 bg-warning bg-opacity-10">
                  <div class="card-body">
                    <h3 class="card-title text-warning">
                      <i class="bi bi-pie-chart me-2"></i>Insights Visualization
                    </h3>
                    <p class="card-text">Interactive pie charts for easy-to-understand student performance metrics.</p>
                    <div class="row">
                      <div class="col-md-4" v-for="(chart, index) in charts" :key="index">
                        <canvas :ref="'chart' + index"></canvas>
                      </div>
                    </div>
                    <div class="mt-3">
                      <button class="btn btn-outline-warning me-2">
                        <i class="bi bi-sort-down me-1"></i>Sort
                      </button>
                      <button class="btn btn-outline-warning">
                        <i class="bi bi-funnel me-1"></i>Filter
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Data Export Section -->
              <div class="col-md-4">
                <div class="card h-100 border-0 bg-danger bg-opacity-10">
                  <div class="card-body">
                    <h3 class="card-title text-danger">
                      <i class="bi bi-file-earmark-excel me-2"></i>Data Export
                    </h3>
                    <p class="card-text">Easily export student data and insights to Excel for further analysis.</p>
                    <button class="btn btn-danger">
                      <i class="bi bi-download me-1"></i>Export to Excel
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import Chart from 'chart.js/auto';

const goedPoints = ref(150);
const selectedTheme = ref('default');

const themes = [
  { id: 'default', name: 'Default', requiredPoints: 0 },
  { id: 'dark', name: 'Dark Mode', requiredPoints: 50 },
  { id: 'neon', name: 'Neon', requiredPoints: 100 },
  { id: 'pastel', name: 'Pastel', requiredPoints: 200 },
];

const canApplyTheme = computed(() => {
  const theme = themes.find(t => t.id === selectedTheme.value);
  return theme && goedPoints.value >= theme.requiredPoints;
});

const applyTheme = () => {
  // Logic to apply the selected theme would go here
  console.log(`Applying theme: ${selectedTheme.value}`);
};

const charts = ref([
  { label: 'Performance', data: [70, 30] },
  { label: 'Engagement', data: [85, 15] },
  { label: 'Completion', data: [60, 40] }
]);

onMounted(() => {
  charts.value.forEach((chart, index) => {
    new Chart(document.getElementById('chart' + index), {
      type: 'pie',
      data: {
        labels: [chart.label, 'Remaining'],
        datasets: [{
          data: chart.data,
          backgroundColor: ['#ffc107', '#f8f9fa']
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'bottom',
          },
          title: {
            display: true,
            text: chart.label
          }
        }
      }
    });
  });
});
</script>

<style scoped>
@import 'bootstrap/dist/css/bootstrap.min.css';
@import 'bootstrap-icons/font/bootstrap-icons.css';

.card-header {
  background: linear-gradient(45deg, #007bff, #6610f2);
}

.card {
  transition: transform 0.3s ease-in-out;
}

.card:hover {
  transform: translateY(-5px);
}
</style>