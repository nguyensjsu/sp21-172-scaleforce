import { useEffect, useState } from 'react';

import Table from '../components/Table';
import { fetchAppointments } from '../services/appointments';

const serviceString = {
  TRIM: 'Trim',
  SHAVE: 'Shave',
  CUT_AND_BEARD: 'Haircut and Beard',
};

const columns = [
  {
    Header: 'Customer',
    accessor: 'bookedUserId',
  },
  {
    Header: 'Date',
    accessor: 'date',
  },
  {
    Header: 'Barber',
    accessor: 'barber',
  },
  {
    Header: 'Start Time',
    accessor: 'startTime',
  },
  {
    Header: 'End Time',
    accessor: 'endTime',
  },
  {
    Header: 'Service',
    accessor: 'service',
  },
  {
    Header: 'Delete',
    accessor: (str) => 'delete',
  },
];

const Appointments = () => {
  const [appointments, setAppointments] = useState([]);
  useEffect(() => {
    getAppointments();
  }, []);
  async function getAppointments() {
    const data = await fetchAppointments();
    data.forEach((app) => {
      app.date = app.startDate.split(' ')[0];
      app.startTime = app.startDate.split(' ')[1];
      app.endTime = app.endDate.split(' ')[1];
      if (!app.bookedUserId) app.bookedUserId = 'OPEN';
      app.service = serviceString[app.service];
    });
    console.log(data);
    setAppointments(data);
  }
  return (
    <div className="flex justify-center content-center">
      <Table data={appointments} columns={columns} />
    </div>
  );
};

export default Appointments;
