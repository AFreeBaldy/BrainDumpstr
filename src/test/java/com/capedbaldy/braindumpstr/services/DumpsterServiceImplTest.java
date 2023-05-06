package com.capedbaldy.braindumpstr.services;


import com.capedbaldy.braindumpstr.models.Dump;
import com.capedbaldy.braindumpstr.models.Tag;
import com.capedbaldy.braindumpstr.repositories.DumpRepository;
import com.capedbaldy.braindumpstr.repositories.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@WebMvcTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class DumpsterServiceImplTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DumpRepository dumpRepository;

    @MockBean
    private TagRepository tagRepository;

    @Autowired
    private DumpsterService dumpsterService;

    private Set<Tag> tags;
    private Dump dump;


    @BeforeEach
    public void setUp() {
        tags = new HashSet<>();
        when(tagRepository.findAll()).thenAnswer(invocation -> tags.stream().toList());
        when(tagRepository.saveAll(any())).thenAnswer(invocationOnMock -> {
            Set<Tag> tagsToSave = invocationOnMock.getArgument(0);
            tags.addAll(tagsToSave);
            return tags.stream().toList();
        });
    }

    @Test
    public void testAddingTagsToDump_ValidInput_ShouldCallRepositoryQueryCommand() {
        // Arrange
        Long dumpId = 1L;
        dump = Dump.builder().dumpId(1L)
                .build();
        Set<String> tagsToInsert = Set.of("Default", "Hello");
        when(dumpRepository.findById(eq(dump.getDumpId()))).thenReturn(Optional.ofNullable(dump));
        when(dumpRepository.save(dump)).thenAnswer(invocationOnMock -> {
            dump = invocationOnMock.getArgument(0);
            return dump;
        });

        // Act
        dumpsterService.addTagsToDump(dumpId, tagsToInsert);


        // Assert
        verify(dumpRepository, times(1)).findById(dumpId);
        verify(dumpRepository, times(1)).save(dump);
        verify(tagRepository, times(1)).findAll();
        verify(tagRepository, times(1)).saveAll(any());
        assertThat(tagRepository.findAll().stream().map(Tag::getValue)).containsAll(tagsToInsert);
        assertTrue(tagRepository.findAll().stream()
                .filter(tag -> tagsToInsert.contains(tag.getValue()))
                .allMatch(tag -> tag.getDumps().contains(dump)));

    }
}
