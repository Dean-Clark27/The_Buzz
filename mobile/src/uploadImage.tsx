import axios from 'axios';

/**
 * Uploads an image to the server
 * @param {string} imageUri - The URI of the image to upload
 * @returns {Promise} - The server response or error
 */
const uploadImage = async (imageUri: string) => {
  const formData = new FormData();

  // To handle the image as a file, create a Blob or File from the URI
  const file = {
    uri: imageUri,
    type: 'image/jpeg', // Adjust MIME type if necessary
    name: 'uploaded_image.jpg',
  };

  // Append the file to the FormData
  formData.append('image', file as any);  // Type assertion to bypass TypeScript error

  try {
    const response = await axios.post('https://example.com/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data;
  } catch (error) {
    console.error('Error uploading image:', error);
    throw error;
  }
};

export default uploadImage;