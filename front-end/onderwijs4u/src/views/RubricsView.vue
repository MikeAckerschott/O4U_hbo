<template>
  <div class="container-fluid mt-5">
    <div class="row mb-3">
      <div class="col-md-2">
        <label for="faseFilter" class="form-label">Filter by Fase:</label>
        <select id="faseFilter" v-model="selectedFase" @change="filterData" class="form-select">
          <option value="">All</option>
          <option v-for="fase in uniqueFases" :key="fase" :value="fase">{{ fase }}</option>
        </select>
      </div>
      <div class="col-md-2">
        <label for="beoordelingFilter" class="form-label">Filter by Beoordeling:</label>
        <select id="beoordelingFilter" v-model="selectedBeoordeling" @change="filterData" class="form-select">
          <option value="">All</option>
          <option v-for="beoordeling in uniqueBeoordelingen" :key="beoordeling" :value="beoordeling">{{ beoordeling }}
          </option>
        </select>
      </div>
      <div class="col-md-2">
        <label for="WerkprocesFilter" class="form-label">Selected Workprocess:</label>
        <select id="WerkprocesFilter" v-model="currentWerkProces" @change="filterData" class="form-select">
          <!-- <option v-for="beoordeling in uniqueBeoordelingen" :key="beoordeling" :value="beoordeling">{{ beoordeling }}          </option> -->
          <option v-for="werkproces, index in data[0].werkproces" :key="werkproces" :value="index">{{ werkproces.description }}</option>
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
                <th @click="sort('beoordelingscriteria')" class="cursor-pointer">
                  Criteria
                  <i :class="getSortIcon('beoordelingscriteria')"></i>
                </th>
                <th @click="sort('fase')" class="cursor-pointer">
                  Fase
                  <i :class="getSortIcon('fase')"></i>
                </th>
                <th @click="sort('beoordeling')" class="cursor-pointer">
                  Beoordeling
                  <i :class="getSortIcon('beoordeling')"></i>
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in paginatedData" :key="item.id" :class="getRowClass(item.beoordeling)">
                <td>
                  <RouterLink class="nav-link" :to="`/rubric/${item.beoordelingscriteria}`">
                    {{ item.beoordelingscriteria }}
                  </RouterLink>
                </td>
                <td>{{ item.fase }}</td>
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
  {
    werkproces: [
      {
        description: "Werkproces 1 Description",
        criteria: [
          { id: 1, beoordelingscriteria: '25192', fase: 'Ontwikkeling', beoordeling: 'Goed' },
          { id: 2, beoordelingscriteria: '25194', fase: 'Uitvoerings', beoordeling: 'Voldoende' },
          { id: 3, beoordelingscriteria: '25191', fase: 'Afronding', beoordeling: 'Onvoldoende' },
          { id: 4, beoordelingscriteria: '25193', fase: 'Voorbereiding', beoordeling: 'Onvoldoende' },
          { id: 5, beoordelingscriteria: '25195', fase: 'Ontwikkeling', beoordeling: 'Goed' },
          { id: 6, beoordelingscriteria: '25196', fase: 'Uitvoerings', beoordeling: 'Voldoende' },
          { id: 7, beoordelingscriteria: '25197', fase: 'Afronding', beoordeling: 'Goed' },
          { id: 8, beoordelingscriteria: '25198', fase: 'Voorbereiding', beoordeling: 'Onvoldoende' },
          { id: 9, beoordelingscriteria: '25199', fase: 'Ontwikkeling', beoordeling: 'Voldoende' },
          { id: 10, beoordelingscriteria: '25200', fase: 'Uitvoerings', beoordeling: 'Goed' },
          { id: 11, beoordelingscriteria: '25201', fase: 'Afronding', beoordeling: 'Onvoldoende' },
          { id: 12, beoordelingscriteria: '25202', fase: 'Voorbereiding', beoordeling: 'Voldoende' },
          { id: 13, beoordelingscriteria: '25203', fase: 'Ontwikkeling', beoordeling: 'Goed' },
          { id: 14, beoordelingscriteria: '25204', fase: 'Uitvoerings', beoordeling: 'Onvoldoende' },
          { id: 15, beoordelingscriteria: '25205', fase: 'Afronding', beoordeling: 'Voldoende' },
          { id: 16, beoordelingscriteria: '25206', fase: 'Voorbereiding', beoordeling: 'Goed' },
          { id: 17, beoordelingscriteria: '25207', fase: 'Ontwikkeling', beoordeling: 'Onvoldoende' },
          { id: 18, beoordelingscriteria: '25208', fase: 'Uitvoerings', beoordeling: 'Voldoende' },
          { id: 19, beoordelingscriteria: '25209', fase: 'Afronding', beoordeling: 'Goed' },
          { id: 20, beoordelingscriteria: '25210', fase: 'Voorbereiding', beoordeling: 'Onvoldoende' }
        ]
      },
      {
        description: "Werkproces 2 Description",
        criteria: [
          { id: 1, beoordelingscriteria: '26192', fase: 'Ontwikkeling', beoordeling: 'Goed' },
          { id: 2, beoordelingscriteria: '26194', fase: 'Uitvoerings', beoordeling: 'Goed' },
          { id: 3, beoordelingscriteria: '26191', fase: 'Afronding', beoordeling: 'Goed' },
          { id: 4, beoordelingscriteria: '26193', fase: 'Voorbereiding', beoordeling: 'Goed' },
          { id: 5, beoordelingscriteria: '26195', fase: 'Ontwikkeling', beoordeling: 'Goed' },
          { id: 6, beoordelingscriteria: '26196', fase: 'Uitvoerings', beoordeling: 'Goed' },
          { id: 7, beoordelingscriteria: '26197', fase: 'Afronding', beoordeling: 'Goed' },
          { id: 8, beoordelingscriteria: '26198', fase: 'Voorbereiding', beoordeling: 'Onvoldoende' },
          { id: 9, beoordelingscriteria: '26199', fase: 'Ontwikkeling', beoordeling: 'Voldoende' },
          { id: 10, beoordelingscriteria: '26200', fase: 'Uitvoerings', beoordeling: 'Goed' },
          { id: 11, beoordelingscriteria: '26201', fase: 'Afronding', beoordeling: 'Onvoldoende' },
          { id: 12, beoordelingscriteria: '26202', fase: 'Voorbereiding', beoordeling: 'Voldoende' },
          { id: 13, beoordelingscriteria: '26203', fase: 'Ontwikkeling', beoordeling: 'Goed' },
          { id: 14, beoordelingscriteria: '26204', fase: 'Uitvoerings', beoordeling: 'Onvoldoende' },
          { id: 15, beoordelingscriteria: '26205', fase: 'Afronding', beoordeling: 'Voldoende' },
          { id: 16, beoordelingscriteria: '26206', fase: 'Voorbereiding', beoordeling: 'Goed' },
          { id: 17, beoordelingscriteria: '26207', fase: 'Ontwikkeling', beoordeling: 'Onvoldoende' },
          { id: 18, beoordelingscriteria: '26208', fase: 'Uitvoerings', beoordeling: 'Voldoende' },
          { id: 19, beoordelingscriteria: '26209', fase: 'Afronding', beoordeling: 'Goed' },
          { id: 20, beoordelingscriteria: '26210', fase: 'Voorbereiding', beoordeling: 'Onvoldoende' }
        ]
      }
    ]
  }
]);


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

