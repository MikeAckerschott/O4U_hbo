<!-- filepath: c:\Users\mikea\Documents\GitHub\O4U_hbo\front-end\onderwijs4u\src\components\CourseTableRow.vue -->
<template>
    <tr :class="getRowClass(course.criteria)">
      <td>
        <RouterLink class="nav-link text-nowrap" :to="`/O4U_hbo/rubric/${course.beoordelingscriteria}`">
          {{ course.beoordelingscriteria }}
        </RouterLink>
      </td>
      <td>
        <template
          v-for="val in [completionCriteriaTracker(getAttachedProjectsFromCourse(course.beoordelingscriteria), course.beoordelingscriteria)]"
          :key="val">
          <div class="progress" style="height: 20px;">
            <div
              class="progress-bar"
              role="progressbar"
              :style="{ width: Math.max((val / course.criteria.length * 100), 10) + '%' }"
              :class="getBadgeClass(val / course.criteria.length)"
              :aria-valuenow="val / course.criteria.length"
              aria-valuemin="0"
              aria-valuemax="100"
            >
              {{ val }} / {{ course.criteria.length }}
            </div>
          </div>
        </template>
      </td>
      <td>
        <RouterLink
          v-for="project in getAttachedProjectsFromCourse(course.beoordelingscriteria)"
          class="nav-link text-nowrap"
          :to="`/O4U_hbo/project/${project.key}`"
          :key="project.key"
          style="white-space: nowrap;"
        >
          {{ project.key }}
        </RouterLink>
      </td>
      <td>
        <template>
            <p>waaa</p>
        </template>
      </td>
    </tr>
  </template>
  
  <script setup>
  import { computed } from 'vue';
  import { RouterLink } from 'vue-router';
  
  defineProps({
    course: Object, // The course data for the row
    getAttachedProjectsFromCourse: Function, // Function to get attached projects
    completionCriteriaTracker: Function, // Function to calculate completion
    getRowClass: Function, // Function to get row class
    getBadgeClass: Function, // Function to get badge class
  });

  
  </script>