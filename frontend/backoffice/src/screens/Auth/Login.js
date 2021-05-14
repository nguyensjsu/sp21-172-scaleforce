import Button from '../../components/Button';
import TextInput from '../../components/TextInput';
import { useForm } from 'react-hook-form';
import { login } from '../../services/auth';

export default function Login() {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();
  const onSubmit = async (data) => {
    const res = await login(data.email, data.password);
    console.log(res);
  };

  return (
    <div className="flex justify-center my-20">
      <div className="bg-gray-100 w-2/5 p-5">
        <form onSubmit={handleSubmit(onSubmit)}>
          <TextInput
            label="Email"
            // type="email"
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
          <div className="pt-3">
            <Button label="Submit" variant="primary" />
          </div>
        </form>
      </div>
    </div>
  );
}
