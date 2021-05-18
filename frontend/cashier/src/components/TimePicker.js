import { useEffect, useState } from 'react';

const TimePicker = ({ setTime }) => {
  const [hour, setHour] = useState('1');
  const [minute, setMinute] = useState('00');
  const [period, setPeriod] = useState('AM');

  useEffect(() => {
    setTime(hour + ':' + minute + period);
  }, [hour, minute, period]);

  return (
    <div className="mt-2 p-5 bg-white rounded-lg shadow-xl">
      <div className="flex">
        <select
          name="hours"
          className="bg-transparent text-xl appearance-none outline-none"
          onChange={({ target: { value } }) => setHour(value)}
        >
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5</option>
          <option value="6">6</option>
          <option value="7">7</option>
          <option value="8">8</option>
          <option value="9">9</option>
          <option value="10">10</option>
          <option value="11">10</option>
          <option value="12">12</option>
        </select>
        <span className="text-xl mr-3">:</span>
        <select
          name="minutes"
          className="bg-transparent text-xl appearance-none outline-none mr-4"
          onChange={({ target: { value } }) => setMinute(value)}
        >
          <option value="00">00</option>
          <option value="00">15</option>
          <option value="00">30</option>
          <option value="30">45</option>
        </select>
        <select
          name="ampm"
          className="bg-transparent text-xl appearance-none outline-none"
          onChange={({ target: { value } }) => setPeriod(value)}
        >
          <option value="AM">AM</option>
          <option value="PM">PM</option>
        </select>
      </div>
    </div>
  );
};

export default TimePicker;
