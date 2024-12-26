import AsyncStorage from '@react-native-async-storage/async-storage';

const saveToCache = async (key: string, data: any) => {
  try {
    await AsyncStorage.setItem(key, JSON.stringify(data));
  } catch (e) {
    console.error('Error saving data to cache:', e);
  }
};

const getFromCache = async (key: string) => {
  try {
    const value = await AsyncStorage.getItem(key);
    return value ? JSON.parse(value) : null;
  } catch (e) {
    console.error('Error retrieving data from cache:', e);
  }
};