<template>
  <div class="container-fluid mt-5">
    <div class="row mb-3">
      <div class="col-md-2">
        <label for="WerkprocesFilter" class="form-label">Jaar:</label>
        <select id="WerkprocesFilter" v-model="currentWerkProces" @change="filterData" class="form-select">
          <option v-for="year, index in data[0].years" :key="year" :value="index">{{
            year.description }}</option>
        </select>
      </div>
      <div class="col-md-6 text-end">
        <div class="d-flex justify-content-end">
          <div class="chart-container me-3">
            <div class="donut-chart" :style="getDonutStyle(percentageCompleted, '#007bff')"></div>
            <div class="chart-label">Voltooid</div>
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
                  Voortgang
                  <i :class="getSortIcon('beoordeling')"></i>
                </th>
                <th @click="" class="cursor-pointer">
                  Projecten
                  <i></i>
                </th>

              </tr>
            </thead>
            <tbody>
              <!-- GET INFORMATION FOR EACH COURSE -->
              <CourseTableRow v-for="item in paginatedData" :key="item.id" :course="item"
                :getAttachedProjectsFromCourse="getAttachedProjectsFromCourse"
                :completionCriteriaTracker="completionCriteriaTracker" :getRowClass="getRowClass"
                :getBadgeClass="getBadgeClass" />
              <!-- INFORMATION FOR EACH COURSE -->
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

import { student_projects, school_criteria } from '@/dummydata/dummydata.js'
import CourseTableRow from '@/components/CourseTableRow.vue'; // Import the new component


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
  const sorted = [...data.value[0].years[currentWerkProces.value].courses];
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

const getRowClass = () => {
  // This function is unused and can be safely removed.
};

const completionCriteriaTracker = (projects, course) => {
  const year = school_criteria.value[0]?.years[currentWerkProces.value];
  if (!year) {
    console.error(`Werkproces at index ${currentWerkProces.value} not found.`);
    return 0;
  }

  const courseData = year.courses.find(item => item.beoordelingscriteria === course);
  if (!courseData) {
    console.error(`Course with beoordelingscriteria "${course}" not found.`);
    return 0;
  }

  const courseCriteria = getCriteriaFromCourse(course);
  if (!courseCriteria) {
    console.error(`Criteria for course "${course}" not found.`);
    return 0;
  }

  const completedCriteria = new Set();
  projects.forEach(project => {
    const criteriaToReach = project.project.criteriaToReach;
    if (criteriaToReach) {
      Object.entries(criteriaToReach).forEach(([criterium, details]) => {
        if (["Voldoende", "Goed"].includes(details.grade)) {
          if (courseCriteria.criteria.some(courseCriterium => courseCriterium.name === criterium)) {
            completedCriteria.add(criterium);
          }
        }
      });
    }
  });

  return completedCriteria.size;
};

const getCriteriaDescription = (criterion) => {
  const allCriteria = school_criteria.value.flatMap(studentProgress =>
    studentProgress.years.flatMap(course =>
      course.criteria.flatMap(courseCriteria => courseCriteria.criteria)
    )
  );

  const foundCriterion = allCriteria.find(courseCriteria => courseCriteria.name === criterion);
  return foundCriterion ? foundCriterion.verantwoording : '';
};


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

const currentWerkProces = ref(0);

const incrementWerkProces = () => {
  currentWerkProces.value += 1;
};

const uniqueFases = computed(() => {
  const fases = data.value[0].years[currentWerkProces.value].criteria.map(item => item.fase);
  return [...new Set(fases)];
});

const uniqueBeoordelingen = computed(() => {
  const beoordelingen = data.value[0].years[currentWerkProces.value].criteria.map(item => item.beoordeling);
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

const percentageCompleted = computed(() => {
  const completed = getTotalCompletedCriteria.value;
  const total = getTotalCriteria.value;
  return ((completed / total) * 100).toFixed(2);
});

const getDonutStyle = (percentage, color) => {
  const rotation = (percentage / 100) * 360;
  return {
    background: `conic-gradient(${color} ${rotation}deg, #e9ecef ${rotation}deg)`,
  };
};

const getTotalCriteria = computed(() => {
  let totalCriteria = 0;
  data.value[0].years[currentWerkProces.value].courses.forEach(criteria => {
    totalCriteria += criteria.criteria.length;
  });
  return totalCriteria;
});

const getTotalCompletedCriteria = computed(() => {
  let completedCriteria = 0;
  data.value[0].years[currentWerkProces.value].courses.forEach(course => {
    const projects = getAttachedProjectsFromCourse(course.beoordelingscriteria);
    const completed = completionCriteriaTracker(projects, course.beoordelingscriteria);
    if (completed === course.criteria.length) {
      completedCriteria += course.criteria.length;
    }
  });
  return completedCriteria;
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
  const courseData = data.value[0].years[currentWerkProces.value].courses.find(item => item.beoordelingscriteria === course);

  if (!courseData) return [];

  const attachedProjects = new Map();

  courseData.criteria.forEach(criteria => {
    getAttachedProjectsFromCriterium(criteria.name).forEach(project => {
      attachedProjects.set(project.key, project);
    });
  });

  return Array.from(attachedProjects.values());
};

const getCriteriaFromCourse = (course) => {
  return data.value[0].years[currentWerkProces.value].courses.find(item => item.beoordelingscriteria === course);
}

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
