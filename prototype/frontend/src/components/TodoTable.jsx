/* eslint-disable react/prop-types,no-unused-vars */
import React, { useEffect, useState } from 'react';

// TODO proptypes?

const TodoRow = ({ todo }) => (
  <tr>
    <td>{todo.title}</td>
    <td>{todo.description}</td>
    <td>{todo.isCompleted ? 'Yes!' : 'No...'}</td>
  </tr>
);

function TodoTable({ initialTodos }) {
  const [todos, setTodos] = useState(initialTodos);
  const [todoRows, setTodoRows] = useState([]);

  const initializeTodoRows = async () => {
    setTodoRows(
      todos.map((todo) => (
        <TodoRow
          key={todo.id}
          todo={todo}
        />
      )),
    );
  };

  useEffect(() => {
    initializeTodoRows();
  }, [todos]);

  return (
    <table>
      <thead>
        <tr>
          <th>Title</th>
          <th>Description</th>
          <th>Completed?</th>
        </tr>
      </thead>
      <tbody>
        {todoRows}
      </tbody>
    </table>
  );
}

export default TodoTable;
