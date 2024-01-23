package com.service.databaseservice.controller;

import com.service.databaseservice.model.Image;
import com.service.databaseservice.payload.inc.ImageDataDTO;
import com.service.databaseservice.repository.ImageRepository;
import com.service.databaseservice.services.ImageService;
import com.service.databaseservice.services.UserLoginTrackerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserLoginTrackerController.class)
@ContextConfiguration(classes = {ImageController.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@WebAppConfiguration
class ImageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @InjectMocks
    private ImageController imageController;

    @MockBean
    private ImageService imageService;

    @MockBean
    private ImageRepository imageRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        when(imageRepository.getImageByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(new Image()));
        when(imageService.saveImage(anyLong(), any(byte[].class))).thenReturn(Optional.of(1L));
    }


    @Test
    void whenGetImage_WithInvalidUserIdOrImageId_thenReturnsNotFound() throws Exception {
        when(imageRepository.getImageByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/db/users/1/images/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenSaveImage_WithInvalidUserIdOrImageData_thenReturn415() throws Exception {
        when(imageService.saveImage(anyLong(), any(byte[].class))).thenReturn(Optional.empty());

        MockMultipartFile file = new MockMultipartFile("imageData", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());

        mockMvc.perform(multipart("/api/v1/db/users/1/images")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isUnsupportedMediaType());
    }
}
