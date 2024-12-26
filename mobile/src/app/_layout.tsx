import React from 'react';
import { User } from './navigationTypes';

// Define prop types for MessagesPage
interface MessagesPageProps {
  user: User;
}

export default function MessagesPage({ user }: MessagesPageProps) {
  return (
    <div>
      <h1>Welcome, {user.name}!</h1>
      {/* Add any other components or UI elements */}
    </div>
  );
}
