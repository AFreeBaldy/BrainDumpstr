package com.capedbaldy.braindumpstr.services;


import com.capedbaldy.braindumpstr.errors.DumpDoesNotExistException;
import com.capedbaldy.braindumpstr.errors.DumpsterServiceErrorHandler;
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
import org.springframework.data.util.Optionals;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private HashSet<Tag> tags;
    private HashSet<Dump> dumps;
    private Dump dump;


    public void setUpAddTagsToDumpTests() {
        when(tagRepository.findAll()).thenAnswer(invocation -> tags.stream().toList());
        when(tagRepository.saveAll(any())).thenAnswer(invocationOnMock -> {
            Set<Tag> tagsToSave = invocationOnMock.getArgument(0);
            tags.addAll(tagsToSave);
            return tags.stream().toList();
        });
        when(dumpRepository.findById(eq(dump.getDumpId()))).thenReturn(Optional.ofNullable(dump));
        when(dumpRepository.save(dump)).thenAnswer(invocationOnMock -> {
            dump = invocationOnMock.getArgument(0);
            return dump;
        });
    }

    public void setUpRemoveTagsFromDumpTests() {
        tags.add(Tag.builder().value("Hello").dumps(dumps).build());
        tags.add(Tag.builder().value("World").dumps(dumps).build());
        dumps.add(Dump.builder().dumpId(1L).tags(tags).build());
        dumps.add(Dump.builder().dumpId(2L).tags(tags).build());

        when(dumpRepository.findById(any())).thenAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            return dumps.stream().filter(dump1 -> dump1.getDumpId().equals(id)).findFirst();
        });
        when(dumpRepository.save(any())).thenReturn(null);

        doAnswer(invocationOnMock -> {
            HashSet<Tag> setToDelete = invocationOnMock.getArgument(0);
            tags.removeAll(setToDelete);
            return null;
        }).when(tagRepository).deleteAll(any());
        doAnswer(invocationOnMock -> {
            Set<String> values = invocationOnMock.getArgument(0);
            return tags.stream().filter(e -> values.contains(e.getValue())).collect(Collectors.toSet());
        }).when(tagRepository).findTagsByValueIn(any());

    }




    @BeforeEach
    public void setUp() {
        tags = new HashSet<>();
        dumps = new HashSet<>();
        dump = Dump.builder().dumpId(1L)
                .build();
    }

    @Test
    public void testAddingTagsToDump_ValidInput_ShouldCallRepositoryQueryCommand() {
        setUpAddTagsToDumpTests();
        // Arrange
        Long dumpId = 1L;
        Set<String> tagsToInsert = Set.of("Default", "Hello");

        // Act
        dumpsterService.addTagsToDump(dumpId, tagsToInsert);


        // Assert
        verify(dumpRepository, times(1)).findById(dumpId);
        verify(dumpRepository, times(1)).save(dump);
        verify(tagRepository, times(1)).findAll();
        verify(tagRepository, times(1)).saveAll(any());
        assertThat(tagRepository.findAll().stream().map(Tag::getValue)).containsOnlyOnceElementsOf(tagsToInsert);
        assertTrue(tagRepository.findAll().stream()
                .filter(tag -> tagsToInsert.contains(tag.getValue()))
                .allMatch(tag -> tag.getDumps().contains(dump)));
    }

    @Test
    public void testAddingTagsToDump_DumpIdDoesntExist_ShouldError() {
        setUpAddTagsToDumpTests();
        // Arrange
        Long dumpId = 2L;
        Set<String> tagsToInsert = Set.of("Default", "Hello");

        // Act and assert
        assertThrows(DumpDoesNotExistException.class, () -> dumpsterService.addTagsToDump(dumpId, tagsToInsert));
    }
    @Test
    public void testAddingTagsToDump_InvalidDumpId_ShouldError() {
        setUpAddTagsToDumpTests();
        // Arrange
        Long dumpId = -2L;
        Set<String> tagsToInsert = Set.of("Default", "Hello");

        // Act and assert
        assertThrows(IllegalArgumentException.class, () -> dumpsterService.addTagsToDump(dumpId, tagsToInsert));
    }

    @Test
    public void testAddingTagsToDump_NoTagsGiven_ShouldError() {
        setUpAddTagsToDumpTests();
        // Arrange
        Long dumpId = 1L;
        Set<String> tagsToInsert = Set.of();

        // Act and assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> dumpsterService.addTagsToDump(dumpId, tagsToInsert));
        assertThat(exception.getMessage()).isEqualTo("Tags field cannot be empty");
    }

    @Test
    public void testRemovingTagsFromDump_NoTagsGiven_ShouldError() {
        setUpRemoveTagsFromDumpTests();
        // Arrange
        Long dumpId = 1L;
        Set<String> tagsToInsert = Set.of();

        // Act and assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> dumpsterService.removeTagsFromDump(dumpId, tagsToInsert));
        assertThat(exception.getMessage()).isEqualTo("Tags field cannot be empty");
    }

    @Test
    public void testRemovingTagsFromDump_DumpIdDoesntExist_ShouldError() {
        setUpRemoveTagsFromDumpTests();
        // Arrange
        Long dumpId = -2L;
        Set<String> tagsToInsert = Set.of("Default", "Hello");

        // Act and assert
        assertThrows(IllegalArgumentException.class, () -> dumpsterService.removeTagsFromDump(dumpId, tagsToInsert));
    }

    @Test
    public void testRemovingTagsFromDump_InvalidDumpId_ShouldError() {
        setUpRemoveTagsFromDumpTests();
        // Arrange
        Long dumpId = 3L;
        Set<String> tagsToInsert = Set.of("Default", "Hello");

        // Act and assert
        assertThrows(DumpDoesNotExistException.class, () -> dumpsterService.removeTagsFromDump(dumpId, tagsToInsert));
    }

    @Test
    public void testRemovingTagsFromDump_ValidInput_ShouldCallRepositoryQueryCommand() {
        setUpRemoveTagsFromDumpTests();
        // Arrange
        Long dumpId = 1L;
        Set<String> tagsToInsert = Set.of("Hello");

        // Act
        dumpsterService.removeTagsFromDump(dumpId, tagsToInsert);


        // Assert
        verify(dumpRepository, times(1)).findById(dumpId);
        verify(dumpRepository, times(1)).save(argThat(dump -> dump.getDumpId().equals(dumpId)));
        verify(tagRepository, times(1)).deleteAll(any());
        assertThat(tags.stream()
                        .filter(tag -> tagsToInsert.contains(tag.getValue()))
                        .allMatch(tag -> tag.getDumps().stream().noneMatch(dump -> dump.getDumpId().equals(dumpId))))
                .isTrue();
        assertThat(dumps.stream()
                        .filter(dump -> dump.getDumpId().equals(dumpId))
                        .allMatch(dump -> dump.getTags().stream()
                                .allMatch(tag -> tagsToInsert.contains(tag.getValue()))))
                .isFalse();
        assertThat(dumps.stream()
                        .filter(dump -> !dump.getDumpId().equals(dumpId))
                        .anyMatch(dump -> dump.getTags().stream()
                                .anyMatch(tag -> tagsToInsert.contains(tag.getValue()))))
                .isTrue();
    }


}
