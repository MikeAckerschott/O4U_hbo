<template>
  <div class="container-fluid mt-5">
    <div class="row mb-3">
      <div class="col-md-2">
        <label for="WerkprocesFilter" class="form-label">Selected Workprocess:</label>
        <select id="WerkprocesFilter" v-model="currentWerkProces" @change="filterData" class="form-select">
          <!-- <option v-for="beoordeling in uniqueBeoordelingen" :key="beoordeling" :value="beoordeling">{{ beoordeling }}          </option> -->
          <option v-for="werkproces, index in data[0].werkproces" :key="werkproces" :value="index">{{
            werkproces.description }}</option>
        </select>
      </div>
      <div class="col-md-6 text-end">
        <div class="d-flex justify-content-end">
          <div class="chart-container me-3">
            <div class="donut-chart" :style="getDonutStyle(percentageCompleted, '#007bff')"></div>
            <div class="chart-label">Completed</div>
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
                <th @click="sort('beoordelingscriteria')" class="cursor-pointer">
                  Course
                  <i :class="getSortIcon('beoordelingscriteria')"></i>
                </th>

                <th @click="sort('beoordeling')" class="cursor-pointer">
                  Beoordeling
                  <i :class="getSortIcon('beoordeling')"></i>
                </th>
                <th @click="" class="cursor-pointer">
                  Linked projects
                  <i></i>
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in paginatedData" :key="item.id" :class="getRowClass(item.criteria)">
                <td>
                  <RouterLink class="nav-link" :to="`/rubric/${item.beoordelingscriteria}`">
                    {{ item.beoordelingscriteria }}
                  </RouterLink>
                </td>
                <td>
                  <template
                    v-for="(val, index) in [completionCriteriaTracker(getAttachedProjectsFromCourse(item.beoordelingscriteria), item.beoordelingscriteria)]"
                    :key="index">
                    <div class="progress" style="height: 20px;">
                      <div class="progress-bar" role="progressbar"
                        :style="{ width: Math.max((val / item.criteria.length * 100), 10) + '%' }"
                        :class="getBadgeClass(val / item.criteria.length)" :aria-valuenow="val / item.criteria.length"
                        aria-valuemin="0" aria-valuemax="100">
                        {{ val }} / {{ item.criteria.length }}
                      </div>
                    </div>
                  </template>
                </td>
                <td>
                  <p v-for="project in getAttachedProjectsFromCourse(item.beoordelingscriteria)">{{ project.key }}</p>
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

import student_projects from '@/dummydata/studentProjects.js'; // Import student projects data
import school_criteria from '@/dummydata/courseCriteria.js'; // Import course criteria data

const data = ref(school_criteria); // Initialize data with student projects

console.log("DATA", data.value)

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
  // console.log(data.value[0].werkproces);
  const sorted = [...data.value[0].werkproces[currentWerkProces.value].criteria];
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


const filterData = () => {
  currentPage.value = 1;
};

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
  }
};

const getRowClass = (beoordeling) => {
  // let beoordelingPercentage = completionCriteriaTracker(beoordeling) / beoordeling.length * 100;
  // if (beoordelingPercentage == 100) {
  //   return 'table-success';
  // } else if (beoordelingPercentage >= 60) {
  //   return 'table-info';
  // } else if (beoordelingPercentage >= 40) {
  //   return 'table-warning';
  // } else {
  //   return 'table-danger';
  // }
};

const completionCriteriaTracker = (projects, course) => {
  console.log("projects: ", projects);
  console.log("course: ", course);

  // Access the correct werkproces and find the course by beoordelingscriteria
  const werkproces = school_criteria.value[0].werkproces[currentWerkProces.value];
  if (!werkproces) {
    console.error(`Werkproces at index ${currentWerkProces.value} not found.`);
    return 0;
  }

  const courseData = werkproces.criteria.find(item => item.beoordelingscriteria === course);
  if (!courseData) {
    console.error(`Course with beoordelingscriteria "${course}" not found.`);
    return 0;
  }

  // Extract the criteria for the course
  const courseCriteria = courseData.criteria;

  // Check if criteria in projects match the course criteria and if the grade is "Voldoende" or "Goed"
  let completedCount = 0;
  for (let project of projects) {
    if (project.project.criteriaToReach) {
      for (let criterium of Object.keys(project.project.criteriaToReach)) {
        if (
          courseCriteria.some(c => c.project === criterium) &&
          ["Voldoende", "Goed"].includes(project.project.criteriaToReach[criterium].grade)
        ) {
          completedCount++;
        }
      }
    }
  }

  return completedCount;
};

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





