import { useEffect, useState } from 'react';
import moment from 'moment';

import Table from '../components/Table';
import { fetchAppointments, bookAppointment } from '../services/appointments';

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
  {
    Header: 'Book',
    accessor: (item) => {
      return (
        <div>
          <a className="text-green-700" href="#" onClick={item.bookAppointment}>
            Book me
          </a>
        </div>
      );
    },
  },
];

const serviceString = {
  TRIM: 'Trim',
  SHAVE: 'Shave',
  CUT_AND_BEARD: 'Haircut and Beard',
};

const Appointments = () => {
  const [appointments, setAppointments] = useState([]);
  useEffect(() => {
    getAppointments();
  }, []);

  async function handleBookAppointment(aptId) {
    // ADD STRIPE HERE
    if (true) {
      const res = await bookAppointment(aptId);
      console.log(res);
    }
  }

  async function getAppointments() {
    const data = await fetchAppointments();
    if (data) {
      data.forEach((app) => {
        let date = moment(app.startDate.split(' ')[0]);
        app.startDate = date.format('MM/DD @ HH:MM');
        if (!app.bookedUserId) app.bookedUserId = 'OPEN';
        app.service = serviceString[app.service];
        app.bookAppointment = () => handleBookAppointment(app.id);
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

export default Appointments;
