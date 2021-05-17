import axios from 'axios';
import { validate } from './auth';

const API_URL = 'https://api.scaleforce.dev/';

export const fetchAppointments = async () => {
  const validated = await validate();
  const { Authorization } = validated?.config?.headers;
  if (!Authorization) return;
  try {
    const res = await axios.get(API_URL + 'admin/appointments', {
      headers: { Authorization },
    });
    return res.data;
  } catch (e) {
    console.log(e);
  }
};

export const addAppointment = async (appointment) => {
  const validated = await validate();
  const { Authorization } = validated?.config?.headers;
  console.log(appointment);
  if (!Authorization) return;
  try {
    const res = await axios.post(API_URL + 'admin/appointment', appointment, {
      headers: { Authorization },
    });
    return res.data;
  } catch (e) {
    console.log(e);
  }
};
