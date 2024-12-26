import AsyncStorage from '@react-native-async-storage/async-storage';

test('caches data to AsyncStorage', async () => {
  const data = [{ id: 1, title: 'Cached' }];
  await AsyncStorage.setItem('messages', JSON.stringify(data));
  const storedData = await AsyncStorage.getItem('messages');
  expect(storedData).toEqual(JSON.stringify(data));
});
