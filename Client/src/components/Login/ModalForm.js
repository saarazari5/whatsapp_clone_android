import React, { useState, useEffect } from 'react';

const ModalForm = () => {
    const [username, setUsername] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [isForgotFormSubmitted, setIsForgotFormSubmitted] = useState(false);

    useEffect(() => {
        // Clear username error when the user types something new
        setError('');
    }, [username]);

    const isValidUsername = (value) => {
        // Regular expression pattern to validate username format
        const usernamePattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return usernamePattern.test(value);
    };

    const validateForgotForm = () => {
        let error = '';
        // Validate username
        if (!username) {
            error = 'Username is required';
        } else if (!isValidUsername(username)) {
            error = 'Invalid username format';
        }
        // Set the errors
        setError(error);

        // Return true if there are no errors
        return error.length === 0;
    };

    const handleForgotSubmit = async (e) => {
        e.preventDefault();
        setIsForgotFormSubmitted(true);
        // validate input
        if (!validateForgotForm()) {
            return;
        }
        // Create request for new password for given username
        const forgotRequest = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username: username })
        };
        setUsername('');
        // Send the request
        try {
            let res = await fetch('http://localhost:5000/api/Token/forgot', forgotRequest);
            if (res.status === 200) {
                const messageRes = await res.json();
                setSuccess(messageRes.message);
            } else if (res.status === 400) {
                const messageRes = await res.json();
                setError(messageRes.message);
            } else {
                throw new Error('Unexpected Server Error');
            }
        } catch (error) {
            console.log(error);
            setError(error);
        }
    };

    return (
        <form onSubmit={handleForgotSubmit}>
            {/* display the success message */}
            {success && <div className="alert alert-success mb-2">{success}</div>}
            <div className="modal-body">
                <div className={`from-group validate-input ${isForgotFormSubmitted && error ? 'was-validated' : ''}`}>
                    <input type="email" className={`form-control${error ? 'input-error' : ''}`} id="email" name="username"
                    placeholder="Enter your username" onChange={(e) => setUsername(e.target.value)}></input>
                    {/* display username error message */}
                    {error && (
                        <div className="invalid-feedback" style={{ display: 'block' }}>
                            {error}
                        </div>
                    )}
                </div>
            </div>
            <div className="modal-footer">
                <button type="submit" className="btn btn-primary" id="resetBtn">Reset Password</button>
            </div>
        </form>
    );
};

export default ModalForm;
