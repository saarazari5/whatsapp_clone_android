import React from 'react';
import Root from './components/App/Root.js';
import ReactDOM from 'react-dom';
import './components/ChatPage/chatPage.css';
import './components/Login/Login.css';
import "./components/Register/registerPage.css"
import './components/Navbar/Navbar.css'

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <Root />
    </React.StrictMode>
);
