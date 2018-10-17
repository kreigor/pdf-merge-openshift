package io.kreiger.pdfmerge.component;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class Directory {
    private final Logger logger = LogManager.getLogger(getClass());

    private String path;
    private String directoryName;

    /**
     * Constructs a <code>Directory</code> object
     */
    public  Directory() {}

    /**
     * Constructs a <code>Directory</code> object
     *
     * @param path The path to the directory
     * @param dirName The name of the directory
     */
    public Directory(String path, String dirName) {
        this.path = path;
        this.directoryName = dirName;
    }

    /**
     * Check if the directory exists
     *
     * @return True if the directory exists, otherwise false
     */
    public boolean exists() {
        File directory = new File(path + directoryName);

        if(directory.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create the directory
     *
     * @return True if the directory was created, otherwise false
     */
    public boolean createDirectory() {
        if(!exists()) {
            File directory = new File(path + directoryName);

            boolean dirCreated;

            dirCreated = directory.mkdir();

            if(dirCreated) {
                logger.info("Created directory: " + directoryName + " in " + path);

                return true;
            } else {
                logger.error("Error creating directory: " + directoryName + " in " + path);

                return false;
            }
        } else {
            logger.error("Directory exists");
            return false;
        }
    }

    /**
     * Delete the directory and all of its contents
     *
     * @return True if the directory was deleted, otherwise false
     */
    public boolean delete() {
        boolean returnVal;
        returnVal = false;

        try {
            File directory = new File(path + directoryName);

            FileUtils.deleteDirectory(directory);

            returnVal = true;
        } catch (IOException ex) {
            logger.trace(ex);

            returnVal = false;
        } finally {
            return returnVal;
        }
    }

    /**
     * Return the path name
     *
     * @return The path name
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the path name
     *
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Returns the name of the directory
     *
     * @return The directory name
     */
    public String getDirectoryName() {
        return directoryName;
    }

    /**
     * Sets the name of the directory
     *
     * @param directoryName The name of the directory
     */
    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    /**
     * Returns the path with the directory name
     *
     * @return the full directory path
     */
    public String getFullPath() {
        String fullPath;

        fullPath = path + directoryName + "/";

        return fullPath;
    }
}