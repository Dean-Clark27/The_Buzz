import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import './index.css'

/**
 * Entry point for the React application.
 * Uses StrictMode to highlight potential problems in the application.
 *
 * @function
 */
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>
)
