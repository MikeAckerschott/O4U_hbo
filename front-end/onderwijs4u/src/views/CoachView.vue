<template>
  <div class="container mt-5">
    <h1 class="mb-4">Studentenvoortgang</h1>

    <div class="table-responsive">
      <table class="table table-hover">
        <thead>
          <tr>
            <th @click="sort('name')" class="sortable">
              Naamn
              <SortIcon :active="sortColumn === 'name'" :ascending="sortOrder === 'asc'" />
            </th>
            <th @click="sort('progress')" class="sortable">
              Voortgang
              <SortIcon :active="sortColumn === 'progress'" :ascending="sortOrder === 'asc'" />
            </th>
            <th @click="sort('yearOfStudy')" class="sortable">
              Studiejaar
              <SortIcon :active="sortColumn === 'yearOfStudy'" :ascending="sortOrder === 'asc'" />
            </th>
            <th @click="sort('needForHelp')" class="sortable">
              Support score
              <SortIcon :active="sortColumn === 'needForHelp'" :ascending="sortOrder === 'asc'" />
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="student in paginatedStudents" :key="student.id" :class="getRowClass(student.needForHelp)">
            <td class="name-cell">
              <RouterLink class="nav-link" :to="`/rubrics`">
                {{ student.name }}
              </RouterLink>
            </td>
            <td>
              <div class="progress" style="height: 20px;">
                <div
                  class="progress-bar"
                  role="progressbar"
                  :style="{ width: `${student.progress}%` }"
                  :aria-valuenow="student.progress"
                  aria-valuemin="0"
                  aria-valuemax="100"
                >{{ student.progress }}%</div>
              </div>
            </td>
            <td>{{ student.yearOfStudy }}</td>
            <td>
              <span
                class="badge"
                :class="getNeedForHelpClass(student.needForHelp)"
              >
                {{ student.needForHelp.toFixed(2) }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <nav aria-label="Student table navigation">
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
</template>

<script setup>
import {ref, computed, h} from 'vue'
import { ChevronUp, ChevronDown } from 'lucide-vue-next'

const SortIcon = {
  props: {
    active: Boolean,
    ascending: Boolean
  },
  setup(props) {
    return () => h('span', { class: `sort-icon ${props.active ? 'active' : ''}` }, [
      props.active && props.ascending
        ? h(ChevronUp, { size: 14 })
        : h(ChevronDown, { size: 14 })
    ])
  }
}

const students = ref([
  { id: 1, name: 'Jan de Vries', progress: 75, yearOfStudy: 3 },
  { id: 2, name: 'Sanne Jansen', progress: 45, yearOfStudy: 2 },
  { id: 3, name: 'Pieter Bakker', progress: 90, yearOfStudy: 4 },
  { id: 4, name: 'Lisa Visser', progress: 30, yearOfStudy: 4 },
  { id: 5, name: 'Tom Smit', progress: 60, yearOfStudy: 3 },
  { id: 6, name: 'Emma de Jong', progress: 15, yearOfStudy: 1 },
  { id: 7, name: 'Daan Mulder', progress: 50, yearOfStudy: 2 },
  { id: 8, name: 'Sophie Meijer', progress: 95, yearOfStudy: 3 },
  { id: 9, name: 'Lars de Boer', progress: 40, yearOfStudy: 4 },
  { id: 10, name: 'Julia de Groot', progress: 70, yearOfStudy: 3 },
  { id: 11, name: 'Rik Bos', progress: 55, yearOfStudy: 2 },
  { id: 12, name: 'Noa Vos', progress: 85, yearOfStudy: 4 },
  { id: 13, name: 'Milan Kuiper', progress: 20, yearOfStudy: 1 },
  { id: 14, name: 'Eva de Leeuw', progress: 65, yearOfStudy: 3 },
  { id: 15, name: 'Luuk de Bruin', progress: 60, yearOfStudy: 4 },
  { id: 16, name: 'Tess de Wit', progress: 35, yearOfStudy: 2 },
  { id: 17, name: 'Bram van Dijk', progress: 70, yearOfStudy: 3 },
  { id: 18, name: 'Lotte van Dam', progress: 50, yearOfStudy: 2 },
  { id: 19, name: 'Finn van den Berg', progress: 95, yearOfStudy: 4 },
  { id: 20, name: 'Sara van der Meer', progress: 60, yearOfStudy: 3 },
  { id: 21, name: 'Jesse de Lange', progress: 40, yearOfStudy: 2 },
  { id: 22, name: 'Nina van Vliet', progress: 75, yearOfStudy: 3 },
  { id: 23, name: 'Sem van Leeuwen', progress: 85, yearOfStudy: 4 },
  { id: 24, name: 'Fleur van den Heuvel', progress: 30, yearOfStudy: 1 },
  { id: 25, name: 'Timo van Beek', progress: 100, yearOfStudy: 4 },
  { id: 26, name: 'Maud van der Linden', progress: 55, yearOfStudy: 2 },
  { id: 27, name: 'Stijn van der Wal', progress: 70, yearOfStudy: 3 },
  { id: 28, name: 'Lieke van der Ven', progress: 90, yearOfStudy: 4 },
  { id: 29, name: 'Jill van der Laan', progress: 65, yearOfStudy: 3 },
  { id: 30, name: 'Koen van der Horst', progress: 80, yearOfStudy: 4 },
  { id: 31, name: 'Roos van der Pol', progress: 95, yearOfStudy: 4 },
  { id: 32, name: 'Sven van der Zee', progress: 50, yearOfStudy: 2 },
  { id: 33, name: 'Mila van der Meulen', progress: 25, yearOfStudy: 1 },
  { id: 34, name: 'Sam van der Linden', progress: 85, yearOfStudy: 4 },
  { id: 35, name: 'Jade van der Steen', progress: 70, yearOfStudy: 3 },
  { id: 36, name: 'Thijs van der Werf', progress: 60, yearOfStudy: 2 },
  { id: 37, name: 'Lynn van der Heijden', progress: 80, yearOfStudy: 3 },
  { id: 38, name: 'Gijs van der Veer', progress: 90, yearOfStudy: 4 },
  { id: 39, name: 'Mick van der Linden', progress: 75, yearOfStudy: 3 },
  { id: 40, name: 'Saar van der Linden', progress: 95, yearOfStudy: 4 },
])

const sortColumn = ref('needForHelp')
const sortOrder = ref('desc')
const currentPage = ref(1)
const itemsPerPage = 10

const calculateNeedForHelp = (student) => {
  const expectedProgress = student.yearOfStudy * 25
  return expectedProgress - student.progress
}

// Add needForHelp property to each student
students.value.forEach(student => {
  student.needForHelp = calculateNeedForHelp(student)
})

const sortedStudents = computed(() => {
  return [...students.value].sort((a, b) => {
    let comparison = 0
    if (a[sortColumn.value] < b[sortColumn.value]) comparison = -1
    if (a[sortColumn.value] > b[sortColumn.value]) comparison = 1
    return sortOrder.value === 'asc' ? comparison : -comparison
  })
})

const paginatedStudents = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  const end = start + itemsPerPage
  return sortedStudents.value.slice(start, end)
})

const totalPages = computed(() => Math.ceil(students.value.length / itemsPerPage))

const sort = (column) => {
  if (sortColumn.value === column) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortColumn.value = column
    sortOrder.value = 'asc'
  }
}

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}

const getNeedForHelpClass = (needForHelp) => {
  if (needForHelp > 50) return 'bg-danger'
  if (needForHelp > 25) return 'bg-warning'
  return 'bg-success'
}

const getRowClass = (needForHelp) => {
  if (needForHelp > 50) return 'table-danger'
  if (needForHelp > 25) return 'table-warning'
  return 'table-success'
}
</script>

<style scoped>
.progress {
  height: 20px;
}
.progress-bar {
  line-height: 20px;
}
.table-responsive {
  margin-bottom: 1rem;
}
.sortable {
  cursor: pointer;
  user-select: none;
}
.sort-icon {
  display: inline-block;
  width: 14px;
  height: 14px;
  margin-left: 5px;
  vertical-align: middle;
}
.sort-icon.active {
  color: #007bff;
}
.name-cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px;
}
.table th, .table td {
  vertical-align: middle;
}
.badge {
  font-size: 0.9em;
  padding: 0.4em 0.6em;
}
</style>