// console.log(getAttachedProjects("WoR-P-2"));


const getBadgeClass = (completion) => {
  completion *= 100;
  if (completion == 100) {
    return 'badge bg-success';
  } else if (completion >= 60) {
    return 'badge bg-info';
  } else if (completion >= 40) {
    return 'badge bg-warning';
  } else {
    return 'badge bg-danger';
  }
};

// Make currentWerkProces reactive
const currentWerkProces = ref(2); // Wrap currentWerkProces in a ref

const incrementWerkProces = () => {
  currentWerkProces.value += 1;
  // console.log(currentWerkProces.value);
};

const uniqueFases = computed(() => {
  // console.log(data.value[0].werkproces[currentWerkProces.value].criteria)
  const fases = data.value[0].werkproces[currentWerkProces.value].criteria.map(item => item.fase);
  return [...new Set(fases)];
});

const uniqueBeoordelingen = computed(() => {
  const beoordelingen = data.value[0].werkproces[currentWerkProces.value].criteria.map(item => item.beoordeling);
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

// Calculate percentages
const percentageCompleted = computed(() => {
  // const completed = data.value[0].werkproces[currentWerkProces.value].criteria.filter(item => item.beoordeling === 'Goed' || item.beoordeling === 'Voldoende').length;
  // return ((completed / data.value[0].werkproces[currentWerkProces.value].criteria.length) * 100).toFixed(2);
  const completed = getTotalCompletedCriteria.value;
  const total = getTotalCriteria.value;
  return ((completed / total) * 100).toFixed(2);
});

// Donut chart styles
const getDonutStyle = (percentage, color) => {
  const rotation = (percentage / 100) * 360;
  return {
    background: `conic-gradient(${color} ${rotation}deg, #e9ecef ${rotation}deg)`,
  };
};

const getTotalCriteria = computed(() => {
  let totalCriteria = 0;
  data.value[0].werkproces[currentWerkProces.value].criteria.forEach(criteria => {
    totalCriteria += criteria.criteria.length;
  });
  console.log(totalCriteria);
  return totalCriteria;
});

const getTotalCompletedCriteria = computed(() => {
  let totalCompleted = 0;
  data.value[0].werkproces[currentWerkProces.value].criteria.forEach(criteria => {
    totalCompleted += criteria.criteria.filter(item => item.beoordeling === 'Goed' || item.beoordeling === 'Voldoende').length;
  });
  console.log(totalCompleted);
  return totalCompleted;
});

const getAttachedProjectsFromCriterium = (requiredCriteria) => {
  const uniqueProjects = new Set();
  Object.entries(student_projects.value).forEach(([key, project]) => {
    if (project.criteriaToReach && project.criteriaToReach.hasOwnProperty(requiredCriteria)) {
      uniqueProjects.add({ key, project });
    }
  });
  return Array.from(uniqueProjects);
};

//Input Course - WoR-Pr
const getAttachedProjectsFromCourse = (course) => {
  const courseData = data.value[0].werkproces[currentWerkProces.value].criteria.find(item => item.beoordelingscriteria === course);

  if (!courseData) return [];

  const attachedProjects = new Map();

  courseData.criteria.forEach(criteria => {
    getAttachedProjectsFromCriterium(criteria.project).forEach(project => {
      attachedProjects.set(project.key, project);
    });
  });

  return Array.from(attachedProjects.values());
};

console.log("attached Projects course: ", getAttachedProjectsFromCourse("WoR-Pr"));
console.log("attached Projects criterium: ", getAttachedProjectsFromCriterium("WoR-P-1"));
</script>

<style>
.chart-container {
  width: 100px;
  height: 100px;
  position: relative;
}

.donut-chart {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: conic-gradient(#007bff 0deg, #e9ecef 0deg);
}

.chart-label {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-weight: bold;
}
</style>
