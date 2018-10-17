package io.kreiger.pdfmerge.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class MultipartFileUpload {
    private final Logger logger = LogManager.getLogger(getClass());

    /**
     *
     * @param path
     * @param file
     * @return
     */
    public boolean uploadFile(String path, MultipartFile file) {

        boolean returnValue;

        if (file.getSize() > 0) {
            String fileName;
            Long fileSize;
            int readBytes;
            InputStream inputStream;
            OutputStream outputStream;

            fileName = path + file.getOriginalFilename();
            fileSize = file.getSize();

            returnValue = true;

            try {
                inputStream = file.getInputStream();
                outputStream = new FileOutputStream(fileName);

                byte[] buffer = new byte[10000];
                while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
                    outputStream.write(buffer, 0, readBytes);
                }

                outputStream.close();
                inputStream.close();
            } catch (IOException ex) {
                logger.trace(ex.getMessage());

                returnValue = false;
            } finally {
                logger.info("File: " + fileName + " (" + fileSize + ") Uploaded.");

                return returnValue;
            }
        } else {
            returnValue = false;
        }

        return returnValue;
    }
}
