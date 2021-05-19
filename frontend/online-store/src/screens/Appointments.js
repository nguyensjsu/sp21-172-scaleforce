import { useEffect, useState } from 'react';
import moment from 'moment';
import { loadStripe } from '@stripe/stripe-js';

import Table from '../components/Table';
import { fetchAppointments, bookAppointment } from '../services/appointments';
import axios from 'axios';

const stripePromise = loadStripe(
  'pk_test_51C7V6ZGmuZOcQR441PEZobTrV6fZHNQ1eFuPPMSjgIRc63S6M0dFD4rCOutmljkpDN0zjpwHLpGZxulqwsNSLTbD00n6bSmNC3'
);

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
    accessor: 'readableService',
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

const Appointments = ({ history }) => {
  const [appointments, setAppointments] = useState([]);
  useEffect(() => {
    getAppointments();
  }, []);

  async function handleBookAppointment(aptId, service) {
    const stripe = await stripePromise;
    console.log(service);
    const session = await axios.post(
      `https://scaleforce-stripe-api.herokuapp.com/create-checkout-session?service=${service}&apptid=${aptId}`
    );
    const result = await stripe.redirectToCheckout({
      sessionId: session.data.id,
    });
    if (result.error) {
      alert(result.error);
    }
  }

  async function book(aptId) {
    const res = await bookAppointment(aptId);
    if (res === 'Success') {
      history.push('dashboard');
    }
  }

  useEffect(() => {
    // Check to see if this is a redirect back from Checkout
    const query = new URLSearchParams(window.location.search);

    if (query.get('success')) {
      book(query.get('apptid'));
      alert('Thanks for booking an appointment!');
    }

    if (query.get('canceled')) {
      alert('Order was cancelled!');
    }
  }, []);

  async function getAppointments() {
    const data = await fetchAppointments();
    if (data) {
      data.forEach((app) => {
        let date = moment(app.startDate.split(' ')[0]);
        app.startDate = date.format('MM/DD @ HH:MM');
        if (!app.bookedUserId) app.bookedUserId = 'OPEN';
        app.bookAppointment = () => handleBookAppointment(app.id, app.service);
        app.readableService = serviceString[app.service];
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
