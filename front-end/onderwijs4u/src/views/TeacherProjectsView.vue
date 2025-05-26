<template>
  <div class="container-fluid mt-5">
    <div class="row mb-3">
      <div class="col-md-3">
        <label for="statusFilter" class="form-label">Filter op Status:</label>
        <select id="statusFilter" v-model="selectedStatus" @change="filterData" class="form-select">
          <option value="active">Active</option>
          <option value="inactive">Inactive</option>
          <option value="">All</option>
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
                <th @click="sort('status')" class="cursor-pointer">
                  Status
                  <i :class="getSortIcon('status')"></i>
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in paginatedData" :key="item.id" :class="{
                'table-warning': item.status === 'awaiting',
                'table-info': item.status === 'active',
                'table-success': item.status === 'inactive'
              }">
                <td>
                  <RouterLink class="nav-link text-nowrap" :to="`/teacherproject/${item.projectName}`">
                    {{ item.projectName }}
                  </RouterLink>
                </td>
                <td>
                  <span :class="{
                    'badge text-bg-warning text-white': item.status === 'awaiting',
                    'badge text-bg-info text-white': item.status === 'active',
                    'badge text-bg-success text-white': item.status === 'inactive'
                  }">
                    {{ item.status === 'awaiting'
                      ? 'Afwachtend op goedkeuring'
                      : (item.status === 'active' ? 'Draaiend' : 'Afgerond') }}
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

<script>
import { student_projects } from '@/dummydata/dummydata.js';

export default {
  data() {
    // If student_projects is a ref, use .value
    const projectsRaw = student_projects.value ? student_projects.value : student_projects;

    // Only include actual project keys (filter out anything that's not an object with expected fields)
    const projectsArray = Object.entries(projectsRaw)
      .filter(([key, value]) => value && typeof value === 'object' && 'running' in value)
      .map(([key, value], idx) => ({
        id: idx + 1,
        projectName: key,
        ...value,
        status: value.awaitingTeacher
          ? 'awaiting'
          : value.running === true
            ? 'active'
            : 'inactive',
      }));

    return {
      selectedStatus: 'active',
      currentPage: 1,
      itemsPerPage: 10,
      projects: projectsArray,
      sortKey: '',
      sortOrders: {
        project: 1,
        status: 1,
      },
    };
  },
  computed: {
    filteredData() {
      let data = this.projects;
      if (this.selectedStatus) {
        data = data.filter(item => item.status === this.selectedStatus);
      }
      return data;
    },
    paginatedData() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredData.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.filteredData.length / this.itemsPerPage);
    },
  },
  methods: {
    filterData() {
      this.currentPage = 1;
    },
    sort(key) {
      this.sortKey = key;
      this.sortOrders[key] = this.sortOrders[key] * -1;
      this.projects.sort((a, b) => {
        const result = a[key] > b[key] ? 1 : a[key] < b[key] ? -1 : 0;
        return result * this.sortOrders[key];
      });
    },
    getSortIcon(key) {
      if (this.sortKey === key) {
        return this.sortOrders[key] === 1 ? 'fa fa-arrow-up' : 'fa fa-arrow-down';
      }
      return '';
    },
    changePage(page) {
      if (page > 0 && page <= this.totalPages) {
        this.currentPage = page;
      }
    },
  },
};
</script>

<style>
.cursor-pointer {
  cursor: pointer;
}

.table-success {
  background-color: #d4edda;
}

.table-danger {
  background-color: #f8d7da;
}

.badge {
  padding: 0.5em;
  font-size: 0.9em;
}

.bg-success {
  background-color: #28a745 !important;
}

.bg-danger {
  background-color: #dc3545 !important;
}
</style>
