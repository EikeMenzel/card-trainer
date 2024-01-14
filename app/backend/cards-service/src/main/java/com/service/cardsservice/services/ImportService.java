package com.service.cardsservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cardsservice.payload.in.export.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.ZipInputStream;

@Service
public class ImportService {
    private final ObjectMapper objectMapper;
    private final DbQueryService dbQueryService;
    private final Logger logger = LoggerFactory.getLogger(ImportService.class);

    public ImportService(ObjectMapper objectMapper, DbQueryService dbQueryService) {
        this.objectMapper = objectMapper;
        this.dbQueryService = dbQueryService;
    }

    public HttpStatusCode processZipFile(MultipartFile multipartFile, Long userId) {
        File tmpDir = createTempDirectories();
        Optional<ExportDTO> exportDTOOptional = Optional.empty();

        try (var zipInputStream = new ZipInputStream(multipartFile.getInputStream())){
            var zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                if(zipEntry.getName().endsWith(".json")) {
                    var byteArrayOutputStream = new ByteArrayOutputStream();
                    var buffer = new byte[1024];
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        byteArrayOutputStream.write(buffer, 0, len);
                    }
                    var byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    exportDTOOptional = Optional.of(objectMapper.readValue(byteArrayInputStream, ExportDTO.class));

                } else if(isImage(zipEntry.getName())) {
                    saveImageToFile(zipInputStream, tmpDir, zipEntry.getName());
                }

                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }

        if(exportDTOOptional.isPresent()) {
            try {
                var exportDTO = processJsonFile(exportDTOOptional.get(), tmpDir);
                HttpStatusCode statusCode =  dbQueryService.importDeck(userId, exportDTO);
                Utils.deleteDirectory(tmpDir);
                return statusCode;
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private ExportDTO processJsonFile(ExportDTO exportDTO, File tmpDir) throws IOException {
        for (CardExportDTO cardExportDTO : exportDTO.cardExportDTOList()) {
            cardExportDTO.getCardDTO().setImage(findImageAsByteArray(tmpDir, cardExportDTO.getCardDTO().getImagePath()));

            if (cardExportDTO instanceof MultipleChoiceCardDTO multipleChoiceCardDTO) {
                for (ChoiceAnswerDTO choice : multipleChoiceCardDTO.getChoiceAnswers()) {
                    choice.setImage(findImageAsByteArray(tmpDir, choice.getImagePath()));
                }
            } else if (cardExportDTO instanceof TextAnswerDTO textAnswerCardDTO) {
                textAnswerCardDTO.setImage(findImageAsByteArray(tmpDir, textAnswerCardDTO.getImagePath()));
            } else {
                logger.info("Encountered an unrecognized CardExportDTO subtype");
            }
        }
        return exportDTO;
    }

    @java.lang.SuppressWarnings("java:S1168")
    public byte[] findImageAsByteArray(File tmpDir, String imageName) throws IOException {
        if(imageName == null)
            return null;

        var imagePath = Path.of(tmpDir.getPath(), imageName);

        if (Files.exists(imagePath)) {
            return Files.readAllBytes(imagePath);
        } else {
            return null; // Image not found
        }
    }

    public void saveImageToFile(ZipInputStream zipInputStream, File tmpDir, String fileName) {
        try {

            try (OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(tmpDir.toPath().resolve(fileName)))) {
                var buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private boolean isImage(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".png") ||
                lowerCaseFileName.endsWith(".jpg") ||
                lowerCaseFileName.endsWith(".jpeg") ||
                lowerCaseFileName.endsWith(".gif");
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

}
