import { forwardRef } from 'react';

const TextInput = forwardRef(
  ({ label, type, icon, placeholder, ...rest }, ref) => {
    return (
      <div>
        <label className="block text-sm font-medium text-gray-700">
          {label}
        </label>
        <div className="mt-1 relative rounded-md shadow-sm">
          {icon && (
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              {icon}
            </div>
          )}
          <input
            ref={ref}
            type={type}
            className={
              icon
                ? 'focus:ring-indigo-500 focus:border-indigo-500 block w-full pr-12 sm:text-sm border-gray-300 rounded-md pl-7'
                : 'focus:ring-indigo-500 focus:border-indigo-500 block w-full pr-12 sm:text-sm border-gray-300 rounded-md'
            }
            placeholder={placeholder}
            {...rest}
          />
        </div>
      </div>
    );
  }
);

export default TextInput;
