package com.kyotu.largefilereadchallenge.configuration;

import com.kyotu.largefilereadchallenge.exception.FileReadException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RequiredArgsConstructor
public class DynamicResourceLoader implements ResourceLoader {

    private final ResourceLoader delegate;

    @Override
    public Resource getResource(String location) {
        File file = new File(location);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            return new InputStreamResource(new BufferedInputStream(inputStream));
        } catch (FileNotFoundException e) {
            throw new FileReadException("File not found: ".concat(e.getMessage()));
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.delegate.getClassLoader();
    }
}
