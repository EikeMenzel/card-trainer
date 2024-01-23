package com.service.cardsservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cardsservice.payload.Views;
import com.service.cardsservice.payload.in.export.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ExportService {
    private final ObjectMapper objectMapper;
    private final DbQueryService dbQueryService;
    private final Logger logger = LoggerFactory.getLogger(ExportService.class);

    public ExportService(DbQueryService dbQueryService, ObjectMapper objectMapper) {
        this.dbQueryService = dbQueryService;
        this.objectMapper = objectMapper;
    }

    public Optional<byte[]> zipDeck(Long userId, Long deckId) throws IOException {
        File uniqueDir = createTempDirectories();

        Optional<ExportDTO> exportDTOOptional = dbQueryService.getCardExportDTOsForExportDeck(userId, deckId);
        if (exportDTOOptional.isEmpty())
            return Optional.empty();

        processImages(exportDTOOptional.get(), uniqueDir);

        saveJsonToFile(exportDTOOptional.get(), uniqueDir);


        var zipFile = new File(uniqueDir.getParentFile(), uniqueDir.getName() + ".zip");
        zipDirectory(uniqueDir, zipFile);

        byte[] zipFileData = Files.readAllBytes(zipFile.toPath());

        Utils.deleteDirectory(uniqueDir);
        deleteZip(zipFile);

        return Optional.of(zipFileData);
    }

    private void saveJsonToFile(ExportDTO exportDTO, File uniqueDir) {
        try {
            var outputFile = new File(Path.of(uniqueDir.getAbsolutePath(), "data.json").toUri());
            objectMapper.writerWithView(Views.Export.class).writeValue(outputFile, exportDTO);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void processImages(ExportDTO exportDTO, File uniqueDir) throws IOException {
        for (CardExportDTO cardExportDTO : exportDTO.cardExportDTOList()) {
            String imagePathCardDTO = saveImageToFile(cardExportDTO.getCardDTO().image(), uniqueDir);
            cardExportDTO.getCardDTO().setImagePath(imagePathCardDTO);

            if (cardExportDTO instanceof MultipleChoiceCardDTO multipleChoiceCardDTO) {
                for (ChoiceAnswerDTO choice : multipleChoiceCardDTO.getChoiceAnswers()) {
                    String choiceAnswerImagePath = saveImageToFile(choice.image(), uniqueDir);
                    choice.setImagePath(choiceAnswerImagePath);
                }
            } else if (cardExportDTO instanceof TextAnswerDTO textAnswerCardDTO) {
                String textAnswerImagePath = saveImageToFile(textAnswerCardDTO.getImage(), uniqueDir);
                textAnswerCardDTO.setImagePath(textAnswerImagePath);
            } else {
                logger.info("Encountered an unrecognized CardExportDTO subtype");
            }
        }
    }

    private File createTempDirectories() {
        var tempDir = "temp";
        var rootTempDir = new File(tempDir);
        rootTempDir.mkdirs();

        var uniqueDirName = UUID.randomUUID().toString();
        var uniqueDir = new File(rootTempDir, uniqueDirName);
        uniqueDir.mkdirs();

        return uniqueDir;
    }

    private String saveImageToFile(byte[] imageData, File uniqueDir) throws IOException {
        if (imageData != null) {
            String imageFileName = UUID.randomUUID() + "." + getImageFormat(imageData);
            var imagePath = Path.of(uniqueDir.getAbsolutePath(), imageFileName);
            Files.write(imagePath, imageData);
            return imageFileName;
        }
        return null;
    }

    private void zipDirectory(File srcDir, File zipfile) throws IOException {
        try (var zipOutputStream = new ZipOutputStream(new FileOutputStream(zipfile))) {
            zipDirectoryHelper(srcDir, srcDir, zipOutputStream);
        }
    }

    private void zipDirectoryHelper(File rootDir, File srcDir, ZipOutputStream zipOut) throws IOException {
        File[] files = srcDir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                zipDirectoryHelper(rootDir, file, zipOut);
                continue;
            }

            String relativePath = rootDir.toURI().relativize(file.toURI()).getPath();
            var zipEntry = new ZipEntry(relativePath);
            zipOut.putNextEntry(zipEntry);

            try (var fileInputStream = new FileInputStream(file)) {
                var buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) > 0) {
                    zipOut.write(buffer, 0, bytesRead);
                }
            }
            zipOut.closeEntry();
        }
    }

    private void deleteZip(File zipFile) throws IOException {
        if (zipFile.exists())
            Files.delete(zipFile.toPath());
    }

    private String getImageFormat(byte[] imageData) {
        if (imageData.length >= 2) {
            if (imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8) {
                return "jpeg";
            } else if (imageData[0] == (byte) 0x89 && imageData[1] == (byte) 0x50) {
                return "png";
            }
        }
        return null;
    }
}
