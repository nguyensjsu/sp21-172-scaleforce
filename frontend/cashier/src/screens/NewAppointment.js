import React from 'react';
import TextInput from "../components/TextInput";

const NewAppointment = () => {
  return (
    <div>
      <div>
        <TextInput label="Barber" type="name" placeholder="Barber Name" />
      </div>
      <div>
        <TextInput label="Start Time" type="string" placeholder="yyyy-mm-dd 00:00:00" />
      </div>
      <div>
        <TextInput label="End Time" type="string" placeholder="yyyy-mm-dd 00:00:00" />
      </div>
    </div>
  );
};

export default NewAppointment;