import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import socket from '../../socket.js';

function LoginForm({ email, password, isLoading, setEmail, setPassword, handleLogin, handleForgotPassword, handlePasswordChange, passwordVisible, togglePasswordVisibility, setIsLoading }) {
    const [errors, setErrors] = useState({});
    const [error, setError] = useState('');
    const [isFormSubmitted, setIsFormSubmitted] = useState(false);

    useEffect(() => {
        // Clear email error when the user types something new
        setErrors((prevErrors) => ({
            ...prevErrors,
            email: '',
        }));

    }, [email]);

    useEffect(() => {
        // Clear password error when the user types something new
        setErrors((prevErrors) => ({
            ...prevErrors,
            password: '',
        }));

    }, [password]);

    const validateForm = () => {
        // errors object
        const errors = {};

        // Validate email
        if (!email) {
            errors.email = 'Email is required';
        } else if (!isValidEmail(email)) {
            errors.email = 'Invalid email format';
        }

        // Validate password
        if (!password) {
            errors.password = 'Password is required';
        } else if (password.length < 8) {
            errors.password = 'Password is less than 8 characters';
        }

        setErrors(errors);

        // Return true if there are no errors
        return Object.keys(errors).length === 0;
    };

    const isValidEmail = (value) => {
        // Regular expression pattern to validate email format
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailPattern.test(value);
    };

    const handleSubmit = async (e) => {
        console.log("inside LoginForm->handleSubmit") // test
        e.preventDefault();
        setIsFormSubmitted(true);
        // validate inputs
        if (!validateForm()) {
            setError('Please correct the errors:');
            return;
        }
        setIsLoading(true);
        // Wait for one second
        await new Promise((resolve) => setTimeout(resolve, 1000));
        // Create user data
        const userDetails = {
            username: email,
            password: password
        }

        console.log("inside LoginForm->handleSubmit: Create request for login") // test

        // Create request for login
        const loginRequest = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userDetails)
        };

        try {

            console.log("inside LoginForm->handleSubmit: tring to  get the token") // test

            let res = await fetch('http://localhost:5000/api/Tokens', loginRequest);
            if (res.status === 200) {
                // Get the Token
                const data = await res.text();
                console.log(res)
                console.log(data)
                const token = data;

                console.log("inside LoginForm->handleSubmit: got the token") // test

                // Create requset object to get the user
                const requestUser = {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
                };

                // Request the user from the server
                res = await fetch(`http://localhost:5000/api/Users/${email}`, requestUser);                
                const resUserDetails = await res.json();
                const user = resUserDetails;
                // Register this user a new socket
                await socket.emit("login", user.username);
                user.token = token;
                handleLogin(e, user);
            } else if (res.status === 400) {
                const messageRes = await res.json();
                setError(messageRes.message);
                setIsLoading(false);
            } else {
                throw new Error('Unexpected Server Error');
            }
        } catch (error) {
            console.log("error:")
            console.log(error);
            setError(error);
            setIsLoading(false);
        }

    };

    return (
        <form className="needs-validation login-form" onSubmit={handleSubmit} noValidate>
            <span className="login-form-title">Member Login</span>
            {/* display the errors message */}
            {error && <div className="alert alert-danger error-message mb-2">{error}</div>}
            {/* email input */}
            <div className={`wrap-input validate-input ${isFormSubmitted && errors.email ? 'was-validated' : ''}`}
                data-validate="Valid email is required: example@hello.world">
                <input required className={`form-control input-main ${errors.email ? 'input-error' : ''}`}
                    type="email" name="email" placeholder="Enter email" value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                {/* display email error message */}
                {errors.email && (
                    <div className="invalid-feedback" style={{ display: 'block' }}>
                        {errors.email}
                    </div>
                )}
                <span className="focus-input"></span>
                <span className="symbol-input">
                    <i className="fa fa-envelope" aria-hidden="true"></i>
                </span>
            </div>
            {/* password input */}
            <div className={`wrap-input validate-input mb-1" ${isFormSubmitted && errors.password ? 'was-validated' : ''}`}
                data-validate="Password is required">
                <input required className={`form-control input-main ${errors.password ? 'input-error' : ''}`}
                    type={passwordVisible ? 'text' : 'password'}
                    id="psw" name="password" value={password} onChange={(e) => handlePasswordChange(e)}
                    placeholder="Enter your password"
                />
                {/* display password error message */}
                {errors.password && (
                    <div className="invalid-feedback" style={{ display: 'block' }}>
                        {errors.password}
                    </div>
                )}
                <span className="focus-input"></span>
                <span className="symbol-input">
                    <i className="fa fa-lock" aria-hidden="true"></i>
                </span>
            </div>
            <span className="showPws" onClick={togglePasswordVisibility}>
                {passwordVisible ? 'Hide password' : 'Show password'}
                <i className={`far ${passwordVisible ? 'fa-eye-slash' : 'fa-eye'}`}></i>
            </span>

            {/* login button */}
            <div className="container-login-form-btn">
                <button className="btn login-form-btn" type="submit" disabled={isLoading}>Login</button>
            </div>
            {/* forgot password */}
            <div className="text-center p-t-12 mt-1">
                <a id="forgotBtn" href="#" onClick={handleForgotPassword}>forgot password?</a>
            </div>
            {/* register link */}
            <div className="text-center p-t-10">
                <Link to="/register" id="registerBtn">
                    <i className="fa fa-long-arrow-right mr-1" aria-hidden="true"></i>
                    register here
                    <i className="fa fa-long-arrow-left ml-1" aria-hidden="true"></i>
                </Link>
            </div>
        </form>
    );
}

export default LoginForm;
