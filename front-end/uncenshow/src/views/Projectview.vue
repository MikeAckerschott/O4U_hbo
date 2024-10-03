<template>
  <div class="container mt-4">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Following</th>
          <th scope="col">Name</th>
          <th scope="col">ID</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in items" :key="item.id" @click="navigateToDetail(item.id)">
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
    };
  },
  methods: {
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
</style>
