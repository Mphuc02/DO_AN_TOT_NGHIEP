import React, {useEffect } from 'react';

const Toast = ({ message, type, onClose }) => {
    useEffect(() => {
        console.log('ok')
        const timer = setTimeout(() => {
            onClose();
        }, 3000); // Toast sẽ tự động đóng sau 3 giây

        return () => clearTimeout(timer);
    }, [message, onClose]);

    return (
        <div
            className={`fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 p-4 rounded-lg shadow-lg ${
                type === 'error' ? 'bg-red-500' : 'bg-green-500'
            } text-white w-[50vw] h-[40vh]`}>
            <div className="flex items-center justify-center h-full">
                <span className="text-xl">{message}</span>
                <button
                    onClick={onClose}
                    className="absolute top-2 right-2 text-2xl font-semibold"
                >
                    &times;
                </button>
            </div>
        </div>
    );
};

export default Toast;
