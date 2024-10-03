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
  { id: 1, project: '25192', verantwoording: 'its me  ', fase: 'Ontwikkeling', feedback: 'bleh', beoordeling: 'Goed'},
  {id: 2, project: '25194', verantwoording: 'check de receipts', fase: 'Uitvoerings', feedback: 'prima', beoordeling: 'Voldoende'},
  {
    id: 3,
    project: '25191',
    verantwoording: 'heb goed mn best gedaan',
    fase: 'Afronding',
    feedback: 'wtf u doing',
    beoordeling: 'Onvoldoende'
  },
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
