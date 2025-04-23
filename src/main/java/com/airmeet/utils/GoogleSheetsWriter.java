package com.airmeet.utils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleSheetsWriter {

    private static final String APPLICATION_NAME = "Airmeet Automation";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static Sheets sheetsService;

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        if (sheetsService == null) {
            FileInputStream serviceAccountStream = new FileInputStream("src/main/resources/credentials.json");

            GoogleCredential credential = GoogleCredential.fromStream(serviceAccountStream)
                    .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));

            sheetsService = new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    credential
            )
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }

        return sheetsService;
    }

    public static void appendToSheet(String spreadsheetId, String sheetName, List<List<Object>> data)
            throws IOException, GeneralSecurityException {

        ValueRange body = new ValueRange().setValues(data);

        Sheets.Spreadsheets.Values.Append request = getSheetsService().spreadsheets().values()
                .append(spreadsheetId, sheetName + "!A1", body)
                .setValueInputOption("RAW")
                .setInsertDataOption("INSERT_ROWS");

        request.execute();
    }
    public static void updateRecordingStats(String spreadsheetId, int rowIndex, int recordingsSelected, int filesZipped)
            throws IOException, GeneralSecurityException {

        String range = "Sheet1!Y" + (rowIndex + 2) + ":Z" + (rowIndex + 2);
        List<List<Object>> values = List.of(List.of(recordingsSelected, filesZipped));

        ValueRange body = new ValueRange().setValues(values);

        getSheetsService().spreadsheets().values()
                .update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
    }
    public static void updateFileUploadStatus(String spreadsheetId, int rowIndex, String fileUploadStatus)
            throws IOException, GeneralSecurityException {

        String range = "Sheet1!AA" + (rowIndex + 2);
        List<List<Object>> values = List.of(List.of(fileUploadStatus));

        ValueRange body = new ValueRange().setValues(values);

        getSheetsService().spreadsheets().values()
                .update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
    }
}
