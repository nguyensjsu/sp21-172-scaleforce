import React from 'react';

export default function Button({ variant, label }) {
  return (
    <button className="bg-blue text-white font-bold py-2 px-4 border-b-4 border-blue-dark rounded">
      {label}
    </button>
  );
}
