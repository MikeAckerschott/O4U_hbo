<template>
  <div class="container-fluid mt-5">
    <div class="row mb-3">
      <div class="col-md-3">
        <label for="beoordelingFilter" class="form-label">Filter op Beoordeling:</label>
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
                <th @click="sort('beoordeling')" class="cursor-pointer">
                  Beoordeling
                  <i :class="getSortIcon('beoordeling')"></i>
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in paginatedData" :key="item.id"
                :class="getRowClass(getCriteriumBeoordeling(item.name))">
                <td>{{ item.name }}</td>
                <td style="white-space: pre-wrap;">{{ item.verantwoording }}</td>
                <td>
                  <RouterLink v-for="studentProject in getAttachedProjectsFromCriterium(item.name)"
                    class="nav-link text-nowrap" :to="`/project/${studentProject.key}`">
                    {{ studentProject.key }}
                  </RouterLink>
                </td>
                <td>
                  <span :class="getBadgeClass(getCriteriumBeoordeling(item.name))">
                    {{ getCriteriumBeoordeling(item.name) }} </span>
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

import { student_projects, school_criteria } from '@/dummydata/dummydata.js'

const route = useRoute(); // Get the current route
const rubricId = ref(route.params.rubric); // Access the dynamic route parameter

// Filter school_criteria based on rubricId
const data = ref([]);
if (rubricId.value) {
  const year = school_criteria.value[0].years.find(wp =>
    wp.courses.some(c => c.beoordelingscriteria === rubricId.value)
  );

  if (year) {
    const course = year.courses.find(c => c.beoordelingscriteria === rubricId.value);
    if (course) {
      console.log("course: ", course);
      data.value = course.criteria.map(item => ({
        name: item.name,
        verantwoording: item.verantwoording,
        linked_project: '',
        feedback: '', // Placeholder for feedback
        beoordeling: '', // Placeholder for beoordeling
      }));
    }
  }
}

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

const percentageCompleted = computed(() => {
  const total = data.value.length;
  if (total === 0) return 0;
  const completedCount = data.value.filter(item => getCriteriumBeoordeling(item.name) === 'Voldoende' || getCriteriumBeoordeling(item.name) === 'Goed').length;
  return Math.round((completedCount / total) * 100);
});

const percentageVoldoende = computed(() => {
  const total = data.value.length;
  if (total === 0) return 0;
  const voldoendeCount = data.value.filter(item => getCriteriumBeoordeling(item.name) === 'Voldoende').length;
  return Math.round((voldoendeCount / total) * 100);
});

const percentageGoed = computed(() => {
  const total = data.value.length;
  if (total === 0) return 0;
  const goedCount = data.value.filter(item => getCriteriumBeoordeling(item.name) === 'Goed').length;
  return Math.round((goedCount / total) * 100);
});

const percentageOnvoldoende = computed(() => {
  const total = data.value.length;
  if (total === 0) return 0;
  const onvoldoendeCount = data.value.filter(item => getCriteriumBeoordeling(item.name) === 'Onvoldoende').length;
  return Math.round((onvoldoendeCount / total) * 100);
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

// Donut chart styles
const getDonutStyle = (percentage, color) => {
  const rotation = (percentage / 100) * 360;
  return {
    background: `conic-gradient(${color} ${rotation}deg, #e9ecef ${rotation}deg)`,
  };
};

const getAttachedProjectsFromCriterium = (requiredCriteria) => {

  const uniqueProjects = new Set();
  Object.entries(student_projects.value).forEach(([key, project]) => {
    if (project.criteriaToReach && project.criteriaToReach.hasOwnProperty(requiredCriteria)) {
      uniqueProjects.add({ key, project });
    }
  });
  return Array.from(uniqueProjects);
};

const getCriteriumBeoordeling = (requiredCriterium) => {
  let grade = "";
  getAttachedProjectsFromCriterium(requiredCriterium).forEach(({ project }) => {
    const criteriumData = project.criteriaToReach[requiredCriterium];
    if (criteriumData) {
      const projectGrade = criteriumData.grade;
      if (!grade || (grade === "Onvoldoende" && projectGrade === "Voldoende") ||
        (grade !== "Goed" && projectGrade === "Goed")) {
        grade = projectGrade;
      }
      
    }
  });
  return grade;
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
