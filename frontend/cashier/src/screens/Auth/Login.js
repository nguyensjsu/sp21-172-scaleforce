import React from 'react';
import Button from '../../components/Button';

import TextInput from '../../components/TextInput';

export default function Home() {
  return (
    <div className="flex justify-center my-20">
      <div className="bg-gray-100 w-2/5 p-5">
        <TextInput
          label="Email"
          type="email"
          placeholder="email@thebarbershop.com"
        />
        <div className="pt-3">
          <TextInput label="Password" type="password" placeholder="********" />
        </div>
        <div className="pt-3">
          <Button label="Submit" variant="primary" />
        </div>
      </div>
    </div>
  );
}
