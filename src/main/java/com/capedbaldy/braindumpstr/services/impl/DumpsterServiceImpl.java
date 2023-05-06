package com.capedbaldy.braindumpstr.services.impl;

import com.capedbaldy.braindumpstr.errors.DumpDoesNotExistException;
import com.capedbaldy.braindumpstr.errors.InvalidDumpIdException;
import com.capedbaldy.braindumpstr.models.Dump;
import com.capedbaldy.braindumpstr.models.Tag;
import com.capedbaldy.braindumpstr.repositories.DumpRepository;
import com.capedbaldy.braindumpstr.repositories.TagRepository;
import com.capedbaldy.braindumpstr.services.DumpsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DumpsterServiceImpl implements DumpsterService {
    private final DumpRepository dumpRepository;
    private final TagRepository tagRepository;

    @Autowired
    public DumpsterServiceImpl(DumpRepository dumpRepository, TagRepository tagRepository) {
        this.dumpRepository = dumpRepository;
        this.tagRepository = tagRepository;
    }
    /**
     * @param dumpId
     * @param tags
     */
    @Override
    public void addTagsToDump(Long dumpId, Set<String> tags) {
        // Get all tags
        Set<String> allTags = tagRepository.findAll().stream()
                .map(Tag::getValue).collect(Collectors.toSet());

        // Remove duplicate tags
        HashSet<Tag> filteredTags = (HashSet<Tag>) tags.stream().filter(tag -> !allTags.contains(tag))
                .map(tag -> Tag.builder().value(tag).build()).collect(Collectors.toSet());


        // Get Dump
        if (dumpId <= 0)
            throw new InvalidDumpIdException();
        Optional<Dump> optionalDump = dumpRepository.findById(dumpId);
        if (optionalDump.isEmpty())
            throw new DumpDoesNotExistException();
        Dump dump = optionalDump.get();

        // Add Dump to Filtered Tags
        filteredTags.forEach(tag -> tag.getDumps().add(dump));
        // Add Filtered Tags to Dump
        dump.getTags().addAll(filteredTags);

        // Save
        tagRepository.saveAll(filteredTags);
        dumpRepository.save(dump);
    }

    /**
     * @param dumpId
     * @param tags
     */
    @Override
    public void removeTagsFromDump(Long dumpId, Set<String> tags) {

    }

    /**
     * @param audioFile
     * @param context
     */
    @Override
    public Long createNewDump(MultipartFile audioFile, String context) {
        return null;
    }

    /**
     * @param audioFile
     * @param context
     */
    @Override
    public void reSummarize(Long dumpId, MultipartFile audioFile, String context) {

    }

    /**
     * @param startDate
     * @param endData
     * @param tags
     * @param context
     */
    @Override
    public Set<Long> search(Optional<Date> startDate, Optional<Date> endData, Optional<Set<String>> tags, Optional<String> context) {

        return null;
    }
}
