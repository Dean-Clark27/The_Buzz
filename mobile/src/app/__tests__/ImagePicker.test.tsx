jest.mock('expo-image-picker', () => ({
    launchImageLibraryAsync: jest.fn(() => Promise.resolve({ cancelled: false, uri: 'image-uri' })),
  }));
  import * as ImagePicker from 'expo-image-picker';
  
  test('picks an image from gallery', async () => {
    const { getByText } = render(<MessagesPage />);
    fireEvent.press(getByText('Select Image'));
    expect(ImagePicker.launchImageLibraryAsync).toHaveBeenCalled();
  });  