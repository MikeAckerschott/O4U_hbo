<template>
  <div class="signup center">
    <div class="container">
      <ErrorMessage
        v-if="store.getters.getErrorMessage != null"
        :message="store.getters.getErrorMessage"
      ></ErrorMessage>
      <form class="form">
        <TextBox
          :emitUpdate="handleUpdatedUsername"
          placeholder="Username"
          :value="username"
          class_name="textbox"
          type="username"
          label="Signup"
        />
        <p v-if="!isMinLengthUsername() && username.length != 0">
          The username should be atleast {{ usernameLength.min }} characters long
        </p>
        <p v-if="!isMaxLengthUsername()">
          The username should be atmost {{ usernameLength.max }} characters long
        </p>
        <TextBox
          :emitUpdate="handleUpdatedPassword"
          placeholder="Password"
          :value="password"
          class_name="textbox"
          type="password"
          label="Password"
        />
        <p v-if="!isMinLengthPassword() && password.length != 0">
          The password should be atleast {{ passwordLength.min }} characters long
        </p>
        <p v-if="!isMaxLengthPassword()">
          The password should be atmost {{ passwordLength.max }} characters long
        </p>
        <p v-if="!isPasswordContainingUppercase() && password.length != 0">
          The password should contain atleast one uppercase letter
        </p>
        <p v-if="!isPasswordContainingLowercase() && password.length != 0">
          The password should contain atleast one lowercase letter
        </p>
        <p v-if="!isPasswordContainingSymbol() && password.length != 0">
          The password should contain atleast one symbol
        </p>
        <p v-if="!isPasswordContainingNumber() && password.length != 0">
          The password should contain atleast one number
        </p>

        <label class="label" for="image">Image</label>

        <p v-if="!isValidImage() && image !== null" class="error-Text">
          Invalid image. Please choose a valid image file. <br/>
          The image should be less than 1MB in size,
          And should be of type jpeg, png or gif.
        </p>


        <input class="input" id="image" type="file" accept="image/*" @change="handleImageChange" />

        <button
          :class="{
            button: isValidPassword() && isValidUsername(),
            buttonDisabled: !isValidPassword() || !isValidUsername()
          }"
          @click.prevent="isValidPassword() && isValidUsername()? signup() : null"
        >
          Signup
        </button>
      </form>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useStore } from 'vuex'
import TextBox from './TextBox.vue'
import ErrorMessage from './ErrorMessage.vue'

export default defineComponent({
  name: 'SignupPage',
  components: {
    TextBox,
    ErrorMessage
  },
  data() {
    return {
      store: useStore(),
      username: '',
      password: '',
      image: null as File | null,
      usernameLength: {
        min: 4,
        max: 50
      },
      passwordLength: {
        min: 8,
        max: 50
      }
    }
  },
  methods: {
    async signup() {

      await this.store.dispatch('signup', {
        username: this.username,
        password: this.password,
        image: this.image
      })

      const user = this.store.getters.getUser
      if (user) {
        this.$router.push({ name: 'home' })
      }
    },
    handleImageChange(event: Event) {
      const target = event.target as HTMLInputElement
      if (target.files && target.files.length > 0) {
        this.image = target.files[0]
      }
    },
    isValidImage() {
      if (this.image === null) {
        return false
      }

      if (this.image.size > 1000000) {
        return false
      }

      return this.image !== null && ['image/jpeg', 'image/png', 'image/gif'].includes(this.image.type);
    },

    handleUpdatedUsername(value: string) {
      this.username = value
    },

    isValidUsername() {
      return (
        this.username.length >= this.usernameLength.min &&
        this.username.length <= this.usernameLength.max
      )
    },

    isMinLengthUsername() {
      return this.username.length >= this.usernameLength.min
    },

    isMaxLengthUsername() {
      return this.username.length <= this.usernameLength.max
    },

    isMinLengthPassword() {
      return this.password.length >= this.passwordLength.min
    },

    isMaxLengthPassword() {
      return this.password.length <= this.passwordLength.max
    },

    isPasswordContainingUppercase() {
      const uppercaseRegex = new RegExp('^(?=.*[A-Z])')
      return uppercaseRegex.test(this.password)
    },

    isPasswordContainingLowercase() {
      const lowercaseRegex = new RegExp('^(?=.*[a-z])')
      return lowercaseRegex.test(this.password)
    },

    isPasswordContainingSymbol() {
      const symbolRegex = new RegExp('^(?=.*[!@#\$%\^&\*])')
      return symbolRegex.test(this.password)
    },

    isPasswordContainingNumber() {
      const numberRegex = new RegExp('^(?=.*[0-9])')
      return numberRegex.test(this.password)
    },

    isValidPassword() {
      const mediumRegex = new RegExp(
        '^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@$!%*?&]{8,50}\$'
      )
      return mediumRegex.test(this.password)
    },

    handleUpdatedPassword(value: string) {
      this.password = value
    }
  }
})
</script>

<style scoped>
.flexbox {
  display: flex;
  justify-content: center;
  align-items: center;
}
.center {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
.container {
  width: 400px;
  border: 1px solid black;
  border-radius: 10px;
  padding: 20px;
}
.form {
  display: flex;
  flex-direction: column;
}
.label {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 10px;
}
.input {
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 5px;
  border: 1px solid lightgray;
}
.button {
  padding: 10px;
  border-radius: 5px;
  border: none;
  background-color: #3f51b5;
  color: white;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
}

.buttonDisabled {
  padding: 10px;
  border-radius: 5px;
  border: none;
  background-color: #3b383f;
  color: rgb(146, 146, 146);
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
}
.error-Text {
  color: rgb(181, 181, 181);
  font-size: 14px;
  padding-bottom: 10px;
  line-height: 1.2;
}
</style>
