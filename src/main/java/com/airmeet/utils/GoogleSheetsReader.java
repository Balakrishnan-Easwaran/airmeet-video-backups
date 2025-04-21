package com.airmeet.utils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.FileInputStream;
import java.util.List;

public class GoogleSheetsReader {

    private static final String APPLICATION_NAME = "Airmeet Automation";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static Sheets sheetsService;

    public static Sheets getSheetsService() throws Exception {
        if (sheetsService == null) {
            FileInputStream serviceAccountStream = new FileInputStream("src/main/resources/credentials.json");

            GoogleCredential credential = GoogleCredential.fromStream(serviceAccountStream)
                    .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));

            sheetsService = new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    credential
            ).setApplicationName(APPLICATION_NAME).build();
        }

        return sheetsService;
    }

    public static List<List<Object>> getSheetRows(String spreadsheetId, String sheetName) throws Exception {
        ValueRange response = getSheetsService()
                .spreadsheets()
                .values()
                .get(spreadsheetId, sheetName + "!A2:Z") // assuming headers are in row 1
                .execute();

        return response.getValues();
    }
}
