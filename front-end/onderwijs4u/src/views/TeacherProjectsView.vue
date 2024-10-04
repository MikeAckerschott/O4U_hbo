<template>
  <div class="container-fluid mt-5">
    <div class="row mb-3">
      <div class="col-md-3">
        <label for="statusFilter" class="form-label">Filter by Status:</label>
        <select id="statusFilter" v-model="selectedStatus" @change="filterData" class="form-select">
          <option value="">All</option>
          <option value="active">Active</option>
          <option value="inactive">Inactive</option>
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
              <tr v-for="item in paginatedData" :key="item.id" :class="{'table-success': item.status === 'active', 'table-danger': item.status === 'inactive'}">
                <td>
                  <RouterLink class="nav-link" :to="`/teacherproject/${item.project}`">
                    {{ item.project }}
                  </RouterLink>
                </td>
                <td>
                  <span :class="{'badge bg-success': item.status === 'active', 'badge bg-danger': item.status === 'inactive'}">
                    {{ item.status === 'active' ? 'Active' : 'Inactive' }}
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
export default {
  data() {
    return {
      selectedStatus: '',
      currentPage: 1,
      itemsPerPage: 10,
      projects: [
        { id: 1, project: 'Project A', status: 'active' },
        { id: 2, project: 'Project B', status: 'inactive' },
        // Add more project data here
      ],
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
