package ua.nure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.util.PathsUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin
@RequestMapping(path="/admin")
@Api(tags = "Admin")
public class AdminController {

    private final Path root = FileSystems.getDefault()
            .getPath("").toAbsolutePath();

    @GetMapping("/backup")
    @ApiOperation(
            value = "Performs data backup and returns mysql dump file",
            nickname = "getBackupData"
    )
    public ResponseEntity<?> getBackupData()
            throws IOException, URISyntaxException {

        createBackupData();

        Path filePath = PathsUtil.getResourcePath("backup/backup_data.sql");
        File file = new File(filePath.toString());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=backup_data.sql");
        header.add("Cache-Control",
                "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        InputStreamResource resource =
                new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    private void createBackupData() {
        ProcessBuilder processBuilder = new ProcessBuilder();

        Path filePath = PathsUtil.getResourcePath("backup/backup.sh");

        processBuilder.command(filePath.toString());

        try {
            Process process = processBuilder.start();

            if (process.waitFor() == 0) {
                System.out.println("Success!");
            } else {
                System.out.println("Failed...");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
