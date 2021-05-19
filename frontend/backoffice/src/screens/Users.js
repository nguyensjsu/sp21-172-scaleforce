import { useEffect, useState } from 'react';
import Table from '../components/Table';
import { fetchUsers } from '../services/users';

const columns = [
  {
    Header: 'Name',
    accessor: 'name',
  },
  {
    Header: 'Email',
    accessor: 'email',
  },
  {
    Header: 'Appointments',
    accessor: 'id',
  },
];

const Users = () => {
  const [users, setUsers] = useState([]);
  useEffect(() => {
    getUsers();
  }, []);
  async function getUsers() {
    const data = await fetchUsers();
    data.forEach((user) => {
      user.name = user.email.split('@')[0];
    });
    setUsers(data);
  }
  return (
    <div className="flex justify-center content-center">
      <Table data={users} columns={columns} />
    </div>
  );
};

export default Users;
