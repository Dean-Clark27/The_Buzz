import { render } from '@testing-library/react-native';
import UserProfile from './UserProfile';

test('renders UserProfile with user details', () => {
  const user = { name: 'John Doe', email: 'john@example.com', picture: 'https://example.com/john.jpg' };
  const { getByText, getByAltText } = render(<UserProfile route={{ params: { user } }} />);
  expect(getByText('Name: John Doe')).toBeTruthy();
  expect(getByText('Email: john@example.com')).toBeTruthy();
});
