import axios from 'axios';
import { API_BASE_URL } from '../api.config';
import { AxiosError } from 'axios';
import store from '@/store';

/**
 * Makes a GET request to the specified endpoint with optional parameters.
 * @param endpoint - The endpoint to send the GET request to.
 * @param parameters - Optional parameters to include in the request URL.
 * @returns A Promise that resolves to the response data.
 * @throws An error if the request fails or if the response contains an error message.
 */
export async function get(endpoint: string, parameters: string | string[] = ""): Promise<any> {
  try {
    const response = await axios.get(buildApiUrl(endpoint, parameters));
    return response.data;
  } catch (error) {
    const errorResponse = error as AxiosError<any>;
    throw new Error(errorResponse.response?.data?.message || 'Failed to make POST request');
  }
}

/**
 * Sends a POST request to the specified endpoint with the provided data and optional parameters.
 * @param endpoint - The endpoint to send the request to.
 * @param data - The data to send in the request body.
 * @param parameters - Optional query parameters to include in the request URL.
 * @returns A Promise that resolves to the response data from the server.
 * @throws An error if the request fails or if the server returns an error message.
 */
export async function post(endpoint: string, data: any, parameters: string | string[] = ""): Promise<any> {
  try {
    const response = await axios.post(buildApiUrl(endpoint, parameters), data);
    return response.data;
  } catch (error) {
    const errorResponse = error as AxiosError<any>;
    throw new Error(errorResponse.response?.data?.message || 'Failed to make POST request');
  }
}

/**
 * Makes a POST request to the specified endpoint without a request body.
 * @param endpoint - The endpoint to send the request to.
 * @param parameters - Optional query parameters to include in the request URL.
 * @returns A Promise that resolves to the response data.
 * @throws An error if the request fails or if the response contains an error message.
 */
export async function postWithoutBody(endpoint: string, parameters: string | string[] = ""): Promise<any> {
  try {
    const response = await axios.post(buildApiUrl(endpoint, parameters));
    return response.data;
  } catch (error) {
    const errorResponse = error as AxiosError<any>;
    throw new Error(errorResponse.response?.data?.message || 'Failed to make POST request');
  }
}

/**
 * Builds the API URL with the given endpoint and parameters.
 * @param endpoint - The API endpoint.
 * @param parameters - The parameters to be included in the URL.
 * @returns The formatted API URL.
 */
function buildApiUrl(endpoint: string, parameters: string | string[] = ""): string {

  if (parameters === "") {
    return `${API_BASE_URL}${endpoint}?token=${store.getters.getToken}`;
  }

  const parameterArray = parameters.constructor === Array ? parameters.join('&') : [parameters]
  return `${API_BASE_URL}${endpoint}?token=${store.getters.getToken}&${parameterArray}`;
}
