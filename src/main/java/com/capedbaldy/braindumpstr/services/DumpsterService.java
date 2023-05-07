package com.capedbaldy.braindumpstr.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public interface DumpsterService {
    void addTagsToDump(Long dumpId, Set<String> tags);

    void removeTagsFromDump(Long dumpId, Set<String> tags);

    Long createNewDump(MultipartFile audioFile, String context);

    void reSummarize(Long dumpId, MultipartFile audioFile, String context);

    Set<Long> search(Optional<Date> startDate, Optional<Date> endData, Optional<Set<String>> tags, Optional<String> context);
}
