package com.service.databaseservice.services;

import com.service.databaseservice.model.Image;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Blob;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UtilsTest {

    @Test
    void extractImageData_NullImage() {
        assertNull(Utils.extractImageData(null));
    }

    @Test
    void extractImageData_ValidImage() throws SQLException {
        Blob mockBlob = Mockito.mock(Blob.class);
        byte[] testData = {1, 2, 3, 4, 5}; // Example data
        when(mockBlob.getBytes(1, testData.length)).thenReturn(testData);
        when(mockBlob.length()).thenReturn((long) testData.length);

        Image image = new Image();
        image.setData(mockBlob); // Assuming Image has a setData method

        assertArrayEquals(testData, Utils.extractImageData(image));
    }

    @Test
    void extractImageData_ThrowsSQLException() throws SQLException {
        Blob mockBlob = Mockito.mock(Blob.class);
        when(mockBlob.getBytes(1, 1)).thenThrow(new SQLException("Test Exception"));

        Image image = new Image();
        image.setData(mockBlob);

        assertNull(Utils.extractImageData(image));
    }
}
