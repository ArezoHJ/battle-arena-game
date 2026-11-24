package org.example.storage;

import org.example.model.GameResult;
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
        ResultStore store = new ResultStore(tempDir.resolve("matches.txt"));
        GameResult first = new GameResult("Alice", "Warrior", "Mage", "Alice", 3);
        GameResult second = new GameResult("Sam", "Mage", "Ranger", "Dator", 5);

        store.save(first);
        store.save(second);

        assertEquals(
                List.of(first, second),
                store.readAll()
        );
    }

    @Test
    void encodeResultCanBeDecoded() throws IOException {
        GameResult result = new GameResult("Åsa\tTest", "Mage", "Ranger", "Åsa\tTest", 4);

        assertEquals(result, ResultStore.decode(ResultStore.encode(result)));
    }
}
