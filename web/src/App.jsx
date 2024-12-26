import { useState } from 'react';
import { GoogleOAuthProvider, GoogleLogin } from '@react-oauth/google';
import './App.css';
import MessageContainer from './components/MessageContainer';
import Navbar from './components/Navbar';
import AddMessageForm from './components/AddMessageForm';
import Profile from './components/Profile';

/**
 * Main application component that handles authentication, state, 
 * and renders the navigation bar, message form, and message container.
 *
 * @component
 */
function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userData, setUserData] = useState(null);
  const [isAddVisible, setIsAddVisible] = useState(false);

  /**
   * Handles successful login, verifies token, and restricts domain.
   * Sets user data if authentication is successful.
   */
  const handleLoginSuccess = async (credentialResponse) => {
    const res = await fetch('/api/auth', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ token: credentialResponse.credential }),
    });
    const result = await res.json();
    if (result.success && result.domain === 'lehigh.edu') {
      setIsAuthenticated(true);
      setUserData(result.user);
    }
  };

  /**
   * Handles logout by resetting authentication and user data.
   */
  const handleLogout = () => {
    setIsAuthenticated(false);
    setUserData(null);
  };

  /**
   * Toggles the visibility of the AddMessageForm component.
   */
  const toggleAddVisibility = () => {
    setIsAddVisible(!isAddVisible);
  };

  return (
    <GoogleOAuthProvider clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}>
      {isAuthenticated ? (
        <>
          {/* Navbar component with a toggle function for form visibility */}
          <Navbar userData={userData} onLogout={handleLogout} toggleForm={toggleAddVisibility} />
          {/* AddMessageForm component, conditionally visible */}
          {isAddVisible && <AddMessageForm refresh={() => window.location.reload()} />}
          {/* MessageContainer to display messages */}
          <MessageContainer />
          {/* Profile component to display user's profile */}
          {userData && <Profile userData={userData} />}
        </>
      ) : (
        <div className="login-page">
          <GoogleLogin
            onSuccess={handleLoginSuccess}
            onError={() => console.log('Login Failed')}
          />
        </div>
      )}
    </GoogleOAuthProvider>
  );
}

export default App;