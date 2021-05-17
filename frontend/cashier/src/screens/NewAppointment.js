import React from 'react';
import TextInput from "../components/TextInput";
import Button from "../components/Button";

const NewAppointment = () => {
  return (
    <div className="flex justify-center my-20">
      <div className="p-5">
        <TextInput label="Barber" type="name" placeholder="Barber Name" />
      </div>
      <div className="p-5">
        <TextInput label="Start Time" type="string" placeholder="yyyy-mm-dd 00:00:00" />
      </div>
      <div className="p-5">
        <TextInput label="End Time" type="string" placeholder="yyyy-mm-dd 00:00:00" />
      </div>
      <div className="p-5">
        <TextInput label="Service" type="string" placeholder="Enter Service" />
      </div>
      <Button label="Submit" variant="primary" />
    </div>
  );
};

export default NewAppointment;