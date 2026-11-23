package org.example.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ResultStore {
    private final Path file;

    public ResultStore(Path file) {
        this.file = file;
    }

    public void save(String result) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(file, result + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public List<String> readAll() throws IOException {
        if (Files.exists(file)) {
            return List.of();
        }

        return Files.readAllLines(file, StandardCharsets.UTF_8);
    }
}
