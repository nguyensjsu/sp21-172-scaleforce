import axios from 'axios';

// export const register = (username, email, password) => {
//   return fetch(API_URL + 'auth', {
//     method: 'POST',
//     headers: {
//       'Content-Type': 'application/json',
//     },
//     body: {
//       username,
//       email,
//       password,
//     },
//   });
// };

const url = 'https://auth.scaleforce.dev/';

export const login = async (email, password) => {
  const res = await axios({
    method: 'post',
    url: url + 'auth',
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

export const validate = async () => {
  const res = await axios({
    method: 'post',
    url: url + 'validate',
    headers: {
      Authorization: `Bearer ${JSON.parse(localStorage.getItem('user'))}`,
    },
  });

  console.log(res);
};
export const logout = () => {
  localStorage.removeItem('user');
};

export const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem('user'));
};
