import React from 'react';
import Button from '../../components/Button';
import TextInput from '../../components/TextInput';
import { useForm } from 'react-hook-form';
import { signup } from '../../services/auth';

export default function Signup({ onSignup, history }) {
  const { register, handleSubmit } = useForm();
  const onSubmit = async (data) => {
    const res = await signup(data.email, data.password);
    if (res) {
      history.push('/login');
    }
  };

  return (
    <div className="flex justify-center my-20">
      <div className="w-2/5">
        <h1 className="bg-gray-800 text-white font-bold text-center">Signup</h1>
        <div className="bg-gray-100 p-5">
          <form onSubmit={handleSubmit(onSubmit)}>
            <TextInput
              label="Email"
              placeholder="email@thebarbershop.com"
              {...register('email', { required: true })}
            />
            <div className="pt-3">
              <TextInput
                label="Password"
                type="password"
                placeholder="********"
                {...register('password', { required: true })}
              />
            </div>
            <div className="flex justify-center pt-3">
              <Button label="Submit" variant="primary" />
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
