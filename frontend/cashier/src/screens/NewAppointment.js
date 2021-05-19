import React, { useState } from 'react';
import TextInput from '../components/TextInput';
import Button from '../components/Button';
import DatePicker from '../components/DatePicker';
import TimePicker from '../components/TimePicker';
import ServicePicker from '../components/ServicePicker';
import { addAppointment } from '../services/appointments';
import moment from 'moment';

const NewAppointment = ({ history }) => {
  const [barber, setBarber] = useState('');
  const [date, setDate] = useState(new Date());
  const [time, setTime] = useState('1:00AM'); // ;)
  const [service, setService] = useState('TRIM');

  async function handleSubmit() {
    const startDate = moment(date);

    addAppointment({
      service,
      startDate: startDate.format('yyyy-MM-DD HH:mm:ss ZZ'),
      endDate: startDate.add('1', 'H').format('yyyy-MM-DD HH:mm:ss ZZ'),
      barber,
      booked: null,
    });
    history.push('appointments');
  }

  return (
    <div className="flex h-screen">
      <div className="border-4 border-black m-auto">
        <div className="p-2">
          <TextInput
            label="Barber"
            type="name"
            placeholder="Barber Name"
            onChange={({ target: { value } }) => setBarber(value)}
          />
        </div>
        <div className="p-2">
          <TimePicker setTime={(time) => setTime(time)} />
          <DatePicker pickDate={(date) => setDate(date)} />
        </div>

        <div className="p-2">
          <ServicePicker setService={setService} />
        </div>
        <div className="flex justify-center p-2">
          <Button label="Submit" variant="primary" onClick={handleSubmit} />
        </div>
      </div>
    </div>
  );
};

export default NewAppointment;
