package com.capedbaldy.braindumpstr.controllers;

import com.capedbaldy.braindumpstr.services.DumpsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/dumpstr")
public class DumpsterController {

    private final DumpsterService dumpsterService;

    @Autowired
    public DumpsterController(DumpsterService dumpsterService) {
        this.dumpsterService = dumpsterService;
    }


    @PutMapping("/{dumpId}/tags/{tags}")
    public ResponseEntity<?> addTagsToDump(@PathVariable Long dumpId, @PathVariable Set<String> tags)
    {
        dumpsterService.addTagsToDump(dumpId, tags);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{dumpId}/tags/{tags}")
    public ResponseEntity<?> removeTagsToDump(@PathVariable Long dumpId, @PathVariable Set<String> tags)
    {
        dumpsterService.removeTagsFromDump(dumpId, tags);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/dump")
    public ResponseEntity<?> createNewDump(@RequestPart("file")MultipartFile audioFile, @RequestParam("context")String context)
    {
        Long dumpId = dumpsterService.createNewDump(audioFile, context);
        return ResponseEntity.ok(dumpId);
    }

    @PostMapping("/{dumpId}/collect")
    public ResponseEntity<?> reSummarizeDump(@PathVariable Long dumpId, @RequestPart("file")MultipartFile audioFile, @RequestParam("context")String context)
    {
        dumpsterService.reSummarize(dumpId, audioFile, context);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchDumpster(@RequestParam("StartDate")Optional<Date> startDate,
                                            @RequestParam("EndDate")Optional<Date> endDate,
                                            @RequestParam("Tags")Optional<Set<String>> tags,
                                            @RequestParam("SearchText")Optional<String> searchText) {

        Set<Long> dumps = dumpsterService.search(startDate, endDate, tags, searchText);
        return ResponseEntity.ok(dumps);
    }
}
