import { useState } from 'react';

const Alert = ({ message }) => {
    const [visible, setVisible] = useState(true);

    const handleClose = () => {
        setVisible(false);
    };

    return visible ? (
        <div className="alert">
            <p>{message}</p>
            <button onClick={handleClose}>Close</button>
        </div>
    ) : null;
};

export default Alert;
