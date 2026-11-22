package org.example.game;

import org.example.model.ActionType;
import org.example.model.Character;
import org.example.model.GameResult;

import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

public final class Game {
    private final String playerName;
    private final Character player;
    private final Character computer;
    private final ComputerPlayer computerPlayer;

    public Game(String playerName, Character player, Character computer, ComputerPlayer computerPlayer) {
        if (playerName == null || playerName.isBlank()) {
            throw new IllegalArgumentException("Spelarens namn får inte vara tomt.");
        }
        this.playerName = playerName;
        this.player = player;
        this.computer = computer;
        this.computerPlayer = computerPlayer;
    }

    public GameResult play(Scanner input, PrintStream output) {
        boolean playerSpecialAvailable = true;
        boolean computerSpecialAvailable = true;
        int rounds = 0;

        output.printf("%s (%s) möter Dator (%s)!%n", playerName, player.getType(), computer.getType());
        while (player.isAlive() && computer.isAlive()) {
            rounds++;
            output.printf("%n--- Runda %d ---%n", rounds);
            ActionType playerAction = readPlayerAction(input, output, playerSpecialAvailable);
            playerSpecialAvailable = performAction(player, computer, playerAction, output, playerName,
                    playerSpecialAvailable);
            printStatus(output);
            if (!computer.isAlive()) {
                break;
            }

            ActionType computerAction = computerPlayer.chooseAction(computerSpecialAvailable);
            output.println("Datorn väljer " + (computerAction == ActionType.ATTACK ? "ATTACK." : "SPECIAL."));
            computerSpecialAvailable = performAction(computer, player, computerAction, output, "Dator",
                    computerSpecialAvailable);
            printStatus(output);
        }

        String winner = player.isAlive() ? playerName : "Dator";
        return new GameResult(playerName, player.getType(), computer.getType(), winner, rounds);
    }


    private ActionType readPlayerAction(Scanner input, PrintStream output, boolean specialAvailable) {
        while (true) {
            output.println("Välj handling: 1. ATTACK" + (specialAvailable ? "  2. SPECIAL" : "  (SPECIAL använd)"));
            output.print("Val: ");
            if (!input.hasNextLine()) {
                throw new IllegalStateException("Ingen inmatning mottogs. Matchen kan inte fortsätta.");
            }
            String choice = input.nextLine().trim();
            if ("1".equals(choice)) {
                return ActionType.ATTACK;
            }
            if ("2".equals(choice) && specialAvailable) {
                return ActionType.SPECIAL;
            }
            output.println("Ogiltigt val. Välj en handling som fortfarande är tillgänglig.");
        }
    }

    private boolean performAction(Character actor, Character target, ActionType action, PrintStream output,
                                  String actorName, boolean specialAvailable) {
        if (action == ActionType.ATTACK) {
            int damage = actor.attack(target);
            output.printf("%s attackerar och gör %d skada.%n", actorName, damage);
            return specialAvailable;
        }
        output.println(actorName + ": " + actor.useSpecial(target));
        return false;
    }

    private void printStatus(PrintStream output) {
        output.printf("HP – %s: %d/%d | Dator: %d/%d%n",
                playerName, player.getHp(), player.getMaxHP(), computer.getHp(), computer.getMaxHP());
    }
}
