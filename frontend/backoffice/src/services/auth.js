import axios from 'axios';
const API_URL = 'https://auth.scaleforce.dev/';

export const register = (username, email, password) => {
  return fetch(API_URL + 'auth', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: {
      username,
      email,
      password,
    },
  });
};

export const login = async (email, password) => {
  const res = await axios({
    method: 'post',
    url: 'https://auth.scaleforce.dev/auth',
    data: {
      email,
      password,
    },
  });

  if (res.data) {
    localStorage.setItem('user', JSON.stringify(res.data.jwt));
  }

  return res.data.uid;
};

export const logout = () => {
  localStorage.removeItem('user');
};

export const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem('user'));
};
