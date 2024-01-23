package com.service.cardsservice.controller;

import com.service.cardsservice.services.DbQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ImageController.class)
@ContextConfiguration(classes = {ImageController.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private DbQueryService dbQueryService;

    @Autowired
    private ImageController imageController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void testSaveAccountImage() throws Exception {
        Long userId = 1L;

        byte[] imageBytes = new byte[]{(byte) 0xFF, (byte) 0xD8, 0x00, 0x10};
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpeg", "image/jpeg", imageBytes);

        Mockito.when(dbQueryService.saveImage(userId, imageFile.getBytes()))
                .thenReturn(Optional.of(1L));

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/v1/images");
        builder.with(new RequestPostProcessor() {
            @Override
            @NonNull
            public MockHttpServletRequest postProcessRequest(@NonNull MockHttpServletRequest request) {
                request.setMethod("POST");
                return request;
            }
        });
        mockMvc.perform(builder
                        .file(imageFile)
                        .header("userId", userId)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }

    @Test
    void testSaveAccountImageInvalid() throws Exception {
        long userId = 1L;

        byte[] imageBytes = new byte[]{};
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.png", "application/octet-stream", imageBytes);

        mockMvc.perform(multipart("/api/v1/images")
                        .file(imageFile)
                        .header("userId", Long.toString(userId)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testGetImage_Success() throws Exception {
        Long userId = 1L;
        Long imageId = 1L;
        byte[] imageData = new byte[]{(byte) 0xFF, (byte) 0xD8, 0x00, 0x10};

        when(dbQueryService.getImage(userId, imageId)).thenReturn(Optional.of(imageData));

        imageController.getImage(userId, imageId);

        mockMvc.perform(get("/api/v1/images/{imageId}", imageId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(content().bytes(imageData));
    }

    @Test
    void testGetImage_NotFound() throws Exception {
        Long userId = 1L;
        Long imageId = 1L;

        when(dbQueryService.getImage(userId, imageId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/images/{imageId}", imageId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
