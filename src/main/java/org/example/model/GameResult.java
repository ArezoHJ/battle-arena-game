package org.example.model;

public record GameResult(
        String playerName,
        String playerCharacter,
        String computerCharacter,
        String winner,
        int rounds) {

    public GameResult {
        if (playerName == null || playerName.isBlank()
                || playerCharacter == null || playerCharacter.isBlank()
                || computerCharacter == null || computerCharacter.isBlank()
                || winner == null || winner.isBlank()
                || rounds < 1) {
            throw new IllegalArgumentException("Matchresultatet innehåller ogiltiga värden.");
        }
    }
}
