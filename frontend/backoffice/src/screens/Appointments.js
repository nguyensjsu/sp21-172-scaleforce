import Table from '../components/Table';

const data = [
  {
    col1: 'Jack Gisel',
    col2: 'Steve P',
    col3: '11:30am',
    col4: 'Haircut + Beardtrim',
  },
  {
    col1: 'Jesus',
    col2: 'Steve P',
    col3: '9:30am',
    col4: 'Haircut',
  },
];

const columns = [
  {
    Header: 'Name',
    accessor: 'col1',
  },
  {
    Header: 'Barber',
    accessor: 'col2',
  },
  {
    Header: 'Time',
    accessor: 'col3',
  },
  {
    Header: 'Service',
    accessor: 'col4',
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
