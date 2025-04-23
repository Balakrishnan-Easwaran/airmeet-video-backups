package com.airmeet.utils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleDriveUploader {

    private static final String APPLICATION_NAME = "Airmeet Automation";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String FOLDER_ID = "1YRhTIzhipibiH809htYP26R56THczfpT";

    public static boolean uploadZipToDrive(String zipFilePath, String zipFileName)
            throws IOException, GeneralSecurityException {
        boolean fileUploadStatus = false;
        Drive driveService = getDriveService();
        System.out.println(" Starting File Upload -: " + zipFileName);
        File fileMetadata = new File();
        fileMetadata.setName(zipFileName);
        fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

        java.io.File file = new java.io.File(zipFilePath);
        FileContent mediaContent = new FileContent("application/zip", file);

        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, name")
                .execute();

        if (uploadedFile != null && uploadedFile.getId() != null) {
            System.out.println("✅ Uploaded to Drive: " + uploadedFile.getName() + " (ID: " + uploadedFile.getId() + ")");
            return true;
        } else {
            return false;
        }

    }

    private static Drive getDriveService() throws IOException, GeneralSecurityException {
        InputStream in = GoogleDriveUploader.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            in = java.nio.file.Files.newInputStream(Paths.get("src/main/resources/credentials.json"));
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName(APPLICATION_NAME).build();
    }
}
