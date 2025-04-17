<template>
  <div class="container-fluid mt-5">
    <div class="row mb-3">
      <div class="col-md-3">
        <label for="beoordelingFilter" class="form-label">Filter by Beoordeling:</label>
        <select id="beoordelingFilter" v-model="selectedBeoordeling" @change="filterData" class="form-select">
          <option value="">All</option>
          <option v-for="beoordeling in uniqueBeoordelingen" :key="beoordeling" :value="beoordeling">{{ beoordeling }}
          </option>
        </select>
      </div>
      <div class="col-md-6 text-end">
        <div class="d-flex justify-content-end">
          <div class="chart-container me-3">
            <div class="donut-chart" :style="getDonutStyle(percentageCompleted, '#007bff')"></div>
            <div class="chart-label">Completed</div>
          </div>
          <div class="chart-container me-3">
            <div class="donut-chart" :style="getDonutStyle(percentageVoldoende, '#ffc107')"></div>
            <div class="chart-label">Voldoende</div>
          </div>
          <div class="chart-container me-3">
            <div class="donut-chart" :style="getDonutStyle(percentageGoed, '#28a745')"></div>
            <div class="chart-label">Goed</div>
          </div>
          <div class="chart-container">
            <div class="donut-chart" :style="getDonutStyle(percentageOnvoldoende, '#dc3545')"></div>
            <div class="chart-label">Onvoldoende</div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-12">
        <div class="table-responsive">
          <table class="table table-striped table-hover table-bordered w-100">
            <thead class="bg-light-blue text-white">
              <tr>
                <th @click="sort('project')" class="cursor-pointer">
                  Project
                  <i :class="getSortIcon('project')"></i>
                </th>
                <th @click="sort('verantwoording')" class="cursor-pointer">
                  Verantwoording
                  <i :class="getSortIcon('verantwoording')"></i>
                </th>
                <th @click="sort('fase')" class="cursor-pointer">
                  Gelinkt project
                  <i :class="getSortIcon('fase')"></i>
                </th>
                <th @click="sort('feedback')" class="cursor-pointer">
                  Feedback
                  <i :class="getSortIcon('feedback')"></i>
                </th>
                <th @click="sort('beoordeling')" class="cursor-pointer">
                  Beoordeling
                  <i :class="getSortIcon('beoordeling')"></i>
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in paginatedData" :key="item.id" :class="getRowClass(item.beoordeling)">
                <td>{{ item.project }}</td>
                <td style="white-space: pre-wrap;">{{ item.verantwoording }}</td>
                <td>{{ item.linked_project }}</td>
                <td>{{ item.feedback }}</td>
                <td>
                  <span :class="getBadgeClass(item.beoordeling)">{{ item.beoordeling }}</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <nav aria-label="Page navigation">
          <ul class="pagination justify-content-center">
            <li class="page-item" :class="{ disabled: currentPage === 1 }">
              <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">Previous</a>
            </li>
            <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: currentPage === page }">
              <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
            </li>
            <li class="page-item" :class="{ disabled: currentPage === totalPages }">
              <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">Next</a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRoute } from 'vue-router'; // Import useRoute

import student_projects from '@/dummydata/studentProjects.js'; // Import student projects data
import school_criteria from '@/dummydata/courseCriteria.js'; // Import course criteria data

const route = useRoute(); // Get the current route
const rubricId = ref(route.params.rubric); // Access the dynamic route parameter

// Example: Log the rubric ID to verify
console.log('Rubric ID:', rubricId.value);



const data = ref(mappedData.value[rubricId.value] || []); // Use the rubricId to filter the data

console.log('Mapped Data:', mappedData.value); // Log the mapped data to verify
console.log('Data:', data.value); // Log the data to verify

const sortKey = ref('');
const sortOrder = ref('asc');
const selectedFase = ref('');
const selectedBeoordeling = ref('');
const currentPage = ref(1);
const itemsPerPage = 10;

const sort = (key) => {
  if (sortKey.value === key) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  } else {
    sortKey.value = key;
    sortOrder.value = 'asc';
  }
};

const getSortIcon = (key) => {
  if (sortKey.value !== key) return 'bi bi-arrow-down-up';
  return sortOrder.value === 'asc' ? 'bi bi-arrow-up' : 'bi bi-arrow-down';
};

const sortedData = computed(() => {
  const sorted = [...data.value];
  if (sortKey.value) {
    sorted.sort((a, b) => {
      const aValue = a[sortKey.value];
      const bValue = b[sortKey.value];

      if (typeof aValue === 'string') {
        return sortOrder.value === 'asc'
          ? aValue.localeCompare(bValue)
          : bValue.localeCompare(aValue);
      } else {
        return sortOrder.value === 'asc'
          ? aValue - bValue
          : bValue - aValue;
      }
    });
  }
  return sorted;
});

const uniqueFases = computed(() => {
  const fases = data.value.map(item => item.fase);
  return [...new Set(fases)];
});

const uniqueBeoordelingen = computed(() => {
  const beoordelingen = data.value.map(item => item.beoordeling);
  return [...new Set(beoordelingen)];
});

const filteredData = computed(() => {
  let filtered = sortedData.value;
  if (selectedFase.value) {
    filtered = filtered.filter(item => item.fase === selectedFase.value);
  }
  if (selectedBeoordeling.value) {
    filtered = filtered.filter(item => item.beoordeling === selectedBeoordeling.value);
  }
  return filtered;
});

const totalPages = computed(() => Math.ceil(filteredData.value.length / itemsPerPage));

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredData.value.slice(start, end);
});

const filterData = () => {
  currentPage.value = 1;
};

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
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

const getBadgeClass = (beoordeling) => {
  switch (beoordeling) {
    case 'Goed':
      return 'badge bg-success';
    case 'Voldoende':
      return 'badge bg-info';
    case 'Onvoldoende':
      return 'badge bg-danger';
  }
};

console.log("Data value 0: ", data.value);

// Calculate percentages
const percentageCompleted = computed(() => {
  const totalCompleted = data.value.filter(item => 
    item.beoordeling === "Goed" || item.beoordeling === "Voldoende"
  ).length;
  return ((totalCompleted / data.value.length) * 100).toFixed(2);
});

const percentageVoldoende = computed(() => {
  const totalCompleted = data.value.filter(item => 
    item.beoordeling === "Voldoende"
  ).length;
  return ((totalCompleted / data.value.length) * 100).toFixed(2);
});

const percentageGoed = computed(() => {
  const totalCompleted = data.value.filter(item => 
    item.beoordeling === "Goed"
  ).length;
  return ((totalCompleted / data.value.length) * 100).toFixed(2);
});

const percentageOnvoldoende = computed(() => {
  const totalCompleted = data.value.filter(item => 
    item.beoordeling === "Onvoldoende"
  ).length;
  return ((totalCompleted / data.value.length) * 100).toFixed(2);
});

// Donut chart styles
const getDonutStyle = (percentage, color) => {
  const rotation = (percentage / 100) * 360;
  return {
    background: `conic-gradient(${color} ${rotation}deg, #e9ecef ${rotation}deg)`,
  };
};
</script>

<style scoped>
.cursor-pointer {
  cursor: pointer;
}

.table {
  width: 100%;
}

.bg-light-blue {
  background-color: #3498db;
}
</style>
