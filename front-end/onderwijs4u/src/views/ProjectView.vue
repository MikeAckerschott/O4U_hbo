<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col-md-3">
        <label for="faseFilter">Filter by Fase:</label>
        <select id="faseFilter" v-model="selectedFase" @change="filterData" class="form-control">
          <option value="">All</option>
          <option v-for="fase in uniqueFases" :key="fase" :value="fase">{{ fase }}</option>
        </select>
      </div>
      <div class="col-md-9">
        <table class="table table-striped table-hover w-100">
          <thead>
            <tr>
              <th @click="sort('beoordelingscriteria')" class="cursor-pointer">
                Beoordelingscriteria
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
            <tr v-for="item in filteredData" :key="item.id">
              <td>{{ item.beoordelingscriteria }}</td>
              <td>{{ item.fase }}</td>
              <td>{{ item.beoordeling }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref, computed } from 'vue';

const data = ref([
  { id: 1, beoordelingscriteria: '25192', fase: 'Ontwikkeling', beoordeling: 'Goed' },
  { id: 2, beoordelingscriteria: '25194', fase: 'Uitvoerings', beoordeling: 'Voldoende' },
  { id: 3, beoordelingscriteria: '25191', fase: 'Afronding', beoordeling: 'Uitstekend' },
  { id: 4, beoordelingscriteria: '25193', fase: 'Voorbereiding', beoordeling: 'Matig' },
  { id: 5, beoordelingscriteria: '25195', fase: 'Ontwikkeling', beoordeling: 'Goed' },
]);

const sortKey = ref('');
const sortOrder = ref('asc');
const selectedFase = ref('');

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

const filterData = () => {
  // This function is called when the filter changes
};
</script>


<style scoped>
.cursor-pointer {
  cursor: pointer;
}
.table {
  width: 100%;
}
</style>
