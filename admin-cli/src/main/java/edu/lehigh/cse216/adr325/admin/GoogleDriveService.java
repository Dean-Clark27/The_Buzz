package edu.lehigh.cse216.adr325.admin;

import com.google.auth.oauth2.GoogleCredentials;

import edu.lehigh.cse216.adr325.admin.MemCachierService;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.services.drive.model.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

public class GoogleDriveService {
    private static Drive driveService;
    private static final String APPLICATION_NAME = "Buzz App";
    private static final String CREDENTIALS_ENV_VAR = "GOOGLE_CREDENTIALS_JSON";
    private static final String FOLDER_ID = "1umi7-ZVs3GR-HppPQ96HL95axLNcFbwM";

    /**
     * Get the Drive service, lazy initialization
     * @return Drive service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        if (driveService == null) {
            String credentialsJson = System.getenv(CREDENTIALS_ENV_VAR);
            if (credentialsJson == null) {
                throw new IOException("Missing environment variable: " + CREDENTIALS_ENV_VAR);
            }
            GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsJson.getBytes()))
                    .createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
            driveService = new Drive.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), new HttpCredentialsAdapter(credentials))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
        return driveService;
    }

    /**
     * Upload a file to Google Drive
     * @param encoded_file_data the file data to upload in base64 encoding
     * @return the file id of the uploaded file
     */
    public static String uploadFileToDrive(String encoded_file_data) {
        try {
            // Initialize drive service and file metadata
            Drive service = getDriveService();
            File fileMetadata = new File();
            String uniqueFileName = "uploaded_file_" + System.currentTimeMillis();
            fileMetadata.setName(uniqueFileName);
            fileMetadata.setParents(Arrays.asList(FOLDER_ID));

            // Write file data to a temporary file
            byte[] file_data = Base64.getDecoder().decode(encoded_file_data);
            java.io.File tempFile = java.io.File.createTempFile("upload", ".tmp");
            java.nio.file.Files.write(tempFile.toPath(), file_data);
            FileContent mediaContent = new FileContent("application/octet-stream", tempFile);

            // Upload file to Google Drive
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            // Add the file to the cache for 1 hour (file_id, 3600, file_data)
            MemCachierService.set(file.getId(), 3600, encoded_file_data);

            // Return the file id
            return file.getId();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a file from Google Drive
     * @param fileId the id of the file to get
     * @return the file data
     */
    public static String deleteFileFromDrive(String fileId) {
        // Check if the file is in the cache
        String cached_file = (String) MemCachierService.get(fileId);
        if (cached_file != null) {
            return cached_file;
        }

        try {
            // Initialize drive service
            Drive service = getDriveService();

            // Download file from Google Drive
            ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
            service.files().delete(fileId);
            // Convert to byte array
            byte[] file_byte_array = output_stream.toByteArray();
            // Encode to base64 and return as string
            return Base64.getEncoder().encodeToString(file_byte_array);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}