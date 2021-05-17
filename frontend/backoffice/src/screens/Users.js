import { useEffect } from 'react';
import Table from '../components/Table';
import { validate } from '../services/auth';
import { fetchUsers } from '../services/users';

const data = [
  {
    col1: 'Jack Gisel',
    col2: 'jackgisel@gmail.com',
    col3: '23',
  },
  {
    col1: 'Jesus',
    col2: 'jesus@gmail.com',
    col3: '10',
  },
];

const columns = [
  {
    Header: 'Name',
    accessor: 'col1',
  },
  {
    Header: 'Email',
    accessor: 'col2',
  },
  {
    Header: 'Appoints',
    accessor: 'col3',
  },
];

const Users = () => {
  useEffect(() => {
    fetchUsers();
    // validate();
  }, []);
  return (
    <div className="flex justify-center content-center">
      <Table data={data} columns={columns} />
    </div>
  );
};

export default Users;
