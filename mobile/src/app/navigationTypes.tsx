import { RouteProp } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';

// Define the `User` interface
export interface User {
  session_token : String,
  user_id : String,
  email : String,
  name : String,
  picture_url : String,
}

// Define the parameter list for the root stack navigator
export type RootStackParamList = {
  GoogleAuth: { setUser: React.Dispatch<React.SetStateAction<User | null>> };
  Messages: { user: User };
  Profile: { user: User };
  // Add other screens as needed
};

// Define navigation prop types for each screen
export type GoogleAuthScreenNavigationProp = StackNavigationProp<
  RootStackParamList,
  'GoogleAuth'
>;

export type MessagesScreenNavigationProp = StackNavigationProp<
  RootStackParamList,
  'Messages'
>;

export type ProfileScreenNavigationProp = StackNavigationProp<
  RootStackParamList,
  'Profile'
>;

// Define route prop types for each screen
export type GoogleAuthScreenRouteProp = RouteProp<RootStackParamList, 'GoogleAuth'>;

export type MessagesScreenRouteProp = RouteProp<RootStackParamList, 'Messages'>;

export type ProfileScreenRouteProp = RouteProp<RootStackParamList, 'Profile'>;
