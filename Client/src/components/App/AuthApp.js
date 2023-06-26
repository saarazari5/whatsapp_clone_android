import React from 'react';
import { Routes, Route } from 'react-router-dom';
import ChatContainer from '../ChatPage/ChatContainer';

function AuthApp() {
  return (
    <Routes>
      <Route path="/chat" element={<ChatContainer/>} />
    </Routes>
  );
}

export default AuthApp;
