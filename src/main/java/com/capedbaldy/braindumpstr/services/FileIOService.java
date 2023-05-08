package com.capedbaldy.braindumpstr.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileIOService {
    void save(String directory, byte[] bytes, String fileName) throws IOException;

    boolean isFileOfType(MultipartFile file, String[] types);

    boolean fileExist(String path) throws IOException;
}
