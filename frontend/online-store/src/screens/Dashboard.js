import { useEffect, useState } from 'react';
import moment from 'moment';

import Table from '../components/Table';
import { fetchUserAppointments } from '../services/appointments';

const columns = [
  {
    Header: 'Name',
    accessor: 'bookedUserId',
  },
  {
    Header: 'Barber',
    accessor: 'barber',
  },
  {
    Header: 'Time',
    accessor: 'startDate',
  },
  {
    Header: 'Service',
    accessor: 'service',
  },
];

const serviceString = {
  TRIM: 'Trim',
  SHAVE: 'Shave',
  CUT_AND_BEARD: 'Haircut and Beard',
};

const Dashboard = () => {
  const [appointments, setAppointments] = useState([]);
  useEffect(() => {
    getAppointments();
  }, []);
  async function getAppointments() {
    const data = await fetchUserAppointments();
    if (data) {
      data.forEach((app) => {
        let date = moment(app.startDate.split(' ')[0]);
        app.startDate = date.format('MM/DD @ HH:MM');
        if (!app.bookedUserId) app.bookedUserId = 'OPEN';
        app.service = serviceString[app.service];
      });
      setAppointments(data);
    }
  }
  return (
    <div className="flex justify-center content-center">
      <Table data={appointments} columns={columns} />
    </div>
  );
};

export default Dashboard;
