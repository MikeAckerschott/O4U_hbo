<template>
  <div class="container mt-4">
    <table class="table table-hover table-striped">
      <thead class="table-dark">
        <tr>
          <th scope="col" @click="sortTable('following')">
            Following
            <i :class="getSortIcon('following')"></i>
          </th>
          <th scope="col" @click="sortTable('name')">
            Name
            <i :class="getSortIcon('name')"></i>
          </th>
          <th scope="col" @click="sortTable('id')">
            ID
            <i :class="getSortIcon('id')"></i>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in sortedItems" :key="item.id" @click="navigateToDetail(item.id)">
          <td>{{ item.following }}</td>
          <td>{{ item.name }}</td>
          <td>{{ item.id }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'CriteriaTable',
  data() {
    return {
      items: [
        { following: 'Yes', name: 'John Doe', id: 1 },
        { following: 'No', name: 'Jane Smith', id: 2 },
        { following: 'Yes', name: 'Alice Johnson', id: 3 },
        // Add more items as needed
      ],
      sortKey: '',
      sortOrder: 1,
    };
  },
  computed: {
    sortedItems() {
      return this.items.sort((a, b) => {
        if (a[this.sortKey] < b[this.sortKey]) return -1 * this.sortOrder;
        if (a[this.sortKey] > b[this.sortKey]) return 1 * this.sortOrder;
        return 0;
      });
    },
  },
  methods: {
    sortTable(key) {
      if (this.sortKey === key) {
        this.sortOrder *= -1;
      } else {
        this.sortKey = key;
        this.sortOrder = 1;
      }
    },
    getSortIcon(key) {
      if (this.sortKey === key) {
        return this.sortOrder === 1 ? 'bi bi-arrow-up' : 'bi bi-arrow-down';
      }
      return 'bi bi-arrow-down-up';
    },
    navigateToDetail(id) {
      this.$router.push({ name: 'DetailPage', params: { id } });
    },
  },
};
</script>

<style>
.table-hover tbody tr:hover {
  cursor: pointer;
}
th {
  cursor: pointer;
}
</style>
