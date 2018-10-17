package io.kreiger.pdfmerge.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
public class PdfMerge {
    private final Logger logger = LogManager.getLogger(getClass());

    private String path;
    private String directory;
    private File outFile;

    /**
     *
     */
    public PdfMerge() {}

    /**
     *
     * @param path
     * @param dir
     */
    public PdfMerge(String path, String dir) {
        this.path = path;
        this.directory = dir;
    }

    /**
     *
     */
    public void fileCleanup() {
        Directory dir = new Directory();
        dir.setPath(this.path);
        dir.setDirectoryName(this.directory);
        dir.delete();
    }

    /**
     *
     * @return
     */
    public List<InputStream> sortFiles() {
        String files;
        String filesLog = "";
        List<InputStream> list = new ArrayList<>();
        File dir = new File(this.getFullPath());

        File[] fileList = dir.listFiles();

        logger.debug("Sorting Files");

        Arrays.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File a, File b) {
                return a.getName().toLowerCase().compareTo(b.getName().toLowerCase());
            }
        });

        logger.debug("fileList Length: " + fileList.length);

        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                files = fileList[i].getName();
                if (files.endsWith(".pdf") || files.endsWith(".PDF")) {
                    try {
                        logger.debug("File: " + getFullPath() + files);
                        list.add(new FileInputStream(new File(this.getFullPath() + files)));
                        filesLog += fileList[i].getName() + ", ";
                        fileList[i].delete();
                    } catch (FileNotFoundException ex) {
                        logger.trace(ex);
                    }
                }
            }
        }

        logger.info("Files to be merged: " + filesLog.substring(0, filesLog.length() - 2));

        return list;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the directory
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * @param directory the directory to set
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * @return the outFile
     */
    public File getOutFile() {
        return outFile;
    }

    /**
     * @param outFile the outFile to set
     */
    public void setOutFile(File outFile) {
        this.outFile = outFile;
    }

    /**
     *
     * @return
     */
    public String getFullPath() {
        String fullPath;

        fullPath = this.path + this.directory + "/";

        return fullPath;
    }
}
