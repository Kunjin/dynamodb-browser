import { environment } from '../../../environments/environment';

export const REST_ENDPOINT = environment.api.endpoint;
export const AWS_REGIONS = REST_ENDPOINT + "/regions";
export const AWS_PROFILE = REST_ENDPOINT + "/awsProfiles";
export const SETTINGS = REST_ENDPOINT + "/settings";
