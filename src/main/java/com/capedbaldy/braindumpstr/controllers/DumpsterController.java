package com.capedbaldy.braindumpstr.controllers;

import com.capedbaldy.braindumpstr.errors.IncorrectFileFormat;
import com.capedbaldy.braindumpstr.services.DumpsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

/**
 * DumpsterController provides RESTful API endpoints for managing and searching "dumps"
 * in the BrainDumpstr application.
 */
@RestController
@RequestMapping("/api/dumpstr")
public class DumpsterController {

    private final DumpsterService dumpsterService;

    /**
     * Constructs a new DumpsterController with the specified DumpsterService.
     *
     * @param dumpsterService the DumpsterService to use for managing dumps
     */
    @Autowired
    public DumpsterController(DumpsterService dumpsterService) {
        this.dumpsterService = dumpsterService;
    }

    /**
     * Adds tags to the specified dump.
     *
     * @param dumpId the ID of the dump to add tags to
     * @param tags   the set of tags to add to the dump
     * @return a ResponseEntity with a 204 No Content status code
     */
    @PutMapping("/{dumpId}/tags/{tags}")
    public ResponseEntity<?> addTagsToDump(@PathVariable Long dumpId, @PathVariable Set<String> tags) {
        dumpsterService.addTagsToDump(dumpId, tags);
        return ResponseEntity.noContent().build();
    }

    /**
     * Removes tags from the specified dump.
     *
     * @param dumpId the ID of the dump to remove tags from
     * @param tags   the set of tags to remove from the dump
     * @return a ResponseEntity with a 204 No Content status code
     */
    @DeleteMapping("/{dumpId}/tags/{tags}")
    public ResponseEntity<?> removeTagsToDump(@PathVariable Long dumpId, @PathVariable Set<String> tags) {
        dumpsterService.removeTagsFromDump(dumpId, tags);
        return ResponseEntity.noContent().build();
    }

    /**
     * Creates a new dump with the specified audio file and context.
     *
     * @param audioFile the audio file for the new dump
     * @param context   the context for the new dump
     * @return a ResponseEntity with a 200 OK status code and the ID of the created dump
     */
    @PostMapping("/dump")
    public ResponseEntity<?> createNewDump(@RequestPart("file") MultipartFile audioFile, @RequestParam("context") String context) {
        Long dumpId = dumpsterService.createNewDump(audioFile, context);
        return ResponseEntity.ok(dumpId);
    }

    /**
     * Re-summarizes the specified dump with a new audio file and context.
     *
     * @param dumpId    the ID of the dump to re-summarize
     * @param audioFile the new audio file for re-summarizing the dump
     * @param context   the new context for re-summarizing the dump
     * @return a ResponseEntity with a 204 No Content status code
     */
    @PostMapping("/{dumpId}/collect")
    public ResponseEntity<?> reSummarizeDump(@PathVariable Long dumpId, @RequestPart("file") MultipartFile audioFile, @RequestParam("context") String context) {
        dumpsterService.reSummarize(dumpId, audioFile, context);
        return ResponseEntity.noContent().build();
    }

    /**
     * Searches for dumps based on the specified search criteria.
     *
     * @param startDate  the optional start date of the search range
     * @param endDate    the optional end date of the search range
     * @param tags       the optional set of tags to filter the search results
     * @param searchText the optional text query to search in the dump content
     * @return a ResponseEntity with a 200 OK status code and a set of dump IDs matching the search criteria
     */
    @PostMapping("/search")
    public ResponseEntity<?> searchDumpster(@RequestParam("StartDate") Optional<Date> startDate,
                                            @RequestParam("EndDate") Optional<Date> endDate,
                                            @RequestParam("Tags") Optional<Set<String>> tags,
                                            @RequestParam("SearchText") Optional<String> searchText) {
        Set<Long> dumps = dumpsterService.search(startDate, endDate, tags, searchText);
        return ResponseEntity.ok(dumps);
    }

}
