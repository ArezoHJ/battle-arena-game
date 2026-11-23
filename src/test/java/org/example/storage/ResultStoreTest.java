package org.example.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultStoreTest {
    @TempDir
    Path tempDir;

    @Test
    void keepsEarlierResultsWhenSavingANewResult() throws  IOException {
        Path file = tempDir.resolve("matches.txt");
        ResultStore store = new ResultStore(file);

        store.save("Alice;Warrior;Alice");
        store.save("Sam;Mage;Dator");

        assertEquals(
                List.of("Alice;Warrior;Alice", "Sam;Mage;Dator"),
                store.readAll()
        );
    }
}
