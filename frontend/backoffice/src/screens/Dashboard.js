import { useEffect, useState } from 'react';
import Table from '../components/Table';
import { fetchUsers } from '../services/users';
import { fetchAppointments } from '../services/appointments';

const columns = [
  {
    Header: 'Users',
    accessor: 'users',
  },
  {
    Header: 'Appointments',
    accessor: 'appointments',
  },
  {
    Header: 'Income',
    accessor: 'income',
  },
];

const Dashboard = () => {
  const [data, setData] = useState([]);
  useEffect(() => {
    getData();
  }, []);
  async function getData() {
    const userData = await fetchUsers();
    const appointmentData = await fetchAppointments();
    setData([
      {
        users: userData.length,
        appointments: appointmentData.length,
        income: '$429',
      },
    ]);
  }
  return (
    <div className="flex justify-center content-center">
      <Table data={data} columns={columns} />
    </div>
  );
};

export default Dashboard;
