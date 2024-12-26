import React from 'react';
import { render, fireEvent } from '@testing-library/react-native';
import GoogleAuth from '../AuthUtils';

describe('GoogleAuth Component', () => {
  it('renders login button', () => {
    const { getByText } = render(<GoogleAuth setUser={() => {}} />);
    expect(getByText('Login with Google')).toBeTruthy();
  });

  it('displays success message on successful login', async () => {
    const { getByText } = render(<GoogleAuth setUser={() => {}} />);
    fireEvent.press(getByText('Login with Google'));
    expect(getByText('Login successful!')).toBeTruthy();
  });

  it('displays error message on login failure', async () => {
    const { getByText } = render(<GoogleAuth setUser={() => {}} />);
    fireEvent.press(getByText('Login with Google'));
    expect(getByText('Login failed. Please try again.')).toBeTruthy();
  });
});
