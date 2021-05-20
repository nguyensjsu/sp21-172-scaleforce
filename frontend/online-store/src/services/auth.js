import axios from 'axios';

const AUTH_URL = 'https://auth.scaleforce.dev/';

export const login = async (email, password) => {
  console.log('we are here');
  const res = await axios.post(AUTH_URL + 'auth', {
    email,
    password,
  });

  if (res.data) {
    localStorage.setItem('user', JSON.stringify(res.data.jwt));
    localStorage.setItem('uid', JSON.stringify(res.data.uid));
  }

  return res.data.uid;
};

export const signup = async (email, password) => {
  return await axios.post(AUTH_URL + 'users', {
    email,
    password,
  });
};

export const validate = async () => {
  try {
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
  } catch (e) {
    if (e.response.status === 500) {
      logout();
      console.log('LOGOUT');
    }
    return;
  }
};

export const logout = () => {
  localStorage.removeItem('user');
};

export const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem('user'));
};
