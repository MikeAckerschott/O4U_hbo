<template>
    <div class="content-panel">
        <slot></slot>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
    name: 'ContentPanel',
    props: {
        title: String,
    },
});
</script>

<style scoped>
.content-panel {
    border: 2px solid rgb(255, 255, 255);
    border-radius: 10px;
    padding: 20px;
}
</style>
