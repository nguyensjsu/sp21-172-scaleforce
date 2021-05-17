import axios from 'axios';

// axios.defaults.baseURL = 'https://api.scaleforce.dev/';
const url = 'http://api.scaleforce.dev/';
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=utf-8';

export const fetchUsers = async () => {
  const token = JSON.parse(sessionStorage.getItem('user'));
  try {
    const res = await axios({
      method: 'get',
      url: url + 'admin/appointments',
      headers: { Authorization: `Bearer ${token}` },
    });
    console.log(res);
  } catch (e) {
    alert(e);
  }
};
