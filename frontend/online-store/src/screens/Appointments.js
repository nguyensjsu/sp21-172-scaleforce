import Table from '../components/Table';

const data = [
  {
    col1: 'Steve P',
    col2: '11:30am',
    col3: 'Haircut + Beardtrim',
  },
  {
    col1: 'Steve P',
    col2: '9:30am',
    col3: 'Haircut',
  },
];

const columns = [
  {
    Header: 'Barber',
    accessor: 'col1',
  },
  {
    Header: 'Time',
    accessor: 'col2',
  },
  {
    Header: 'Service',
    accessor: 'col3',
  },
];

const Appointments = () => {
  return (
    <div className="flex justify-center content-center">
      <Table data={data} columns={columns} />
    </div>
  );
};

export default Appointments;