// Make currentWerkProces reactive
const currentWerkProces = ref(0); // Wrap currentWerkProces in a ref

const incrementWerkProces = () => {
  currentWerkProces.value += 1;
  console.log(currentWerkProces.value);
};

const uniqueFases = computed(() => {
  console.log(data.value[0].werkproces[currentWerkProces.value].criteria)
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
  const completed = data.value[0].werkproces[currentWerkProces.value].criteria.filter(item => item.beoordeling === 'Goed' || item.beoordeling === 'Voldoende').length;
  return ((completed / data.value[0].werkproces[currentWerkProces.value].criteria.length) * 100).toFixed(2);
});

const percentageVoldoende = computed(() => {
  const voldoende = data.value[0].werkproces[currentWerkProces.value].criteria.filter(item => item.beoordeling === 'Voldoende').length;
  return ((voldoende / data.value[0].werkproces[currentWerkProces.value].criteria.length) * 100).toFixed(2);
});

const percentageGoed = computed(() => {
  const goed = data.value[0].werkproces[currentWerkProces.value].criteria.filter(item => item.beoordeling === 'Goed').length;
  return ((goed / data.value[0].werkproces[currentWerkProces.value].criteria.length) * 100).toFixed(2);
});

const percentageOnvoldoende = computed(() => {
  const onvoldoende = data.value[0].werkproces[currentWerkProces.value].criteria.filter(item => item.beoordeling === 'Onvoldoende').length;
  return ((onvoldoende / data.value[0].werkproces[currentWerkProces.value].criteria.length) * 100).toFixed(2);
});

// Donut chart styles
const getDonutStyle = (percentage, color) => {
  const rotation = (percentage / 100) * 360;
  return {
    background: `conic-gradient(${color} ${rotation}deg, #e9ecef ${rotation}deg)`,
  };
};
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
