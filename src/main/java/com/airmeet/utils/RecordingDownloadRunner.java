package com.airmeet.utils;

import com.airmeet.base.DriverManager;
import com.airmeet.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.time.Duration;

import java.util.List;

public class RecordingDownloadRunner {

    public static void main(String[] args) {
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

            for (int i = 0; i < 1; i++) {
                List<Object> row = rows.get(i);
                if (row.isEmpty()) continue;

                String airmeetId = row.get(0).toString();
                String zipFileName = row.get(15).toString();
                String url = "https://www.airmeet.com/airmeets/" + airmeetId + "/schedule/recordings";
                System.out.println("🔁 [" + (i + 2) + "] Visiting: " + url); // +2 to reflect row number in sheet



                driver.get(url);

                Thread.sleep(5000);
                // download the video & zip it and do all other operations.
                By checkbox = By.xpath("//th[@class='checkbox-outer']//span[@class='checkmark']");
                driver.findElement(checkbox).click();

                By downloadButton = By.xpath("//button[contains(text(), 'Recordings') and contains(text(), 'selected')]");
                Thread.sleep(2000);
                driver.findElement(downloadButton).click();
                Thread.sleep(5000);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));

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
                File zipped = ZipUtil.zipAllFilesInFolder(downloadDir, zipFilePath);
                System.out.println("Zip complete!");
            }
            //DriverManager.quitDriver();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
