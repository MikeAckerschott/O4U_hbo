<template>
  <div class="container mt-5">
    <h1 class="mb-4">Graduated Student Rubrics</h1>

    <div class="row mb-3">
      <div class="col-md-6">
        <h2>{{ student.name }}</h2>
        <p><strong>Student Number:</strong> {{ student.studentNumber }}</p>
        <p><strong>Graduation Date:</strong> {{ student.graduationDate }}</p>
      </div>
      <div class="col-md-6 text-end">
        <button @click="exportToExcel" class="btn btn-success">
          <i class="bi bi-file-earmark-excel me-2"></i>Export to Excel
        </button>
      </div>
    </div>

    <div class="table-responsive">
      <table class="table table-striped table-hover">
        <thead class="table-light">
          <tr>
            <th @click="sort('beoordelingscriteria')" class="cursor-pointer">
              Courses
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
          <tr v-for="rubric in sortedRubrics" :key="rubric.id" :class="getRowClass(rubric.beoordeling)">
            <td>{{ rubric.beoordelingscriteria }}</td>
            <td>{{ rubric.fase }}</td>
            <td>
              <span :class="getBadgeClass(rubric.beoordeling)">
                {{ rubric.beoordeling }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import * as XLSX from 'xlsx';

const student = ref({
  name: 'Rick',
  studentNumber: '12345678',
  graduationDate: '2023-06-15',
});

const rubrics = ref([
  {id: 1, beoordelingscriteria: '25192', fase: 'Ontwikkeling', beoordeling: 'Goed'},
  {id: 2, beoordelingscriteria: '25194', fase: 'Uitvoerings', beoordeling: 'Voldoende'},
  {id: 3, beoordelingscriteria: '25191', fase: 'Afronding', beoordeling: 'Voldoende'},
  {id: 4, beoordelingscriteria: '25193', fase: 'Voorbereiding', beoordeling: 'Goed'},
  {id: 5, beoordelingscriteria: '25195', fase: 'Ontwikkeling', beoordeling: 'Goed'},
  {id: 6, beoordelingscriteria: '25196', fase: 'Uitvoerings', beoordeling: 'Voldoende'},
  {id: 7, beoordelingscriteria: '25197', fase: 'Afronding', beoordeling: 'Goed'},
  {id: 8, beoordelingscriteria: '25198', fase: 'Voorbereiding', beoordeling: 'Voldoende'},
  {id: 9, beoordelingscriteria: '25199', fase: 'Ontwikkeling', beoordeling: 'Voldoende'},
  {id: 10, beoordelingscriteria: '25200', fase: 'Uitvoerings', beoordeling: 'Goed'},
]);

const sortKey = ref('');
const sortOrder = ref('asc');

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

const sortedRubrics = computed(() => {
  return [...rubrics.value].sort((a, b) => {
    if (a[sortKey.value] < b[sortKey.value]) return sortOrder.value === 'asc' ? -1 : 1;
    if (a[sortKey.value] > b[sortKey.value]) return sortOrder.value === 'asc' ? 1 : -1;
    return 0;
  });
});

const getBadgeClass = (beoordeling) => {
  switch (beoordeling) {
    case 'Goed':
      return 'badge bg-success';
    case 'Voldoende':
      return 'badge bg-info';
    case 'Onvoldoende':
      return 'badge bg-danger';
    default:
      return 'badge bg-secondary';
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
    default:
      return '';
  }
};

const exportToExcel = () => {
  const wb = XLSX.utils.book_new();

  // Add student details sheet
  const studentDetails = [
    { Label: 'Name', Value: student.value.name },
    { Label: 'Student Number', Value: student.value.studentNumber },
    { Label: 'Graduation Date', Value: student.value.graduationDate },
  ];
  const wsStudent = XLSX.utils.json_to_sheet(studentDetails);
  XLSX.utils.book_append_sheet(wb, wsStudent, "Student Details");

  // Add rubrics sheet
  const wsRubrics = XLSX.utils.json_to_sheet(rubrics.value);
  XLSX.utils.book_append_sheet(wb, wsRubrics, "Rubrics");

  const fileName = `${student.value.name}_Rubrics.xlsx`;
  XLSX.writeFile(wb, fileName);
};
</script>

<style scoped>
.cursor-pointer {
  cursor: pointer;
}
</style>
