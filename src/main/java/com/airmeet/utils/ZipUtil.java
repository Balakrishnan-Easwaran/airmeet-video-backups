package com.airmeet.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public static int zipAllFilesInFolder(String folderPath, String zipFilePath) throws IOException {
        File folder = new File(folderPath);
        File zipFile = new File(zipFilePath);
        int zippedCount = 0;

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            File[] files = folder.listFiles();
            if (files == null || files.length == 0) {
                System.out.println("No files to zip in folder: " + folderPath);
                return 0;
            }

            for (File file : files) {
                String fileName = file.getName().toLowerCase();

                if (!file.isFile() || file.getName().endsWith(".crdownload") || !fileName.endsWith(".mp4")) {
                    continue;
                }

                try (FileInputStream fis = new FileInputStream(file)) {
                    zos.putNextEntry(new ZipEntry(file.getName()));

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                }
                System.out.println("Zipping: " + file.getName());
                zippedCount++;
            }
        }

        return zippedCount;
    }
    public static boolean clearFolders(String... folderPaths) {
        boolean clearFolderStatus = true;
        for (String path : folderPaths) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.delete()) {
                        System.out.println("🧹 Deleted: " + file.getName() + " from " + folder.getName());
                    } else {
                        System.out.println("Failed to delete: " + file.getName());
                        clearFolderStatus = false;
                    }
                }
            }
        }
        return clearFolderStatus;
    }
}
