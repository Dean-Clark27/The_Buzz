import React, { useState } from 'react';
import * as ImagePicker from 'expo-image-picker';
import { Button, Image, View } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import GoogleAuth from '../auth/AuthUtils';
import MessagesPage from './MessagesPage';
import UserProfile from './UserProfile';

type AppStackParamList = {
  GoogleAuth: { setUser: React.Dispatch<React.SetStateAction<User | null>> };
  Messages: { user: User };
  Profile: { user: User };
};

interface User {
  token: string;
  name: string;
  email: string;
  picture: string;
}

const Stack = createStackNavigator<AppStackParamList>();

export default function App() {
  const [user, setUser] = useState<User | null>(null);

  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="GoogleAuth">
        {!user ? (
          <Stack.Screen
            name="GoogleAuth"
            component={GoogleAuth}
            initialParams={{ setUser }}
            options={{ title: 'Sign In' }}
          />
        ) : (
          <>
            <Stack.Screen
              name="Messages"
              component={MessagesPage}
              initialParams={{ user }}
              options={{ title: 'Messages' }}
            />
            <Stack.Screen
              name="Profile"
              options={{ title: 'Profile' }}
              children={({ navigation, route }) => (
                <UserProfile navigation={navigation} route={route} />
              )}
            />
          </>
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
}