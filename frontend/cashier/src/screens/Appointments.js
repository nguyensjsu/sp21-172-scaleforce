import Table from '../components/Table';

const data = [
  {
    col1: 'Jack Gisel',
    col2: "Today",
    col3: 'Steve P',
    col4: '11:30am',
    col5: "12:30pm",
    col6: 'Haircut + Beardtrim',
  },
  {
    col1: 'Jesus',
    col2: "Today",
    col3: 'Steve P',
    col4: '9:30am',
    col5: "10:00am",
    col6: 'Haircut',
  },
];

const columns = [
  {
    Header: 'Customer',
    accessor: 'col1',
  },
  {
    Header: 'Date',
    accessor: 'col2',
  },
  {
    Header: 'Barber',
    accessor: 'col3',
  },
  {
    Header: 'Start Time',
    accessor: 'col4',
  },
  {
    Header: 'End Time',
    accessor: 'col5',
  },
  {
    Header: 'Service',
    accessor: 'col6',
  },
  {
    Header: 'Delete',
    accessor: (str) => 'delete'
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
