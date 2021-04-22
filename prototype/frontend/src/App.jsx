import './App.css';

import axios from 'axios';
import React from 'react';
import { useMutation, useQuery, useQueryClient } from 'react-query';

import TodoTable from './components/TodoTable';

const TodoForm = () => {
  const queryClient = useQueryClient();

  const addTodo = (todo) => axios.post('/todos', todo);

  const mutation = useMutation(addTodo, {
    onMutate: async (newTodo) => {
      await queryClient.cancelQueries('todos');
      const previousTodos = queryClient.getQueryData('todos');
      queryClient.setQueryData('todos', (old) => [...old, newTodo]);
      return { previousTodos };
    },
    onError: (err, newTodo, context) => {
      queryClient.setQueryData('todos', context.previousTodos);
    },
    onSettled: () => {
      queryClient.invalidateQueries('todos');
    },
  });

  const onSubmit = (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);
    const newTodo = {
      id: -1,
      title: formData.get('title'),
      description: formData.get('description'),
    };

    mutation.mutate(newTodo);
  };

  return (
    <form onSubmit={onSubmit}>
      <label htmlFor="title">
        <input type="text" name="title" placeholder="title" required />
      </label>
      <label htmlFor="description">
        <input type="text" name="description" placeholder="description" required />
      </label>
      <input type="submit" />
    </form>
  );
};

function App() {
  const todosQuery = useQuery('todos', async () => {
    const response = await axios.get('/todos');
    return response.data;
  });

  if (todosQuery.isLoading) {
    return (
      <span>Loading...</span>
    );
  }

  if (todosQuery.isError) {
    return (
      <span>
        Error:
        {' '}
        {todosQuery.error.message}
      </span>
    );
  }

  return (
    <>
      <TodoForm />
      <TodoTable
        todos={todosQuery.data}
      />
    </>
  );
}

export default App;
