const API_URL = 'http://auth.scaleforce.dev/';

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

export const login = (username, password) => {
  return fetch(API_URL + 'auth', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: {
      username,
      password,
    },
  }).then((response) => {
    if (response.data.accessToken) {
      localStorage.setItem('user', JSON.stringify(response.data));
    }

    return response.data;
  });
};

export const logout = () => {
  localStorage.removeItem('user');
};

export const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem('user'));
};
