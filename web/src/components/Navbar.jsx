import PropTypes from 'prop-types';

export default function Navbar({ userData, onLogout, toggleForm }) {

  Navbar.propTypes = {
    userData: PropTypes.shape({
      id: PropTypes.string,
      firstName: PropTypes.string,
    }).isRequired,
    onLogout: PropTypes.func.isRequired,
    toggleForm: PropTypes.func.isRequired,
  };

  return (
    <nav className="navbar">
      <h1>Welcome, {userData ? userData.firstName : "User"}</h1>
      <button onClick={toggleForm} className="add-button">+</button>
      <button onClick={onLogout}>Logout</button>
    </nav>
  );
}
