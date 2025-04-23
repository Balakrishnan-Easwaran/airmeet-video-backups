package com.airmeet.utils;

import com.airmeet.base.DriverManager;
import com.airmeet.pages.LoginPage;
import com.google.api.services.sheets.v4.Sheets;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.io.File;
import java.time.Duration;

import java.util.List;

public class RecordingDownloadRunner {

    public static void main(String[] args) {

        try {
            String timestamp = LocalDateTime.now().toString().replace(":", "-");
            PrintStream logStream = new PrintStream(new FileOutputStream("automation-log-" + timestamp + ".txt", true));
            System.setOut(logStream);  // Redirect stdout to file
            System.setErr(logStream);  // Redirect stderr to file
            System.out.println("===== Log started at " + LocalDateTime.now() + " =====");
        } catch (Exception e) {
            e.printStackTrace(); // fallback in case log redirection fails
        }

        try {
            WebDriver driver = DriverManager.getDriver();

            String sheetId = "1hwf9tT6Z47-kCshdjWuWBgMLMIj-GuvH8CxiXhACKEo";
            String sheetName = "Sheet1"; // or your actual tab name

            List<List<Object>> rows = GoogleSheetsReader.getSheetRows(sheetId, sheetName);
            driver.get("https://www.airmeet.com/signin/auth");
            LoginPage loginPage = new LoginPage(driver);
            loginPage.login("jeff@clicked.com", "HostLogin2023-01#");
            try {
                System.out.println("going to sleep");
                Thread.sleep(10000); // Replace later with proper wait
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 22; i < rows.size(); i++) {
                List<Object> row = rows.get(i);
                if (row.isEmpty()) continue;

                String airmeetId = row.get(0).toString();
                String zipFileName = row.get(15).toString();
                String url = "https://www.airmeet.com/airmeets/" + airmeetId + "/schedule/recordings";
                System.out.println("🔁 [" + (i + 2) + "] Visiting: " + url); // +2 to reflect row number in sheet



                driver.get(url);
                Thread.sleep(5000);

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//th[@class='checkbox-outer']//span[@class='checkmark']")
                ));

                // download the video & zip it and do all other operations.
                By checkbox = By.xpath("//th[@class='checkbox-outer']//span[@class='checkmark']");
                driver.findElement(checkbox).click();

                List<WebElement> downloadButtons = driver.findElements(By.xpath("//button[contains(text(), 'Recordings') and contains(text(), 'selected')]"));

                if (downloadButtons.isEmpty()) {
                    System.out.println("⚠️ No download button appeared — skipping row " + (i + 2));
                    GoogleSheetsWriter.updateFileUploadStatus(sheetId, i, "⚠️ No Recordings Found");
                    continue;
                }

                WebElement emptyDownloadButton = downloadButtons.get(0);

                if (!emptyDownloadButton.isEnabled()) {
                    System.out.println("⚠️ Download button is disabled — skipping row " + (i + 2));
                    GoogleSheetsWriter.updateFileUploadStatus(sheetId, i, "⚠️ Download Button Disabled");
                    continue;
                }



                By downloadButton = By.xpath("//button[contains(text(), 'Recordings') and contains(text(), 'selected')]");
                String buttonText = driver.findElement(downloadButton).getText();
                String[] parts = buttonText.split(" ");
                int recordingsSelected = Integer.parseInt(parts[1]);

                Thread.sleep(2000);
                driver.findElement(downloadButton).click();
                Thread.sleep(5000);


                // Wait until popup with close button appears
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[normalize-space()='Close']")
                ));
                System.out.println("Waiting for download to finish!");
                String downloadDir = "/Users/agilabalakrishnan/Desktop/airmeet-video-backups";
                System.out.println("Waiting for download to finish!");
                DownloadWatcher.waitForDownloadsToFinish("/Users/agilabalakrishnan/Desktop/airmeet-video-backups", 120);
                Thread.sleep(3000);
                System.out.println("Starting to zip!");
                String zipFilePath = "/Users/agilabalakrishnan/Desktop/airmeet-video-backup-zips/" + zipFileName + ".zip";
                int zippedFileCount = ZipUtil.zipAllFilesInFolder(downloadDir, zipFilePath);
                System.out.println("Zip complete!");

                GoogleSheetsWriter.updateRecordingStats(sheetId, i, recordingsSelected, zippedFileCount);

                // Uploading files to google drive! Yes!
                boolean fileUploadStatus = GoogleDriveUploader.uploadZipToDrive(zipFilePath, zipFileName + ".zip");
                if (fileUploadStatus) {
                    System.out.println("Uploaded successfully!");
                    GoogleSheetsWriter.updateFileUploadStatus(sheetId, i, "Successful");
                }
                else {
                    System.out.println("Erros in uploading files. aborting now!");
                    DriverManager.quitDriver();
                }

                // Clearing folders so that the next set of videos can be downloaded!
                boolean folderClearanceStatus = ZipUtil.clearFolders(downloadDir,"/Users/agilabalakrishnan/Desktop/airmeet-video-backup-zips/");
                if (folderClearanceStatus) {
                    System.out.println("Successfully cleared folders, continuining to next!");
                }
                else {
                    System.out.println("Erros in clearing folders! aborting now!");
                    DriverManager.quitDriver();
                }
            }
            //DriverManager.quitDriver();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}