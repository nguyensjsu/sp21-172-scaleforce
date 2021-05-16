import React from 'react';
import { useTable } from 'react-table';

const Table = ({ data, columns }) => {
  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    rows,
    prepareRow,
  } = useTable({ columns, data });

  return (
    <table {...getTableProps()} className="min-w-max w-full table-auto">
      <thead>
        {headerGroups.map((headerGroup) => (
          <tr
            className="bg-gray-200 text-gray-600 uppercase text-sm leading-normal"
            {...headerGroup.getHeaderGroupProps()}
          >
            {headerGroup.headers.map((column) => (
              <th {...column.getHeaderProps()} className="py-3 px-6 text-left">
                {column.render('Header')}
              </th>
            ))}
          </tr>
        ))}
      </thead>
      <tbody
        className="text-gray-600 text-sm font-light"
        {...getTableBodyProps()}
      >
        {rows.map((row) => {
          prepareRow(row);
          return (
            <tr
              className="border-b border-gray-200 hover:bg-gray-100"
              {...row.getRowProps()}
            >
              {row.cells.map((cell) => {
                return (
                  <td
                    {...cell.getCellProps()}
                    className="py-3 px-6 text-left whitespace-nowrap"
                  >
                    {cell.render('Cell')}
                  </td>
                );
              })}
            </tr>
          );
        })}
      </tbody>
    </table>
  );
};

export default Table;
