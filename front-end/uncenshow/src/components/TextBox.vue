<template>
    <label v-if="label != null" class="label" >{{ label }}</label>
    <input :type="type" :placeholder="placeholder" v-model="internalValue" :class="class_name" @input="getStringSize"/>
</template>

<script lang="ts">
import { defineComponent } from "vue";
export default defineComponent({
    name: 'TextBox',
    props: {
        emitUpdate: Function,
        emitUpdateIndex: Function,
        placeholder: String,
        value: String,
        class_name: String,
        type: String,
        label: String,
        index: Number
    },
    data() {
        return {
            internalValue: this.value,
        }
    },
    watch: {
        internalValue(value) {
            if (this.emitUpdate) {
                this.emitUpdate(value);
            }
            if (this.emitUpdateIndex) {
                this.emitUpdateIndex(value, this.index);
            }
        }
    },
    methods: {
        getStringSize() {
            const size = this.internalValue?.length ?? 0;
            return size;
        }
    }
})
</script>

<style scoped>
    .textbox {
        padding: 10px;
        margin-bottom: 10px;
        border-radius: 5px;
        border: 1px solid lightgray;
    }
    .label {
        font-size: 20px;
        font-weight: 700;
        margin-bottom: 10px;
    }
</style>