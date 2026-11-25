package org.example.storage;

import org.example.model.GameResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public final class ResultStore {
    private static final String FIELD_SEPARATOR = "\t";
    private static final int FIELD_COUNT = 5;
    private final Path historyPath;

    public ResultStore(Path historyPath) {
        this.historyPath = historyPath;
    }

    public synchronized void save(GameResult result) throws IOException {
        Path parent = historyPath.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(historyPath,encode(result) + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public synchronized List<GameResult> readAll() throws IOException {
        if (Files.notExists(historyPath)) {
            return List.of();
        }

        List<GameResult> results = new ArrayList<>();
        for (String line : Files.readAllLines(historyPath, StandardCharsets.UTF_8)) {
            if (!line.isBlank()) {
                results.add(decode(line));
            }
        }
        return List.copyOf(results);
    }

    public static String encode(GameResult result) {
            Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
            return String.join(FIELD_SEPARATOR,
                    encodeField(encoder, result.playerName()),
                    encodeField(encoder, result.playerCharacter()),
                    encodeField(encoder, result.computerCharacter()),
                    encodeField(encoder, result.winner()),
                    Integer.toString(result.rounds()));
    }

    public static GameResult decode(String line) throws IOException {
        String[] fields = line.split(FIELD_SEPARATOR, -1);
        if (fields.length != FIELD_COUNT) {
            throw new IOException("En rad i matchhistoriken har fel format.");
        }
        try {
            Base64.Decoder decoder = Base64.getUrlDecoder();
            return new GameResult(
                    decodeField(decoder, fields[0]),
                    decodeField(decoder, fields[1]),
                    decodeField(decoder, fields[2]),
                    decodeField(decoder, fields[3]),
                    Integer.parseInt(fields[4]));
        } catch (IllegalArgumentException exception) {
            throw new IOException("En rad i matchhistoriken har fel format.", exception);
        }
    }

    private static String encodeField(Base64.Encoder encoder, String value) {
        return encoder.encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
    private static String decodeField(Base64.Decoder decoder, String value) {
        return new String(decoder.decode(value), StandardCharsets.UTF_8);
    }
}
