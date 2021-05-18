const serviceString = {
  TRIM: 'Trim',
  SHAVE: 'Shave',
  CUT_AND_BEARD: 'Haircut and Beard',
};

const TimePicker = ({ setService }) => {
  return (
    <div className="mt-2 p-5 bg-white rounded-lg shadow-xl">
      <div className="flex">
        <select
          name="hours"
          className="bg-transparent text-xl appearance-none outline-none"
          onChange={({ target: { value } }) => setService(value)}
        >
          {Object.keys(serviceString).map((service) => (
            <option value={service}>{serviceString[service]}</option>
          ))}
        </select>
      </div>
    </div>
  );
};

export default TimePicker;
