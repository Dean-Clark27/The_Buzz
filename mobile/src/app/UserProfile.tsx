import React from 'react';
import { View, Text, Image, StyleSheet } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RouteProp } from '@react-navigation/native';

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

type ProfileScreenNavigationProp = StackNavigationProp<AppStackParamList, 'Profile'>;
type ProfileScreenRouteProp = RouteProp<AppStackParamList, 'Profile'>;

interface UserProfileProps {
  navigation: ProfileScreenNavigationProp;
  route: ProfileScreenRouteProp;
}

const UserProfile: React.FC<UserProfileProps> = ({ route }) => {
  const { user } = route.params;

  return (
    <View style={styles.container}>
      <Image source={{ uri: user.picture }} style={styles.profileImage} />
      <Text style={styles.name}>{`Name: ${user.name}`}</Text>
      <Text style={styles.email}>{`Email: ${user.email}`}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
    backgroundColor: '#fff',
  },
  profileImage: {
    width: 100,
    height: 100,
    borderRadius: 50,
    marginBottom: 20,
  },
  name: {
    fontSize: 24,
    fontWeight: 'bold',
  },
  email: {
    fontSize: 18,
    color: 'gray',
    marginTop: 10,
  },
});

export default UserProfile;