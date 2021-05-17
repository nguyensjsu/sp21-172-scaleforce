import React from 'react';
import logo from '../components/card.jpg'
import Table from '../components/Table'

const data = [
  {
    col1: '156-875',
    col2: '$20.00',
  },
];

const columns = [
  {
    Header: 'Card Number',
    accessor: 'col1'
  },
  {
    Header: 'Balance',
    accessor: 'col2',
  },
];

const Card = () => {
  return (
    <div>
      <img class="mx-auto p-5" src={logo} alt="card"/>
      <Table data={data} columns={columns}/>
    </div>
  );
};

export default Card;