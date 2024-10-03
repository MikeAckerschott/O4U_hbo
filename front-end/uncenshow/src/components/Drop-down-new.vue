<template>
  <div class="selectContainer">
    <p @click="toggleMenu">{{ displayName }} <i class="arrow down"></i></p>
    <div v-if="isOpened" class="PopoutMenu">
      <button
        v-for="item in items"
        :key="item.name"
        @click="customItemCallback(item)"
        @click.prevent="console.log('todo')"
      >
        {{ item.name }}
      </button>
      <TextBox
        v-for="(textField, index) in textFields"
        :key="textField.value"
        :emitUpdateIndex="handleUpdatedValue"
        :index="index"
        :value="textField.value"
        class_name="textbox"
        type="username"
        @click.prevent="console.log('todo')"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import TextBox from './TextBox.vue'

export default defineComponent({
  name: 'DropDownNew',
  components: { TextBox },
  props: {
    items: {
      type: Array<{ name: string; callback: () => void; closeOnClick: boolean }>,
      required: false
    },
    textFields: {
      type: Array<{ value: string; callback: (textfieldValue: string) => void }>,
      required: false
    },
    displayName: String
  },
  data() {
    return {
      isOpened: false
    }
  },

  created() {},
  methods: {
    toggleMenu() {
      this.isOpened = !this.isOpened
      if (this.isOpened) {
        // Add mouse down event listener to close the menu when anywhere is clicked
        document.addEventListener('mousedown', this.closeMenuOnClickOutside)
      } else {
        // Remove the event listener when the menu is closed
        document.removeEventListener('mousedown', this.closeMenuOnClickOutside)
      }
    },

    handleUpdatedValue(value: string, index: number) {
      if (this.textFields && this.textFields[index]) {
        this.textFields[index].callback(value)
      }
    },

    closeMenuOnClickOutside(event: MouseEvent) {
      // Check if the dropdown menu is open
      if (this.isOpened) {
        // Check if the clicked element is inside the dropdown menu or its trigger
        const isInsideMenu = this.$el.contains(event.target)
        if (!isInsideMenu) {
          // If clicked outside the menu, close it
          this.isOpened = false
          // Remove the event listener once the menu is closed
          document.removeEventListener('mousedown', this.closeMenuOnClickOutside)
        }
      }
    },

    customItemCallback(item: { name: string; callback: () => void; closeOnClick: boolean }) {
      item.callback()
      if (item.closeOnClick) {
        this.isOpened = false
      }
    }
  }
})
</script>

<style scoped>
.selectContainer > p {
  font-size: 20px;
  overflow: visible;
}

.PopoutMenu {
  background-color: white;
  position: absolute;
  z-index: 100;
}

.arrow {
  border: solid rgb(255, 255, 255);
  border-width: 0 3px 3px 0;
  display: inline-block;
  padding: 3px;
}

.down {
  transform: rotate(45deg);
  -webkit-transform: rotate(45deg);
}

.selectContainer {
  position: relative; /* Ensure relative positioning for absolute positioning within */
  height: 30px; /* Set a specific height */
}
</style>
