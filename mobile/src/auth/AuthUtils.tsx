import React, { useEffect } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Image } from 'react-native';
import * as WebBrowser from 'expo-web-browser';
import * as Google from 'expo-auth-session/providers/google';
import clientSecret from '../../client-secret.json';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from './navigationTypes';
import { RouteProp } from '@react-navigation/native';
import { User } from './navigationTypes';

WebBrowser.maybeCompleteAuthSession();

// Use navigation and route props
type GoogleAuthScreenNavigationProp = StackNavigationProp<AppStackParamList, 'GoogleAuth'>;
type GoogleAuthScreenRouteProp = RouteProp<RootStackParamList, 'GoogleAuth'>;

type AppStackParamList = {
  GoogleAuth: { setUser: React.Dispatch<React.SetStateAction<User | null>> };
  Messages: { user: User };
  Profile: { user: User };
};

interface GoogleAuthProps {
  navigation: GoogleAuthScreenNavigationProp;
  route: GoogleAuthScreenRouteProp;
}

export default function GoogleAuth({ navigation, route }: GoogleAuthProps) {
  const { setUser } = route.params;

  const [request, response, promptAsync] = Google.useAuthRequest({
    clientId: clientSecret.web.client_id,
    iosClientId: '<YOUR_IOS_CLIENT_ID>',
    androidClientId: '<YOUR_ANDROID_CLIENT_ID>',
    webClientId: clientSecret.web.client_id,
  });

  useEffect(() => {
    if (response?.type === 'success' && response.authentication) {
      const { accessToken } = response.authentication;
  
      // Fetch user profile info from Google
      fetch('https://www.googleapis.com/oauth2/v2/userinfo', {
        headers: { Authorization: `Bearer ${accessToken}` },
      })
        .then(res => res.json())
        .then(data => {
          setUser({
            token: accessToken,
            name: data.name,
            email: data.email, // No more error
            picture: data.picture, // No more error
          });
          navigation.navigate('Messages', { user: data }); // Navigate after successful login
        })
        .catch(error => console.error('Error fetching user info:', error));
    }
  }, [response]);  

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Sign In</Text>
      <TouchableOpacity style={styles.googleButton} onPress={() => promptAsync()}>
        <Image
          source={{ uri: "https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg" }}
          style={styles.googleIcon}
        />
        <Text style={styles.buttonText}>Sign in with Google</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#fff',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 40,
    textDecorationLine: 'underline',
  },
  googleButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#e0e0e0',
    paddingVertical: 12,
    paddingHorizontal: 20,
    borderRadius: 30,
    elevation: 3,
  },
  googleIcon: {
    width: 24,
    height: 24,
    marginRight: 10,
  },
  buttonText: {
    color: '#555',
    fontSize: 18,
    fontWeight: 'bold',
  },
});