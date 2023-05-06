package com.capedbaldy.braindumpstr.controllers;

import com.capedbaldy.braindumpstr.TestUtils;
import com.capedbaldy.braindumpstr.services.DumpsterService;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class DumpsterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DumpsterService dumpsterService;


    /**
     *
     */
    @Test
    public void addTagsToDump_ShouldReturnNoContent() throws Exception {
        // Arrange
        Set<String> tags = Set.of("Hello", "World");
        Long dumpId = 0L;
        doNothing().when(dumpsterService).addTagsToDump(any(), any());

        // Act
        mockMvc.perform(put("/api/dumpstr/{dumpId}/tags/{tags}", dumpId, String.join(",", tags))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Assert
        verify(dumpsterService, times(1)).addTagsToDump(dumpId, tags);
    }


    /**
     *
     */
    @Test
    public void removeTagsFromDump_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long dumpId = 0L;
        Set<String> tags = Set.of("Default");
        doNothing().when(dumpsterService).removeTagsFromDump(any(), any());

        // Act
        mockMvc.perform(delete("/api/dumpstr/{dumpId}/tags/{tags}", dumpId, String.join(",", tags))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Assert
        verify(dumpsterService, times(1)).removeTagsFromDump(dumpId, tags);
    }


    /**
     *
     */
    @Test
    public void createNewDump_ShouldReturndumpId() throws Exception {
        // Arrange
        Long dumpId = 0L;
        String context = "Test context";
        URL resourceUrl = Objects.requireNonNull(getClass().getClassLoader().getResource("com/capedbaldy/braindumpstr/testmp3.mp3"));
        Path filePath = Paths.get(resourceUrl.toURI());
        byte[] fileContent = Files.readAllBytes(filePath);
        MockMultipartFile file = new MockMultipartFile("file", "filename.mp3", String.valueOf(new MediaType("audio", "mpeg")), fileContent);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("context", context);
        doReturn(dumpId).when(dumpsterService).createNewDump(any(), eq(context));


        // Act
        mockMvc.perform(multipart("/api/dumpstr/dump").file(file).params(params)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk()).andExpect(content().string(String.valueOf(dumpId)));

        // Assert
        verify(dumpsterService, times(1)).createNewDump(any(), eq(context));
    }


    /**
     *
     */
    @Test
    public void generateNewSummary_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long dumpId = 0L;
        String context = "new context";
        URL resourceUrl = Objects.requireNonNull(getClass().getClassLoader().getResource("com/capedbaldy/braindumpstr/testmp3.mp3"));
        Path filePath = Paths.get(resourceUrl.toURI());
        byte[] fileContent = Files.readAllBytes(filePath);
        MockMultipartFile file = new MockMultipartFile("file", "filename.mp3", String.valueOf(new MediaType("audio", "mpeg")), fileContent);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("context", context);
        doNothing().when(dumpsterService).reSummarize(any(), any(), any());

        // Act
        MvcResult result = mockMvc.perform(multipart("/api/dumpstr/{dumpId}/collect", dumpId)
                        .file(file).params(params)
                .contentType(MediaType.MULTIPART_FORM_DATA)).andReturn();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
        verify(dumpsterService, times(1)).reSummarize(any(), any(), any());
    }


    /**
     *
     */
    @Test
    public void getDumpsBySearchCriteria_ShouldReturnDumps() throws Exception {
        // Arrange
        Long dumpId = 0L;
        String searchText = "search text";
        doReturn(Set.of(dumpId)).when(dumpsterService).search(any(), any(), any(), any());
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("SearchText", searchText);

        // Act
        MvcResult result = mockMvc.perform(post("/api/dumpstr/search").contentType(MediaType.APPLICATION_JSON)
                .content(map.toString())).andReturn();
        JSONArray data = new JSONArray(result.getResponse().getContentAsString());

        // Assert
        assertTrue(TestUtils.jsonArrayContains(data, dumpId));
        verify(dumpsterService, times(1)).search(any(), any(), any(), any());
    }

}
