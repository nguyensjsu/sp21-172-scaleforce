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

function TodoTable({ todos }) {
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
        {todos.map((todo) => (
          <TodoRow
            key={todo.id}
            todo={todo}
          />
        ))}
      </tbody>
    </table>
  );
}

export default TodoTable;
