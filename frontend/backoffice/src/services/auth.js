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

export const login = (username, password) => {
  return fetch(API_URL + 'auth', {
    method: 'POST',
    mode: 'cors',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      email: username,
      password,
    }),
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
