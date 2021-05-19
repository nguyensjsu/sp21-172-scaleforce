import axios from 'axios';
import { validate } from './auth';

const AUTH_URL = 'https://auth.scaleforce.dev/';

export const fetchUsers = async () => {
  const validated = await validate();
  if (!validated) return;
  const { Authorization } = validated.config.headers;
  if (!Authorization) return;
  try {
    const res = await axios.get(AUTH_URL + 'users', {
      headers: { Authorization },
    });
    return res.data;
  } catch (e) {
    console.log(e);
  }
};
