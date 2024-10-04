<template>
  <div class="container-fluid mt-5">
    <div class="row mb-3">
      <div class="col-md-3">
        <label for="faseFilter" class="form-label">Filter by Fase:</label>
        <select id="faseFilter" v-model="selectedFase" @change="filterData" class="form-select">
          <option value="">All</option>
          <option v-for="fase in uniqueFases" :key="fase" :value="fase">{{ fase }}</option>
        </select>
      </div>
      <div class="col-md-3">
        <label for="beoordelingFilter" class="form-label">Filter by Beoordeling:</label>
        <select id="beoordelingFilter" v-model="selectedBeoordeling" @change="filterData" class="form-select">
          <option value="">All</option>
          <option v-for="beoordeling in uniqueBeoordelingen" :key="beoordeling" :value="beoordeling">{{
              beoordeling
            }}
          </option>
        </select>
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
                Fase
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
              <td><RouterLink class="nav-link" :to="`/project/${item.project}`">
                {{ item.project }}
              </RouterLink></td>
              <td>{{ item.verantwoording }}</td>
              <td>{{ item.fase }}</td>
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
import {ref, computed} from 'vue';

const data = ref([
  {
    id: 1,
    project: 'Website Redesign',
    verantwoording: 'I worked diligently on this project, ensuring all requirements were met.',
    fase: 'Development',
    feedback: 'Excellent effort and attention to detail.',
    beoordeling: 'Goed'
  },
  {
    id: 2,
    project: 'Mobile App Development',
    verantwoording: 'I collaborated effectively with my team to complete the tasks.',
    fase: 'Development',
    feedback: 'Good teamwork, but some areas need improvement.',
    beoordeling: 'Voldoende'
  },
  {
    id: 3,
    project: 'Data Migration',
    verantwoording: 'I struggled with some concepts but sought help to understand them.',
    fase: 'Expert',
    feedback: 'Needs more understanding of key concepts.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 4,
    project: 'System Integration',
    verantwoording: 'I planned the project timeline and followed it closely.',
    fase: 'Oriëntatie',
    feedback: 'Planning was good, but execution lacked.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 5,
    project: 'Cloud Implementation',
    verantwoording: 'I applied innovative solutions to solve problems.',
    fase: 'Development',
    feedback: 'Creative approach and well-executed.',
    beoordeling: 'Goed'
  },
  {
    id: 6,
    project: 'Network Upgrade',
    verantwoording: 'I ensured all tasks were completed on time.',
    fase: 'Development',
    feedback: 'Timely completion, but quality can improve.',
    beoordeling: 'Voldoende'
  },
  {
    id: 7,
    project: 'Security Enhancement',
    verantwoording: 'I reviewed all work thoroughly before submission.',
    fase: 'Expert',
    feedback: 'Thorough review, excellent quality.',
    beoordeling: 'Goed'
  },
  {
    id: 8,
    project: 'Software Evaluation',
    verantwoording: 'I faced challenges but overcame them with persistence.',
    fase: 'Oriëntatie',
    feedback: 'Good effort, but more persistence needed.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 9,
    project: 'Database Optimization',
    verantwoording: 'I balanced multiple tasks efficiently.',
    fase: 'Development',
    feedback: 'Efficient multitasking, but some errors present.',
    beoordeling: 'Voldoende'
  },
  {
    id: 10,
    project: 'Project Management Tool',
    verantwoording: 'I took initiative to lead the project.',
    fase: 'Development',
    feedback: 'Strong leadership, well done.',
    beoordeling: 'Goed'
  },
  {
    id: 11,
    project: 'API Development',
    verantwoording: 'I learned from my mistakes and improved.',
    fase: 'Expert',
    feedback: 'Improvement noted, but more effort needed.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 12,
    project: 'IT Infrastructure Setup',
    verantwoording: 'I prepared thoroughly for each phase.',
    fase: 'Oriëntatie',
    feedback: 'Good preparation, but execution needs work.',
    beoordeling: 'Voldoende'
  },
  {
    id: 13,
    project: 'User Interface Design',
    verantwoording: 'I integrated feedback to enhance my work.',
    fase: 'Development',
    feedback: 'Feedback integration was excellent.',
    beoordeling: 'Goed'
  },
  {
    id: 14,
    project: 'Bug Fixing',
    verantwoording: 'I encountered difficulties but sought solutions.',
    fase: 'Development',
    feedback: 'Needs better problem-solving skills.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 15,
    project: 'Performance Testing',
    verantwoording: 'I ensured all deliverables met the criteria.',
    fase: 'Expert',
    feedback: 'Criteria met, good job.',
    beoordeling: 'Voldoende'
  },
  {
    id: 16,
    project: 'Technical Documentation',
    verantwoording: 'I organized my tasks effectively.',
    fase: 'Oriëntatie',
    feedback: 'Well-organized, excellent work.',
    beoordeling: 'Goed'
  },
  {
    id: 17,
    project: 'Feature Implementation',
    verantwoording: 'I faced setbacks but remained focused.',
    fase: 'Development',
    feedback: 'Focus is good, but results need improvement.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 18,
    project: 'Team Collaboration',
    verantwoording: 'I communicated regularly with my team.',
    fase: 'Development',
    feedback: 'Good communication, but more clarity needed.',
    beoordeling: 'Voldoende'
  },
  {
    id: 19,
    project: 'Quality Assurance',
    verantwoording: 'I ensured high quality in all tasks.',
    fase: 'Expert',
    feedback: 'High quality work, well done.',
    beoordeling: 'Goed'
  },
  {
    id: 20,
    project: 'Deployment Planning',
    verantwoording: 'I planned each step carefully.',
    fase: 'Oriëntatie',
    feedback: 'Planning was good, but execution lacked.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 20,
    project: 'Deployment Planning',
    verantwoording: 'I planned each step carefully.',
    fase: 'Oriëntatie',
    feedback: 'Planning was good, but execution lacked.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 21,
    project: 'Time Management System',
    verantwoording: 'I managed my time effectively.',
    fase: 'Development',
    feedback: 'Good time management, but some delays noted.',
    beoordeling: 'Voldoende'
  },
  {
    id: 22,
    project: 'Task Responsibility',
    verantwoording: 'I took responsibility for my tasks.',
    fase: 'Development',
    feedback: 'Responsible and reliable, good job.',
    beoordeling: 'Goed'
  },
  {
    id: 23,
    project: 'Feedback Improvement',
    verantwoording: 'I learned from feedback and improved.',
    fase: 'Expert',
    feedback: 'Improvement noted, but more effort needed.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 24,
    project: 'Material Preparation',
    verantwoording: 'I prepared all necessary materials.',
    fase: 'Oriëntatie',
    feedback: 'Materials prepared well, good job.',
    beoordeling: 'Voldoende'
  },
  {
    id: 25,
    project: 'Knowledge Application',
    verantwoording: 'I applied my knowledge effectively.',
    fase: 'Development',
    feedback: 'Knowledge application was excellent.',
    beoordeling: 'Goed'
  },
  {
    id: 26,
    project: 'Issue Resolution',
    verantwoording: 'I encountered issues but resolved them.',
    fase: 'Development',
    feedback: 'Issue resolution needs improvement.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 27,
    project: 'Task Completion',
    verantwoording: 'I ensured all tasks were completed.',
    fase: 'Expert',
    feedback: 'Tasks completed, but quality needs work.',
    beoordeling: 'Voldoende'
  },
  {
    id: 28,
    project: 'Work Organization',
    verantwoording: 'I organized my work efficiently.',
    fase: 'Oriëntatie',
    feedback: 'Well-organized, excellent work.',
    beoordeling: 'Goed'
  },
  {
    id: 29,
    project: 'Challenge Management',
    verantwoording: 'I faced challenges but remained determined.',
    fase: 'Development',
    feedback: 'Determination is good, but results need improvement.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 30,
    project: 'Team Communication',
    verantwoording: 'I communicated effectively with my team.',
    fase: 'Development',
    feedback: 'Good communication, but more clarity needed.',
    beoordeling: 'Voldoende'
  },
  {
    id: 31,
    project: 'High Standards Maintenance',
    verantwoording: 'I ensured high standards in my work.',
    fase: 'Expert',
    feedback: 'High standards maintained, well done.',
    beoordeling: 'Goed'
  },
  {
    id: 32,
    project: 'Phase Planning',
    verantwoording: 'I planned each phase meticulously.',
    fase: 'Oriëntatie',
    feedback: 'Planning was good, but execution lacked.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 33,
    project: 'Workload Management',
    verantwoording: 'I managed my workload effectively.',
    fase: 'Development',
    feedback: 'Good workload management, but some delays noted.',
    beoordeling: 'Voldoende'
  },
  {
    id: 34,
    project: 'Initiative Taking',
    verantwoording: 'I took initiative in my tasks.',
    fase: 'Development',
    feedback: 'Strong initiative, well done.',
    beoordeling: 'Goed'
  },
  {
    id: 35,
    project: 'Mistake Learning',
    verantwoording: 'I learned from my mistakes.',
    fase: 'Expert',
    feedback: 'Learning noted, but more effort needed.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 36,
    project: 'Task Preparation',
    verantwoording: 'I prepared thoroughly for each task.',
    fase: 'Oriëntatie',
    feedback: 'Good preparation, but execution needs work.',
    beoordeling: 'Voldoende'
  },
  {
    id: 37,
    project: 'Feedback Application',
    verantwoording: 'I applied feedback to improve my work.',
    fase: 'Development',
    feedback: 'Feedback application was excellent.',
    beoordeling: 'Goed'
  },
  {
    id: 38,
    project: 'Problem Solving',
    verantwoording: 'I encountered difficulties but sought solutions.',
    fase: 'Development',
    feedback: 'Needs better problem-solving skills.',
    beoordeling: 'Onvoldoende'
  },
  {
    id: 39,
    project: 'Deliverable Criteria',
    verantwoording: 'I ensured all deliverables met the criteria.',
    fase: 'Expert',
    feedback: 'Criteria met, good job.',
    beoordeling: 'Voldoende'
  },
  {
    id: 40,
    project: 'Task Organization',
    verantwoording: 'I organized my tasks effectively.',
    fase: 'Oriëntatie',
    feedback: 'Well done.',
    beoordeling: 'Voldoende'
  }]);

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
