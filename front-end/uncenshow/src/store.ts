import { createStore, Store } from 'vuex';
import { login, signup } from '@/services/Login-Signup-Service';
import { post } from '@/services/ApiService';
import type { User, Quiz } from '@/types';
import router from './router';

interface AuthState {
  user: User | null;
  errorMessage: string | null; // Add errorMessage to state
}

const USER_STORAGE_KEY = 'user';

const store: Store<AuthState> = createStore({
  state: {
    user: JSON.parse(localStorage.getItem(USER_STORAGE_KEY) || 'null') as User,
    errorMessage: null,
  },
  mutations: {
    setUser(state: AuthState, user: User): void {
      state.user = user;
      state.errorMessage = null; // Clear error message on successful login/signup
      localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(user));
    },
    clearUser(state: AuthState): void {
      state.user = null;
      state.errorMessage = null; // Clear error message on logout
      localStorage.removeItem(USER_STORAGE_KEY);
    },
    setErrorMessage(state: AuthState, errorMessage: string): void {
      state.errorMessage = errorMessage;
    },
    clearErrorMessage(state: AuthState): void {
      state.errorMessage = null;
    }
  },
  actions: {
    async signup({ commit }, credentials: { username: string; password: string; image: File }): Promise<void> {
      try {
        const signupData = {
          username: credentials.username,
          password: credentials.password,
          image: "",
        };
        let responseImagePath = "";
        if (credentials.image != null) {
          const body = new FormData();
          body.append('image', credentials.image);
          responseImagePath = await post('/user/image', body);
          signupData.image = responseImagePath;
        }
        const response = await signup(signupData);
        let pathToImage: string = "src\\assets\\Images\\profile-images\\" + responseImagePath;
        if (responseImagePath === "") {
          pathToImage = 'src\\assets\\Images\\standard-profile-icon.jpg';
        }
        const user: User = {
          id: response.id,
          token: response.token,
          username: response.username,
          password: "", // don't store the password
          image: pathToImage,
        };

        commit('setUser', user);
      } catch (error: any) {
        commit('setErrorMessage', error.message);
      }
    },
    async login({ commit }, credentials: { username: string; password: string }): Promise<void> {
      try {
        const response = await login(credentials);

        const user: User = {
          id: response.id,
          token: response.token,
          username: response.username,
          password: "", // don't store the password
          image: response.image,
        };

        commit('setUser', user);
      } catch (error: any) {
        commit('setErrorMessage', error.message);
      }
    },
    // async getQuizzes({ commit }, credentials: {username: string;}): Promise<void> {
    //   try {
    //     const response = await post('/user/getQuizzes', credentials);
    //     commit('setQuizzes', response.quizzes);
    //   } catch (error: any) {
    //     commit('setErrorMessage', error.message);
    //   }
    // },
    async uploadQuiz({ commit }, quizData: { quiz : Quiz }): Promise<void> {
      try {
        if (this.state.user) {
          quizData.quiz.creator = this.state.user;
        }

        console.log('store upload quiz')
        console.log(quizData.quiz.img)

        const uploadedData = {
          name: quizData.quiz.name,
          creator: quizData.quiz.creator,
          quiz: quizData.quiz.quiz,
          img: quizData.quiz.img,
        } as Quiz;

        console.log('store upload quiz img set')
        console.log(uploadedData.img)


        const response = await post('/user/uploadQuiz', quizData.quiz);
      } catch (error: any) {
        commit('setErrorMessage', error.message);
      }
    },


    logout({ commit }): void {
      commit('clearUser');
      router.push('/signup');
    },
    clearErrorMessage({ commit }): void {
      commit('clearErrorMessage');
    }
  },
  getters: {
    isAuthenticated: (state: AuthState): boolean => state.user !== null,
    getUser: (state: AuthState): User | null => state.user,
    getErrorMessage: (state: AuthState): string | null => state.errorMessage,
    getToken: (state: AuthState): string | null => state.user?.token ?? null,
  },
});

export default store;
