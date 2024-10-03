import type { User } from '@/types';
import { post } from '@/services/ApiService';

export async function login(credentials: { username: string; password: string }): Promise<User> {
    const response = await post('/user/login', credentials);

    const user: User = {
        id: response.id,
        token: response.token,
        username: response.username,
        password: response.password,
        image: response.picture,
    };

    return user;
}

export async function signup(credentials: { username: string; password: string; image: string }): Promise<User> {
    const response = await post('/user/signup', credentials);

    const newUser: User = {
        id: response.id,
        token: response.token,
        username: response.username,
        password: response.password,
        image: response.picture,
    };

    return newUser;
}