**Airmeet Session Recording Backup Automation**

This project automates the process of downloading, zipping, and backing up session recordings from [Airmeet](https://www.airmeet.com). It uses **Selenium WebDriver**, **Google Drive API**, and **Google Sheets API**, all integrated with Java.

---

**What It Does**

For each Airmeet event (based on a list of IDs in a Google Sheet):

1. Navigates to the event’s recordings page  
2. Selects all available recordings  
3. Downloads the recordings into a local folder  
4. Waits until all `.mp4` files finish downloading  
5. Zips the downloaded recordings into a `bucket` folder (named from a Google Sheet column)  
6. Uploads the zipped file to a specified Google Drive folder  
7. Logs the following back into the same Google Sheet:
   - Number of recordings selected
   - Number of videos zipped
   - Upload success/failure status
8. ✅ Cleans up all local files before processing the next event

---

**Tech Stack**

- **Java 24**
- **Selenium WebDriver 4.19.1**
- **Google Sheets API**
- **Google Drive API**
- **WebDriverManager**
- **Maven**

---

**Folder Structure**

```
.
├── src
│   └── main
│       ├── java
│       │   └── com.airmeet
│       │       ├── base          # DriverManager class
│       │       ├── runner        # Main runner class
│       │       ├── utils         # Zip, Sheet, Drive helpers
│       └── resources
│           └── credentials.json  # Google Service Account credentials (excluded from Git)
```

---

**Notes**

- All `System.out.println()` logs are redirected to timestamped `automation-log-*.txt` files
- Service account must be shared into:
  - 📁 Google Drive folder (to upload files)
  - 📄 Google Sheet (to update row data)
- If the recording checkbox is unavailable or the download button is disabled, the script logs and skips the row
- Project avoids crashes by checking element availability before actions

---

**How to Run**

1. Configure your `credentials.json` and place it in `/src/main/resources`
2. Add Airmeet IDs to column A of your Google Sheet
3. Run the main runner:
```bash
mvn clean install
```

---

**Author**

Balakrishnan Easwaran
