<script setup lang="ts">
import ContentPanel from '@/components/ContentPanel.vue'
import PrimaryButton from '@/components/PrimaryButton.vue'
import type { User, Quiz } from '@/types'
import { useStore } from 'vuex'
import { post } from '@/services/ApiService'
import { get } from '@/services/ApiService'
import { useRouter } from 'vue-router'

const store = useStore()
const $router = useRouter()


</script>

<template>
  <main>
    <!-- <button class="debug" @click="getAllQuizzes"></button> -->
    <div class="centered">
      <div class="column">
        <div class="main-text-container">
          <div class="column main-text">
            <h1 class="title-text">Welkom</h1>
            <p class="paragraph">
              lorum ipsum dolor sit amet, consectetur adipiscing elit. Nulla quam velit, vulputate eu pharetra nec,
              mattis ac neque. Duis vulputate commodo lectus, ac blandit elit tincidunt id. Sed rhoncus, tortor sed
              eleifend tristique, tortor mauris molestie elit, et luctus enim justo non nisi. Donec a semper turpis.
              Nunc eget ultricies sem. Morbi porta, libero id ullamcorper semper, sapien justo venenatis odio, non
              euismod felis turpis ut quam. Sed id dui mi.
            </p>
          </div>
          <img src="../assets/Images/Icons/logo.png" class="logo-image" />
        </div>
        <div class="sub-text-container">
          <ContentPanel class="contentPanel">
            <div class="align-center">
              <div class="row">
                <div class="column">
                  <h1 class="title-text">Title</h1>

                  <p class="paragraph">lorum ipsum dolor sit amet, consectetur adipiscing elit. Nulla quam velit, vulputate eu pharetra nec,</p>
                </div>
                <img src="../assets/Images/Icons/desktop keyboard mouse 1.png" alt="Arrow icon" class="lobby-image" />
              </div>
              <div class="row">
                <PrimaryButton class="create-button" label="button" icon-right="src\assets\Images\Icons\add.png">
                </PrimaryButton>
                <PrimaryButton class="create-button" label="button"></PrimaryButton>
              </div>
            </div>
          </ContentPanel>
          <ContentPanel class="contentPanel">
            <div class="align-center">
              <div class="row">
                <div class="column">
                  <h1 class="title-text">Title</h1>

                  <p class="paragraph">lorum ipsum dolor sit amet, consectetur adipiscing elit. Nulla quam velit, vulputate eu pharetra nec,</p>
                </div>
                <img class="game-image" src="../assets/Images/Icons/gaming 2.png" alt="Arrow icon" />
              </div>
              <div class="row">
                <PrimaryButton class="create-button" label="button" @click="console.log('todo')"
                  href="/quizList">
                </PrimaryButton>
              </div>
            </div>
          </ContentPanel>
        </div>
      </div>
    </div>
  </main>
</template>

<style scoped>
.centered {
  /* place in middle of screen; */
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;

  margin-left: 10%;
}

.debug {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9999;

  width: 100px;
  height: 100px;
}

.main-text {
  max-width: 40%;
}

.row {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: left;
  width: 100%;
}

.main-text-container {
  display: flex;
  flex-direction: row;
  justify-content: left;
  align-items: center;
}

.sub-text-container {
  display: flex;
  flex-direction: row;
  justify-content: left;
  align-items: center;
}

.column {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: left;
}

.contentPanel {
  width: 90%;
  max-width: 400px;
  margin: 0 10px;
  height: 100%;
}

.title-text {
  font-size: 30px;
  font-weight: 700;
  margin-bottom: 10px;
  text-align: left;
  max-width: 100%;
}

.create-button {
  width: 50%;
}

.align-center {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.paragraph {
  font-size: 14px;
  font-weight: 400;
  margin-bottom: 20px;
  text-align: left;
  max-width: 100%;
}

.lobby-image {
  display: flex;
  justify-content: center;
  width: 130px;
  height: 130px;
  margin-bottom: 10px;
}

.game-image {
  display: flex;
  justify-content: center;
  width: 130px;
  height: 130px;
  margin-bottom: 10px;
}

.logo-image {
  display: flex;
  justify-content: center;
  width: 200px;
  height: 200px;
  margin-bottom: 10px;
}

.divider {
  width: 100%;
  height: 1px;
  background-color: #b1b1b1;
  margin-bottom: 20px;
}
</style>
