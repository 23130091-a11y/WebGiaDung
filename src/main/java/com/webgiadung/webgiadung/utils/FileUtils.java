package com.webgiadung.doanweb.utils;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FileUtils {

    public static String saveFile(Part part, String realPath, String subFolder) throws IOException {

        if (part == null || part.getSubmittedFileName().isEmpty()) return null;

        String fileName = System.currentTimeMillis() + "_" +
                Paths.get(part.getSubmittedFileName()).getFileName();

        String uploadDir = realPath + "assets/img/" + subFolder;

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        part.write(uploadDir + File.separator + fileName);
        return fileName;
    }
}
