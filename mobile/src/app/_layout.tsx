import { createStackNavigator } from "@react-navigation/stack";
import MessagesPage from "./MessagesPage";

const Stack = createStackNavigator();

/**
 * - RootLayout is the main layout for the app
 * 
 * @returns {JSX.Element} - The root layout
 */
export default function RootLayout() {
  return (
    // Stack Navigator for the app (only one screen for now but can be expanded or changed to a different navigator)
    // Check out the "React Navigation" documentation for more information on navigators
    <Stack.Navigator>
      <Stack.Screen 
        name="MessagesPage" 
        component={MessagesPage} 
        options={{ headerShown: false }} 
      />
    </Stack.Navigator>
  );
}
