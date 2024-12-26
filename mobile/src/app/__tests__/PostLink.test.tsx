import React from 'react';
import { render, fireEvent } from '@testing-library/react-native';
import PostList from '../components/PostList';
import PostDetail from '../components/PostDetail';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

const Stack = createStackNavigator();

describe('Post Linking Tests', () => {
  const posts = [{ id: '1', title: 'Post 1' }, { id: '2', title: 'Post 2' }];

  const App = () => (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="PostList" component={PostList} initialParams={{ posts }} />
        <Stack.Screen name="PostDetail" component={PostDetail} />
      </Stack.Navigator>
    </NavigationContainer>
  );

  it('should navigate to PostDetail when a post is clicked', () => {
    const { getByText } = render(<App />);
    const post = getByText('Post 1');

    fireEvent.press(post);

    expect(getByText('Post 1 Details')).toBeTruthy();
  });
});
