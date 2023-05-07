package com.capedbaldy.braindumpstr.services.dumpsterservice;

import com.capedbaldy.braindumpstr.models.AudioFile;
import com.capedbaldy.braindumpstr.models.Dump;
import com.capedbaldy.braindumpstr.repositories.AudioRepository;
import com.capedbaldy.braindumpstr.repositories.DumpRepository;
import com.capedbaldy.braindumpstr.repositories.TagRepository;
import com.capedbaldy.braindumpstr.services.DumpsterService;
import com.capedbaldy.braindumpstr.services.FileIOService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class CreateNewDumpTests {


    private final byte[] audioFileBytes = {3, 4, 1};
    private MockMultipartFile audioFile;
    @MockBean
    private TagRepository tagRepository;
    @MockBean
    private DumpRepository dumpRepository;
    @MockBean
    private AudioRepository audioRepository;
    private ArrayList<Dump> dumps;
    private ArrayList<AudioFile> audioFiles;
    @Autowired
    private DumpsterService dumpsterService;
    private Long dumpId;
    @MockBean
    private FileIOService fileIOService;
    private HashMap<String, byte[]> files;
    @Value("${audio.storage.directory}")
    private String audioFileStorageDirectory;

    @BeforeEach
    public void setUp() throws IOException {
        dumpId = 1L;
        dumps = new ArrayList<>();
        files = new HashMap<>();
        audioFiles = new ArrayList<>();

        // Audio file mocks
        audioFile = new MockMultipartFile("file",
                "test-audio.mp3", "audio/mpeg", audioFileBytes);

        // Mock FileIOService
        doAnswer(invocationOnMock -> {
            files.put(invocationOnMock.getArgument(0), invocationOnMock.getArgument(1));
            return 2;
        }).when(fileIOService).save(any(), any(), any());
        doAnswer(invocationOnMock -> files.containsKey((String) invocationOnMock.getArgument(0))).when(fileIOService).fileExist(any());

        // Mock Dump Repository
        doAnswer(invocationOnMock -> {
            Dump dump = invocationOnMock.getArgument(0);
            dump.setDumpId(dumps.size() + 1L);
            dumps.add(dump);
            return dump;
        }).when(dumpRepository).save(any());

        doAnswer(invocationOnMock -> Optional.of(dumps.get(((Long) invocationOnMock.getArgument(0)).intValue() - 1))).
                when(dumpRepository).findById(any());

        // Mock Audio Repository
        doAnswer(invocationOnMock -> {
            AudioFile audioFile = invocationOnMock.getArgument(0);
            if (audioFiles.contains(audioFile))
                return audioFile;
            audioFile.setId(audioFiles.size() + 1L);
            audioFiles.add(audioFile);
            return audioFile;
        }).when(audioRepository).save(any());
    }

    @Test
    public void runs() {
        // Arrange


        // Act


        // Assert


    }


    @Test
    public void testCreateNewDump_ValidInputs_ShouldCallFileServiceToSaveFile_ShouldSaveInRepositories() throws IOException {
        // Arrange
        String context = "My context";

        // Act
        Long dumpId = dumpsterService.createNewDump(audioFile, context);

        // Assert
        verify(fileIOService, times(1))
                .save(audioFileStorageDirectory, audioFileBytes,
                        dumpId + audioFile.getOriginalFilename().substring(audioFile.getOriginalFilename()
                                .lastIndexOf('.')));
        verify(dumpRepository, times(1))
                .save(argThat(a -> Objects.equals(a.getDumpId(), dumps.indexOf(a) + 1L)));
        verify(audioRepository, times(2))
                .save(argThat(a -> Objects.equals(a.getId(), audioFiles.indexOf(a) + 1L)));
        assertThat(dumpRepository.findById(dumpId)).isPresent();
        //noinspection OptionalGetWithoutIsPresent
        assertThat(((String) dumpRepository.findById(dumpId).get().getContext())).isEqualTo(context);
        assertThat(dumps.get(dumpId.intValue() -1).getDumpId()).isEqualTo(dumpId);
    }


}
