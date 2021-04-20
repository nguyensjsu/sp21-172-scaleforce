import './App.css';

import axios from 'axios';
import React from 'react';
import { useQuery } from 'react-query';

import TodoTable from './components/TodoTable';

function App() {
  const todos = useQuery('todos', async () => {
    const response = await axios.get('/todos');
    return response.data;
  });

  if (todos.isLoading) {
    return (
      <span>Loading...</span>
    );
  }

  if (todos.isError) {
    return (
      <span>
        Error:
        {' '}
        {todos.error.message}
      </span>
    );
  }

  return (
    <>
      <TodoTable
        todos={todos.data}
      />
    </>
  );
}

export default App;
