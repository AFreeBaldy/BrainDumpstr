package com.capedbaldy.braindumpstr.services;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface FileIOService {
    void save(String directory, byte[] bytes, String fileName) throws IOException;

    boolean fileExist(String path) throws IOException;
}
