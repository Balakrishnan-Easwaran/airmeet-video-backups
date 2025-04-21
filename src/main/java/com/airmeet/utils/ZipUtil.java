package com.airmeet.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public static File zipAllFilesInFolder(String folderPath, String zipFilePath) throws IOException {
        File folder = new File(folderPath);
        File zipFile = new File(zipFilePath);

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            File[] files = folder.listFiles();
            if (files == null || files.length == 0) {
                System.out.println("⚠️ No files to zip in folder: " + folderPath);
                return null;
            }

            for (File file : files) {
                if (!file.isFile() || file.getName().endsWith(".crdownload")) {
                    continue;
                }

                try (FileInputStream fis = new FileInputStream(file)) {
                    zos.putNextEntry(new ZipEntry(file.getName()));

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        System.out.println("Zipping: " + file.getName());
                        zos.write(buffer, 0, length);
                    }

                    zos.closeEntry();
                }
            }
        }

        return zipFile;
    }
}
