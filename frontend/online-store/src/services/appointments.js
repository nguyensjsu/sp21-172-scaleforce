import axios from 'axios';
import moment from 'moment';
import { validate } from './auth';

const API_URL = 'https://api.scaleforce.dev/';

export const fetchAppointments = async () => {
  const validated = await validate();
  if (!validated) return;
  const { Authorization } = validated?.config?.headers;
  if (!Authorization) return;
  try {
    const res = await axios.get(API_URL + `user/appointments/open`, {
      headers: { Authorization },
    });
    return res.data;
  } catch (e) {
    console.log(e);
  }
};

export const fetchUserAppointments = async () => {
  const validated = await validate();
  if (!validated) return;
  const { Authorization } = validated?.config?.headers;
  if (!Authorization) return;
  const uid = JSON.parse(localStorage.getItem('uid'));
  try {
    const res = await axios.get(API_URL + `user/appointments/${uid}`, {
      headers: { Authorization },
    });
    return res.data;
  } catch (e) {
    console.log(e);
  }
};

export const bookAppointment = async (aptId) => {
  const validated = await validate();
  if (!validated) return;
  const { Authorization } = validated?.config?.headers;
  if (!Authorization) return;
  const uid = JSON.parse(localStorage.getItem('uid'));
  try {
    const res = await axios.patch(
      API_URL + `user/appointment/${aptId}`,
      {
        userId: uid,
      },
      {
        headers: { Authorization },
      }
    );
    return 'Success';
  } catch (e) {
    console.log(e);
  }
};
