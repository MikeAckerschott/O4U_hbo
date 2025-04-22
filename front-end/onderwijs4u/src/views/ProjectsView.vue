<template>
  <div class="container-fluid mt-5">
    <div class="row mb-3">
      <div class="col-md-3">

      </div>
    </div>
    <div class="row">
      <div class="col-12">
        <div class="table-responsive">
          <table class="table table-striped table-hover table-bordered w-100">
            <thead class="bg-light-blue text-white">
              <tr>
                <th class="cursor-pointer">
                  Project
                  <i :class="getSortIcon('project')"></i>
                </th>
                <th class="cursor-pointer">
                  Beschrijving
                  <i :class="getSortIcon('periode')"></i>
                </th>
                <th class="cursor-pointer">
                  Status
                  <i :class="getSortIcon('periode')"></i>
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in paginatedData" :key="item.id"
                :class="{ 'table-info': item.running === true, 'table-success': item.running === false }">
                <td>
                  <RouterLink class="nav-link text-truncate" :to="`/project/${item.projectName}`"
                    style="white-space: nowrap;">
                    {{ item.projectName }}
                  </RouterLink>
                </td>
                <td>{{ item.description }}</td>
                <td><span
                    :class="{ 'badge bg-info': item.running === true, 'badge bg-success': item.running === false }">
                    {{ item.running === true ? 'Draaiend' : 'Afgerond' }}
                  </span>
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

import { student_projects } from '@/dummydata/dummydata.js'

// const data = ref([
//   { id: 1, project: 'slagersportaal', periode: '1' },
//   { id: 2, project: 'gaming', periode: '2' },
//   // Add more data as needed
// ]);

const data = ref(student_projects);

console.log("DATA: ", data.value)
// console.log("PROJECTRS: ",projects.value)

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
  console.log(data.value);
  const entries = Object.entries(data.value); // Convert object to array of key-value pairs
  if (sortKey.value) {
    entries.sort(([, a], [, b]) => {
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
  return Object.fromEntries(entries); // Convert back to an object
});

const filteredData = computed(() => {
  let filtered = Object.entries(sortedData.value); // Convert to array for filtering
  if (selectedPeriode.value) {
    filtered = filtered.filter(([, value]) => value.periode === selectedPeriode.value);
  }
  return Object.fromEntries(filtered); // Convert back to an object
});

const totalPages = computed(() => {
  const totalItems = Object.keys(filteredData.value).length; // Get the total number of items
  return Math.ceil(totalItems / itemsPerPage); // Calculate the total number of pages
});

const paginatedData = computed(() => {
  const entries = Object.entries(filteredData.value); // Convert to array for pagination
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return entries.slice(start, end).map(([key, value]) => {
    return { projectName: key, ...value }; // Include the key as part of the object
  });
});

console.log("ITERATING: ", paginatedData.value)

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
  }
};
</script>
