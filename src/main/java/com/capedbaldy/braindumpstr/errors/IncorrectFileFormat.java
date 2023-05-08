package com.capedbaldy.braindumpstr.errors;

import org.springframework.web.multipart.MultipartFile;

public class IncorrectFileFormat extends RuntimeException{
    public FileObjectType getFileObjectType() {
        return fileObjectType;
    }

    public String[] getExpectedFileFormats() {
        return expectedFileFormats;
    }

    public Object getFile() {
        return file;
    }

    private enum FileObjectType {
        MULTI_PART_FILE
    }
    private FileObjectType fileObjectType;
    private Object file;
    private String[] expectedFileFormats;

    public IncorrectFileFormat(MultipartFile multipartFile, String[] expectedFileFormats) {
        fileObjectType = FileObjectType.MULTI_PART_FILE;
        file = multipartFile;
        this.expectedFileFormats = expectedFileFormats;
    }


}
