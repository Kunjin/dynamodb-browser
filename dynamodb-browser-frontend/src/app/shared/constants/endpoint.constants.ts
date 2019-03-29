import { environment } from '../../../environments/environment';

export const REST_ENDPOINT = environment.api.endpoint;
export const AWS_REGIONS_URL = REST_ENDPOINT + '/regions';
export const AWS_PROFILE_URL = REST_ENDPOINT + '/awsProfiles';
export const SETTINGS_URL = REST_ENDPOINT + '/settings';
export const TABLES_URL = REST_ENDPOINT + '/tables'
export const RECORDS_URL = REST_ENDPOINT + '/records';
export const OPERATIONS_URL = REST_ENDPOINT + '/operations';
