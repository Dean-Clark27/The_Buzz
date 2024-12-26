import React from 'react';
import { render, fireEvent } from '@testing-library/react-native';
import FileUploadComponent from '../components/FileUploadComponent';

describe('File Upload Tests', () => {
  it('should allow uploading an image file', () => {
    const { getByTestId } = render(<FileUploadComponent />);
    const uploadButton = getByTestId('upload-button');
    const fileInput = getByTestId('file-input');

    fireEvent.change(fileInput, {
      target: { files: [{ name: 'image.png', type: 'image/png' }] },
    });

    expect(uploadButton.props.disabled).toBe(false);
  });

  it('should not allow uploading unsupported file types', () => {
    const { getByTestId } = render(<FileUploadComponent />);
    const uploadButton = getByTestId('upload-button');
    const fileInput = getByTestId('file-input');

    fireEvent.change(fileInput, {
      target: { files: [{ name: 'document.exe', type: 'application/x-msdownload' }] },
    });

    expect(uploadButton.props.disabled).toBe(true);
  });

  it('should display an error for invalid file types', () => {
    const { getByText, getByTestId } = render(<FileUploadComponent />);
    const fileInput = getByTestId('file-input');

    fireEvent.change(fileInput, {
      target: { files: [{ name: 'invalidfile.xyz', type: 'application/xyz' }] },
    });

    expect(getByText('Invalid file type.')).toBeTruthy();
  });
});
