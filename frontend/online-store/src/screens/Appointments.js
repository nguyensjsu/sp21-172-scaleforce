import Table from '../components/Table';

const data = [
  {
    col1: 'Steve P',
    col2: '5/16/21',
    col3: '11:30am',
    col4: '12:30pm',
    col5: 'Haircut + Beardtrim',
  },
  {
    col1: 'Steve P',
    col2: '3/16/21',
    col3: '9:30am',
    col4: '10:00am',
    col5: 'Haircut',
  },
];

const columns = [
  {
    Header: 'Barber',
    accessor: 'col1',
  },
  {
    Header: 'Date',
    accessor: 'col2',
  },
  {
    Header: 'Start Time',
    accessor: 'col3',
  },
  {
    Header: 'End Time',
    accessor: 'col4',
  },
  {
    Header: 'Service',
    accessor: 'col5',
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
