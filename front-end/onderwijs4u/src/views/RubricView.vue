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
          <option v-for="beoordeling in uniqueBeoordelingen" :key="beoordeling" :value="beoordeling">{{ beoordeling }}</option>
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
                <td>{{ item.project }}</td>
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
import { ref, computed } from 'vue';

const data = ref([
  { id: 1, project: '25192', verantwoording: 'I worked diligently on this project, ensuring all requirements were met.', fase: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
  { id: 2, project: '25194', verantwoording: 'I collaborated effectively with my team to complete the tasks.', fase: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
  { id: 3, project: '25191', verantwoording: 'I struggled with some concepts but sought help to understand them.', fase: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
  { id: 4, project: '25193', verantwoording: 'I planned the project timeline and followed it closely.', fase: 'Voorbereiding', feedback: 'Planning was good, but execution lacked.', beoordeling: 'Onvoldoende' },
  { id: 5, project: '25195', verantwoording: 'I applied innovative solutions to solve problems.', fase: 'Ontwikkeling', feedback: 'Creative approach and well-executed.', beoordeling: 'Goed' },
  { id: 6, project: '25196', verantwoording: 'I ensured all tasks were completed on time.', fase: 'Uitvoerings', feedback: 'Timely completion, but quality can improve.', beoordeling: 'Voldoende' },
  { id: 7, project: '25197', verantwoording: 'I reviewed all work thoroughly before submission.', fase: 'Afronding', feedback: 'Thorough review, excellent quality.', beoordeling: 'Goed' },
  { id: 8, project: '25198', verantwoording: 'I faced challenges but overcame them with persistence.', fase: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
  { id: 9, project: '25199', verantwoording: 'I balanced multiple tasks efficiently.', fase: 'Ontwikkeling', feedback: 'Efficient multitasking, but some errors present.', beoordeling: 'Voldoende' },
  { id: 10, project: '25200', verantwoording: 'I took initiative to lead the project.', fase: 'Uitvoerings', feedback: 'Strong leadership, well done.', beoordeling: 'Goed' },
  { id: 11, project: '25201', verantwoording: 'I learned from my mistakes and improved.', fase: 'Afronding', feedback: 'Improvement noted, but more effort needed.', beoordeling: 'Onvoldoende' },
  { id: 12, project: '25202', verantwoording: 'I prepared thoroughly for each phase.', fase: 'Voorbereiding', feedback: 'Good preparation, but execution needs work.', beoordeling: 'Voldoende' },
  { id: 13, project: '25203', verantwoording: 'I integrated feedback to enhance my work.', fase: 'Ontwikkeling', feedback: 'Feedback integration was excellent.', beoordeling: 'Goed' },
  { id: 14, project: '25204', verantwoording: 'I encountered difficulties but sought solutions.', fase: 'Uitvoerings', feedback: 'Needs better problem-solving skills.', beoordeling: 'Onvoldoende' },
  { id: 15, project: '25205', verantwoording: 'I ensured all deliverables met the criteria.', fase: 'Afronding', feedback: 'Criteria met, good job.', beoordeling: 'Voldoende' },
  { id: 16, project: '25206', verantwoording: 'I organized my tasks effectively.', fase: 'Voorbereiding', feedback: 'Well-organized, excellent work.', beoordeling: 'Goed' },
  { id: 17, project: '25207', verantwoording: 'I faced setbacks but remained focused.', fase: 'Ontwikkeling', feedback: 'Focus is good, but results need improvement.', beoordeling: 'Onvoldoende' },
  { id: 18, project: '25208', verantwoording: 'I communicated regularly with my team.', fase: 'Uitvoerings', feedback: 'Good communication, but more clarity needed.', beoordeling: 'Voldoende' },
  { id: 19, project: '25209', verantwoording: 'I ensured high quality in all tasks.', fase: 'Afronding', feedback: 'High quality work, well done.', beoordeling: 'Goed' },
  { id: 20, project: '25210', verantwoording: 'I planned each step carefully.', fase: 'Voorbereiding', feedback: 'Planning was good, but execution lacked.', beoordeling: 'Onvoldoende' },
  { id: 21, project: '25211', verantwoording: 'I managed my time effectively.', fase: 'Ontwikkeling', feedback: 'Good time management, but some delays noted.', beoordeling: 'Voldoende' },
  { id: 22, project: '25212', verantwoording: 'I took responsibility for my tasks.', fase: 'Uitvoerings', feedback: 'Responsible and reliable, good job.', beoordeling: 'Goed' },
  { id: 23, project: '25213', verantwoording: 'I learned from feedback and improved.', fase: 'Afronding', feedback: 'Improvement noted, but more effort needed.', beoordeling: 'Onvoldoende' },
  { id: 24, project: '25214', verantwoording: 'I prepared all necessary materials.', fase: 'Voorbereiding', feedback: 'Materials prepared well, good job.', beoordeling: 'Voldoende' },
  { id: 25, project: '25215', verantwoording: 'I applied my knowledge effectively.', fase: 'Ontwikkeling', feedback: 'Knowledge application was excellent.', beoordeling: 'Goed' },
  { id: 26, project: '25216', verantwoording: 'I encountered issues but resolved them.', fase: 'Uitvoerings', feedback: 'Issue resolution needs improvement.', beoordeling: 'Onvoldoende' },
  { id: 27, project: '25217', verantwoording: 'I ensured all tasks were completed.', fase: 'Afronding', feedback: 'Tasks completed, but quality needs work.', beoordeling: 'Voldoende' },
  { id: 28, project: '25218', verantwoording: 'I organized my work efficiently.', fase: 'Voorbereiding', feedback: 'Well-organized, excellent work.', beoordeling: 'Goed' },
  { id: 29, project: '25219', verantwoording: 'I faced challenges but remained determined.', fase: 'Ontwikkeling', feedback: 'Determination is good, but results need improvement.', beoordeling: 'Onvoldoende' },
  { id: 30, project: '25220', verantwoording: 'I communicated effectively with my team.', fase: 'Uitvoerings', feedback: 'Good communication, but more clarity needed.', beoordeling: 'Voldoende' },
  { id: 31, project: '25221', verantwoording: 'I ensured high standards in my work.', fase: 'Afronding', feedback: 'High standards maintained, well done.', beoordeling: 'Goed' },
  { id: 32, project: '25222', verantwoording: 'I planned each phase meticulously.', fase: 'Voorbereiding', feedback: 'Planning was good, but execution lacked.', beoordeling: 'Onvoldoende' },
  { id: 33, project: '25223', verantwoording: 'I managed my workload effectively.', fase: 'Ontwikkeling', feedback: 'Good workload management, but some delays noted.', beoordeling: 'Voldoende' },
  { id: 34, project: '25224', verantwoording: 'I took initiative in my tasks.', fase: 'Uitvoerings', feedback: 'Strong initiative, well done.', beoordeling: 'Goed' },
  { id: 35, project: '25225', verantwoording: 'I learned from my mistakes.', fase: 'Afronding', feedback: 'Learning noted, but more effort needed.', beoordeling: 'Onvoldoende' },
  { id: 36, project: '25226', verantwoording: 'I prepared thoroughly for each task.', fase: 'Voorbereiding', feedback: 'Good preparation, but execution needs work.', beoordeling: 'Voldoende' },
  { id: 37, project: '25227', verantwoording: 'I applied feedback to improve my work.', fase: 'Ontwikkeling', feedback: 'Feedback application was excellent.', beoordeling: 'Goed' },
  { id: 38, project: '25228', verantwoording: 'I encountered difficulties but sought solutions.', fase: 'Uitvoerings', feedback: 'Needs better problem-solving skills.', beoordeling: 'Onvoldoende' },
  { id: 39, project: '25229', verantwoording: 'I ensured all deliverables met the criteria.', fase: 'Afronding', feedback: 'Criteria met, good job.', beoordeling: 'Voldoende' },
  { id: 40, project: '25230', verantwoording: 'I organized my tasks effectively.', fase: 'Voorbereiding', feedback: 'well done', beoordeling: 'Voldoende' }]);

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
