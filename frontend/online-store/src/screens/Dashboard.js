import Table from '../components/Table';

const data = [
  {
    col1: '9',
    col2: '4',
    col3: '$230.00',
  },
];

const columns = [
  {
    Header: 'Users',
    accessor: 'col1',
  },
  {
    Header: 'Appointments',
    accessor: 'col2',
  },
  {
    Header: 'Income',
    accessor: 'col3',
  },
];

const Dashboard = () => {
  return (
    <div className="flex justify-center content-center">
      <Table data={data} columns={columns} />
    </div>
  );
};

export default Dashboard;
