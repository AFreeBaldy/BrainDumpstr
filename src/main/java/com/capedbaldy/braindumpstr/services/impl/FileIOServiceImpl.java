package com.capedbaldy.braindumpstr.services.impl;

import com.capedbaldy.braindumpstr.services.FileIOService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Service
public class FileIOServiceImpl implements FileIOService {
    /**
     * @param directory
     * @param bytes
     * @param fileName
     * @throws IOException
     */
    @Override
    public void save(String directory, byte[] bytes, String fileName) throws IOException {

    }

    /**
     * @param file
     * @param types
     * @return
     */
    @Override
    public boolean isFileOfType(MultipartFile file, String[] types) {
        int lastIndex = file.getOriginalFilename().lastIndexOf('.');
        if (lastIndex == file.getOriginalFilename().length())
            throw new StringIndexOutOfBoundsException();

        String fileType = Objects.requireNonNull(file.getOriginalFilename()).substring(lastIndex + 1);
        return Arrays.stream(types).anyMatch(s -> s.equals(fileType));
    }

    /**
     * @param path
     * @return
     * @throws IOException
     */
    @Override
    public boolean fileExist(String path) throws IOException {
        return false;
    }
}
