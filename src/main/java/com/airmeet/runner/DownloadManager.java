package com.airmeet.runner;

import com.airmeet.base.DriverManager;
import com.airmeet.pages.LoginPage;
import com.airmeet.utils.RecordingDownloadRunner;
import org.openqa.selenium.WebDriver;

public class DownloadManager {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = DriverManager.getDriver();
        System.out.println("Step 1: Opening login page");
        driver.get("https://www.airmeet.com/signin/auth"); // Update if needed

        LoginPage loginPage = new LoginPage(driver);
        System.out.println("calling login function");
        loginPage.login("jeff@clicked.com", "HostLogin2023-01#");
        // Wait for login to complete and then go to video library page
        try {
            System.out.println("going to sleep");
            Thread.sleep(10000); // Replace later with proper wait
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        //ScrapeAirmeetID scrapeAirmeetID = new ScrapeAirmeetID(driver);
        //scrapeAirmeetID.scrapeAirmeets();

        driver.get("https://www.airmeet.com/community-manager/40a3c3cd-efa2-437d-aa5b-6ea720b8d074/library/session-recordings");

        // Eventually: continue automation from here
    }
}
