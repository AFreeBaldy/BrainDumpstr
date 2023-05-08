package com.capedbaldy.braindumpstr.services.impl;

import com.capedbaldy.braindumpstr.errors.DumpDoesNotExistException;
import com.capedbaldy.braindumpstr.errors.IncorrectFileFormat;
import com.capedbaldy.braindumpstr.models.AudioFile;
import com.capedbaldy.braindumpstr.models.Dump;
import com.capedbaldy.braindumpstr.models.Tag;
import com.capedbaldy.braindumpstr.repositories.AudioRepository;
import com.capedbaldy.braindumpstr.repositories.DumpRepository;
import com.capedbaldy.braindumpstr.repositories.TagRepository;
import com.capedbaldy.braindumpstr.services.DumpsterService;
import com.capedbaldy.braindumpstr.services.FileIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DumpsterServiceImpl implements DumpsterService {
    private final DumpRepository dumpRepository;
    private final TagRepository tagRepository;
    private final FileIOService fileIOService;
    private final AudioRepository audioRepository;

    @Value("${audio.storage.directory}")
    private String savePath;

    @Autowired
    public DumpsterServiceImpl(DumpRepository dumpRepository, TagRepository tagRepository, FileIOService fileIOService, AudioRepository audioRepository) {
        this.dumpRepository = dumpRepository;
        this.tagRepository = tagRepository;
        this.fileIOService = fileIOService;
        this.audioRepository = audioRepository;
    }
    /**
     * @param dumpId
     * @param tags
     */
    @Override
    public void addTagsToDump(Long dumpId, Set<String> tags) {
        // Validate
        if (dumpId <= 0)
            throw new IllegalArgumentException(String.format("DumpId was %d expected it to be greater than 0", dumpId));
        if (tags == null || tags.isEmpty())
            throw new IllegalArgumentException("Tags field cannot be empty");

        // Get all tags
        Set<String> allTags = tagRepository.findAll().stream()
                .map(Tag::getValue).collect(Collectors.toSet());

        // Remove duplicate tags
        HashSet<Tag> filteredTags = (HashSet<Tag>) tags.stream().filter(tag -> !allTags.contains(tag))
                .map(tag -> Tag.builder().value(tag).build()).collect(Collectors.toSet());


        // Get Dump
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
        // Validate
        if (dumpId <= 0)
            throw new IllegalArgumentException(String.format("DumpId was %d expected it to be greater than 0", dumpId));
        if (tags == null || tags.isEmpty())
            throw new IllegalArgumentException("Tags field cannot be empty");

        // Get Dump
        Optional<Dump> optionalDump = dumpRepository.findById(dumpId);
        if (optionalDump.isEmpty())
            throw new DumpDoesNotExistException();
        Dump dump = optionalDump.get();

        // Assert that tags are present in dump
        boolean containsTags = dump.getTags().stream().anyMatch(e -> tags.contains(e.getValue()));
        if (!containsTags)
            throw new IllegalArgumentException(String.format("Some of the given tags [%s] are not associated with Dump of DumpId %d",
                    tags.stream().filter(t -> dump.getTags().stream().anyMatch(e -> e.getValue().equals(t))).collect(Collectors.joining(",")),
                    dumpId));

        // Remove tags from dump
        dump.setTags((HashSet<Tag>) dump.getTags().stream().filter(tag -> !tags.contains(tag.getValue())).collect(Collectors.toSet()));
        dumpRepository.save(dump);

        // Remove dump for tags
        HashSet<Tag> tagsWithTagsAsValue = tagRepository.findTagsByValueIn(tags);
        tagsWithTagsAsValue
                .forEach(t -> t.setDumps((HashSet<Dump>) t.getDumps().stream().filter(d -> !d.getDumpId().equals(dumpId)).collect(Collectors.toSet())));

        // Delete empty tags
        tagRepository.deleteAll(tagsWithTagsAsValue.stream().filter(t -> t.getDumps().size() == 0).collect(Collectors.toSet()));
    }


    /**
     * @param multipartFile
     * @param context
     */
    @Override
    public Long createNewDump(MultipartFile multipartFile, String context) throws IncorrectFileFormat {
        // Validate audio file
        String[] expectedFileFormats = {"mp3"};
        if (!fileIOService.isFileOfType(multipartFile, expectedFileFormats))
            throw new IncorrectFileFormat(multipartFile, expectedFileFormats);

        AudioFile audioFile = AudioFile.builder()
                .mimeType(multipartFile.getContentType())
                .build();
        String extension = Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename()
                .lastIndexOf('.'));
        Dump dump = Dump.builder().context(context).build();
        dump.getAudioFiles().add(audioFile);

        // Save dump
        dump = dumpRepository.save(dump);

        // Save audio file
        audioFile = audioRepository.save(audioFile);
        String audioFilePath = savePath + audioFile.getId() + extension;
        audioFile.setPath(audioFilePath);
        audioFile = audioRepository.save(audioFile);

        // Save to IO
        try {
            fileIOService.save(savePath, multipartFile.getBytes(), String.valueOf(audioFile.getId()) + extension);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return dump.getDumpId();
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
