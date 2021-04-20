package ua.nure.util;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathsUtil {

    private static final Path root = FileSystems.getDefault().getPath("").toAbsolutePath();

    private static final Path resources = Paths.get("src", "main", "resources");

    public static Path getResourcePath(String fileName) {
        return Paths.get(root.toString(), resources.toString(), fileName);
    }
}
