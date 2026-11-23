package org.example.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ResultStore {
    private final Path historyFile;

    public ResultStore(Path historyFile) {
        this.historyFile = historyFile;
    }

    public void save(String result) throws IOException {
        Path parent = historyFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(historyFile, result + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public List<String> readAll() throws IOException {
        if (Files.notExists(historyFile)) {
            return List.of();
        }

        return Files.readAllLines(historyFile, StandardCharsets.UTF_8);
    }
}
