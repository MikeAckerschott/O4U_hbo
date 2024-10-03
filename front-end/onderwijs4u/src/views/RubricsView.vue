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
      <div class="col-md-9 text-end">
        <div>
          <strong>Percentage Completed:</strong> {{ percentageCompleted }}%
        </div>
        <div>
          <strong>Percentage Voldoende:</strong> {{ percentageVoldoende }}%
        </div>
        <div>
          <strong>Percentage Goed:</strong> {{ percentageGoed }}%
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
                Rubrics
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
import {ref, computed} from 'vue';

const data = ref([
  {id: 1, beoordelingscriteria: '25192', fase: 'Ontwikkeling', beoordeling: 'Goed'},
  {id: 2, beoordelingscriteria: '25194', fase: 'Uitvoerings', beoordeling: 'Voldoende'},
  {id: 3, beoordelingscriteria: '25191', fase: 'Afronding', beoordeling: 'Onvoldoende'},
  {id: 4, beoordelingscriteria: '25193', fase: 'Voorbereiding', beoordeling: 'Onvoldoende'},
  {id: 5, beoordelingscriteria: '25195', fase: 'Ontwikkeling', beoordeling: 'Goed'},
  {id: 6, beoordelingscriteria: '25196', fase: 'Uitvoerings', beoordeling: 'Voldoende'},
  {id: 7, beoordelingscriteria: '25197', fase: 'Afronding', beoordeling: 'Goed'},
  {id: 8, beoordelingscriteria: '25198', fase: 'Voorbereiding', beoordeling: 'Onvoldoende'},
  {id: 9, beoordelingscriteria: '25199', fase: 'Ontwikkeling', beoordeling: 'Voldoende'},
  {id: 10, beoordelingscriteria: '25200', fase: 'Uitvoerings', beoordeling: 'Goed'},
  {id: 11, beoordelingscriteria: '25201', fase: 'Afronding', beoordeling: 'Onvoldoende'},
  {id: 12, beoordelingscriteria: '25202', fase: 'Voorbereiding', beoordeling: 'Voldoende'},
  {id: 13, beoordelingscriteria: '25203', fase: 'Ontwikkeling', beoordeling: 'Goed'},
  {id: 14, beoordelingscriteria: '25204', fase: 'Uitvoerings', beoordeling: 'Onvoldoende'},
  {id: 15, beoordelingscriteria: '25205', fase: 'Afronding', beoordeling: 'Voldoende'},
  {id: 16, beoordelingscriteria: '25206', fase: 'Voorbereiding', beoordeling: 'Goed'},
  {id: 17, beoordelingscriteria: '25207', fase: 'Ontwikkeling', beoordeling: 'Onvoldoende'},
  {id: 18, beoordelingscriteria: '25208', fase: 'Uitvoerings', beoordeling: 'Voldoende'},
  {id: 19, beoordelingscriteria: '25209', fase: 'Afronding', beoordeling: 'Goed'},
  {id: 20, beoordelingscriteria: '25210', fase: 'Voorbereiding', beoordeling: 'Onvoldoende'},
  {id: 21, beoordelingscriteria: '25211', fase: 'Ontwikkeling', beoordeling: 'Voldoende'},
  {id: 22, beoordelingscriteria: '25212', fase: 'Uitvoerings', beoordeling: 'Goed'},
  {id: 23, beoordelingscriteria: '25213', fase: 'Afronding', beoordeling: 'Onvoldoende'},
  {id: 24, beoordelingscriteria: '25214', fase: 'Voorbereiding', beoordeling: 'Voldoende'},
  {id: 25, beoordelingscriteria: '25215', fase: 'Ontwikkeling', beoordeling: 'Goed'},
  {id: 26, beoordelingscriteria: '25216', fase: 'Uitvoerings', beoordeling: 'Onvoldoende'},
  {id: 27, beoordelingscriteria: '25217', fase: 'Afronding', beoordeling: 'Voldoende'},
  {id: 28, beoordelingscriteria: '25218', fase: 'Voorbereiding', beoordeling: 'Goed'},
  {id: 29, beoordelingscriteria: '25219', fase: 'Ontwikkeling', beoordeling: 'Onvoldoende'},
  {id: 30, beoordelingscriteria: '25220', fase: 'Uitvoerings', beoordeling: 'Voldoende'},
  {id: 31, beoordelingscriteria: '25221', fase: 'Afronding', beoordeling: 'Goed'},
  {id: 32, beoordelingscriteria: '25222', fase: 'Voorbereiding', beoordeling: 'Onvoldoende'},
  {id: 33, beoordelingscriteria: '25223', fase: 'Ontwikkeling', beoordeling: 'Voldoende'},
  {id: 34, beoordelingscriteria: '25224', fase: 'Uitvoerings', beoordeling: 'Goed'},
  {id: 35, beoordelingscriteria: '25225', fase: 'Afronding', beoordeling: 'Onvoldoende'},
  {id: 36, beoordelingscriteria: '25226', fase: 'Voorbereiding', beoordeling: 'Voldoende'},
  {id: 37, beoordelingscriteria: '25227', fase: 'Ontwikkeling', beoordeling: 'Goed'},
  {id: 38, beoordelingscriteria: '25228', fase: 'Uitvoerings', beoordeling: 'Onvoldoende'},
  {id: 39, beoordelingscriteria: '25229', fase: 'Afronding', beoordeling: 'Voldoende'},
  {id: 40, beoordelingscriteria: '25230', fase: 'Voorbereiding', beoordeling: 'Goed'},
]);

const sortKey = ref('');
const sortOrder = ref('asc');
const selectedFase = ref('');
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

const filteredData = computed(() => {
  if (!selectedFase.value) return sortedData.value;
  return sortedData.value.filter(item => item.fase === selectedFase.value);
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

// Calculate percentages
const percentageCompleted = computed(() => {
  const completed = data.value.filter(item => item.beoordeling === 'Goed' || item.beoordeling === 'Voldoende').length;
  return ((completed / data.value.length) * 100).toFixed(2);
});

const percentageVoldoende = computed(() => {
  const voldoende = data.value.filter(item => item.beoordeling === 'Voldoende').length;
  return ((voldoende / data.value.length) * 100).toFixed(2);
});

const percentageGoed = computed(() => {
  const goed = data.value.filter(item => item.beoordeling === 'Goed').length;
  return ((goed / data.value.length) * 100).toFixed(2);
});
</script>
