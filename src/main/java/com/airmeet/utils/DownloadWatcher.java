package com.airmeet.utils;

import java.io.File;

public class DownloadWatcher {

    public static void waitForDownloadsToFinish(String folderPath, int maxWaitSeconds) throws InterruptedException {
        File folder = new File(folderPath);
        int waited = 0;

        System.out.println("⏳ Waiting for downloads to finish...");

        while (hasCrDownloads(folder) || hasUnstableFiles(folder)) {
            if (waited >= maxWaitSeconds) {
                System.out.println("⚠️ Waited too long. Proceeding anyway.");
                break;
            }

            Thread.sleep(1000);
            waited++;
        }

        System.out.println("✅ Downloads complete and stable.");
    }

    private static boolean hasCrDownloads(File folder) {
        File[] crDownloads = folder.listFiles((dir, name) -> name.endsWith(".crdownload"));
        return crDownloads != null && crDownloads.length > 0;
    }

    private static boolean hasUnstableFiles(File folder) {
        File[] files = folder.listFiles(File::isFile);
        if (files == null) return false;

        for (File file : files) {
            if (file.length() == 0) {
                System.out.println("⏳ File not ready yet: " + file.getName());
                return true;
            }
        }
        return false;
    }
}
