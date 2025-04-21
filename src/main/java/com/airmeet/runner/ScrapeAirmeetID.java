package com.airmeet.runner;

import com.airmeet.utils.GoogleSheetsWriter;
import com.airmeet.utils.JsonToSheetHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.json.JSONArray;

import java.util.List;

public class ScrapeAirmeetID {

    private WebDriver driver;

    public ScrapeAirmeetID(WebDriver driver) {
        this.driver = driver;
    }

    public void scrapeAirmeets () {
        int i = 1;
        while (i < 29) {
            try {
                driver.get("https://api.airmeet.com/api/v1/airmeet/community/v2/getAirmeets?pageSize=20&showDuplicates=true&communityId=40a3c3cd-efa2-437d-aa5b-6ea720b8d074&page=" + i + "&archived=false");

                String jsonResponse = driver.findElement(By.tagName("pre")).getText();
                JSONArray dataArray = new JSONArray(jsonResponse);

                List<List<Object>> rows = JsonToSheetHelper.convertJsonArrayToSheetData(dataArray, i);

                if (!rows.isEmpty()) {
                    if (i == 1) {
                        // include headers
                        GoogleSheetsWriter.appendToSheet("1hwf9tT6Z47-kCshdjWuWBgMLMIj-GuvH8CxiXhACKEo", "Sheet1", rows);
                    } else {
                        // skip headers
                        GoogleSheetsWriter.appendToSheet("1hwf9tT6Z47-kCshdjWuWBgMLMIj-GuvH8CxiXhACKEo", "Sheet1", rows.subList(1, rows.size()));
                    }
                }

                System.out.println("✅ Page " + i + " done");
                i++;

            } catch (Exception e) {
                System.out.println("❌ Page " + i + " failed: " + e.getMessage());
                i++;
            }
        }
    }
}
