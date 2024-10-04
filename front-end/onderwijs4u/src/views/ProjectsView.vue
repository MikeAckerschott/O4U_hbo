<template>
  <div class="container-fluid mt-5">
    <div class="row mb-3">
      <div class="col-md-3">
        <label for="periodeFilter" class="form-label">Filter by Periode:</label>
        <select id="periodeFilter" v-model="selectedPeriode" @change="filterData" class="form-select">
          <option value="">All</option>
          <option v-for="periode in uniquePeriodes" :key="periode" :value="periode">{{ periode }}</option>
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
                <th @click="sort('periode')" class="cursor-pointer">
                  Periode
                  <i :class="getSortIcon('periode')"></i>
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in paginatedData" :key="item.id">
                <td>
                  <RouterLink class="nav-link" :to="`/project/${item.project}`">
                    {{ item.project }}
                  </RouterLink>
                </td>
                <td>{{ item.periode }}</td>
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
  {id: 1, project: 'slagersportaal', periode: '1'},
  {id: 2, project: 'gaming', periode: '2'},
  // Add more data as needed
]);

const sortKey = ref('');
const sortOrder = ref('asc');
const selectedPeriode = ref('');
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

const uniquePeriodes = computed(() => {
  const periodes = data.value.map(item => item.periode);
  return [...new Set(periodes)];
});

const filteredData = computed(() => {
  let filtered = sortedData.value;
  if (selectedPeriode.value) {
    filtered = filtered.filter(item => item.periode === selectedPeriode.value);
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
</script>

