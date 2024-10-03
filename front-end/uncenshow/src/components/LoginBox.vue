<template>
    <div class="login center">
        <div class="container">
            <ErrorMessage v-if="store.getters.getErrorMessage != null" :message="store.getters.getErrorMessage"></ErrorMessage>
            <form class="form">
                <TextBox :emitUpdate="handleUpdatedUsername" placeholder="Username" :value="username" class_name="textbox" type="username" label="Login" />
                <TextBox :emitUpdate="handleUpdatedPassword" placeholder="Password" :value="password" class_name="textbox" type="password" label="Password" />
                <button class="button" @click.prevent="login">Login</button>
            </form>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import TextBox from './TextBox.vue';
import { useStore } from "vuex";
import ErrorMessage from './ErrorMessage.vue';

export default defineComponent({
    name: 'LoginPage',
    components: {
    TextBox,
    ErrorMessage
},
    data() {
        return {
            store: useStore(),
            username: '',
            password: '',
        };
    },
    methods: {
        handleUpdatedUsername(value: string) {
            this.username = value;
        },
        handleUpdatedPassword(value: string) {
            this.password = value;
        },
        async login() {
            await this.store.dispatch('login', {
                username: this.username,
                password: this.password,
            })

            const user = this.store.getters.getUser;
            console.log(user);
            if (user) {
                this.$router.push({ name: 'home' })
            }
        },
    },
});
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
</style>