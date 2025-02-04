package com.luv2code.jobportal.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {



    /**
     * Saves an uploaded file to the specified directory on the filesystem.
     * This method handles the complete file upload process including:
     * - Creating the target directory if it doesn't exist
     * - Copying the uploaded file data to the destination
     * - Handling any IO errors that occur during the process
     *
     * @param uploadDir The target directory path where the file will be saved
     * @param filename The name to save the file as in the target directory
     * @param multipartFile The uploaded file from the HTTP multipart request
     * @throws IOException If there are issues creating directories, reading the uploaded file,
     *                     or writing to the destination path
     */
    public static void saveFile(String uploadDir,
                                    String filename,
                                    MultipartFile multipartFile) throws IOException {

        // Convert the upload directory string to a Path object for file operations
        Path uploadPath = Paths.get(uploadDir);

        // Create the upload directory and any necessary parent directories if they don't exist
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Get input stream from the uploaded file and copy it to destination path
        // Using try-with-resources to ensure the input stream is properly closed
        try(InputStream inputStream = multipartFile.getInputStream();) {
            Path path = uploadPath.resolve(filename);
            System.out.println("FilePath: "  + path);
            System.out.println("filename: " + filename);

            // Copy the file to destination, overwriting if a file with same name exists
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException ioe) {
            // Wrap and rethrow IO exceptions with more context about the failure
            throw new IOException("could not save image file: " + filename, ioe);
        }

    }



}
