package com.service.databaseservice.services;

import com.service.databaseservice.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    private Utils() {}
    public static byte[] extractImageData(Image image) {
        if (image == null)
            return null;

        try {
            return image.getData().getBytes(1, (int) image.getData().length());
        } catch (SQLException e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

}
