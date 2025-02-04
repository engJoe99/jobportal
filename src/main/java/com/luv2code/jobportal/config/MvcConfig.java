package com.luv2code.jobportal.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private static final String UPLOAD_DIR = "photos";

    // Override method to configure resource handlers for serving static resources
    // This method sets up handlers for serving static files (like images) from the filesystem
    // It calls exposeDirectory() to map the UPLOAD_DIR ("photos") to a URL pattern
    // allowing uploaded files to be served via HTTP requests
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory(UPLOAD_DIR, registry);
    }



    // Helper method to expose a directory as a static resource location, allowing files to be served statically
    // This method configures Spring MVC to serve files from a specific directory on the filesystem
    // For example, if uploadDir is "photos", files in that directory will be accessible via /photos/**
    // @param uploadDir - The directory path to expose (e.g. "photos", "uploads", etc)
    // @param registry - The ResourceHandlerRegistry used to configure resource handling rules
    private void exposeDirectory(String uploadDir, ResourceHandlerRegistry registry) {
        // Convert the upload directory string to a Path object for filesystem operations
        Path path = Paths.get(uploadDir);
        // Configure the resource handler mapping for this directory
        // Pattern: /{uploadDir}/** will match all files under the upload directory
        // For example: /photos/image.jpg will serve image.jpg from the photos directory
        registry.addResourceHandler("/" + uploadDir + "/**")
                // Map the URL pattern to the actual filesystem location using file: protocol
                // Uses absolute path to ensure correct file resolution regardless of working directory
                // The trailing slash is required to properly resolve subdirectories
                .addResourceLocations("file:" + path.toAbsolutePath() + "/");
    }

}


