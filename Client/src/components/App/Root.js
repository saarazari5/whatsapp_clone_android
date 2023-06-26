import React, { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import NotFound from '../NotFound/NotFound.js';
import Register from '../Register/Register.js';
import './Root.css';
import Login from '../Login/Login.js';
import ChatContainer from '../ChatPage/ChatContainer.js';
import { UserProvider } from '../../UserContext.js';


function Root() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const authLogin = () => {
    // If the authentication is successful, set isAuthenticated to true
    setIsAuthenticated(true);
  };

  const handleLogout = () => {
    // Perform logout logic, e.g., clear session, remove tokens, etc.
    // Set isAuthenticated to false
    setIsAuthenticated(false);
  };

  return (
    <React.StrictMode>
      <UserProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/"
              element={<Login authenticate={authLogin} />}
            />
            <Route path="/chat"
              element={
                isAuthenticated ? (
                  <ChatContainer handleLogout={handleLogout} />
                ) : (
                  <Navigate to="/" />
                )
              }
            />
            <Route path="/register"
              element={<Register />}
            />
            <Route path="/*" element={<NotFound />} />
          </Routes>
        </BrowserRouter>
      </UserProvider>

    </React.StrictMode>
  );
}

export default Root;