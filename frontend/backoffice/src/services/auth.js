import axios from 'axios';

const AUTH_URL = 'https://auth.scaleforce.dev/';

export const login = async (email, password) => {
  const res = await axios.post(AUTH_URL + 'auth', {
    email,
    password,
  });

  if (res.data) {
    localStorage.setItem('user', JSON.stringify(res.data.jwt));
  }

  return res.data.uid;
};

export const validate = async () => {
  const token = JSON.parse(localStorage.getItem('user'));
  const res = await axios.post(
    AUTH_URL + 'validate',
    {},
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return res;
};
export const logout = () => {
  localStorage.removeItem('user');
};

export const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem('user'));
};
