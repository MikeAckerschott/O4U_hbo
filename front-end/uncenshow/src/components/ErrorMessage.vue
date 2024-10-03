<template>
    <div class="error-message-box">
        <p class="error-message">{{ message }}</p>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
    name: 'ErrorMessage',
    props: {
        message: String,
    }
})
</script>

<style scoped>
    .error-message-box {
        padding: 10px;
        margin-bottom: 10px;
        border-radius: 5px;
        border: 1px solid rgb(255, 59, 59);
    }
    .error-message {
        font-size: 14px;
        font-weight: 700;
    }
</style>
