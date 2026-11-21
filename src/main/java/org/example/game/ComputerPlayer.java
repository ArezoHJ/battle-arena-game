package org.example.game;

import org.example.model.ActionType;
import org.example.model.Character;
import org.example.model.Mage;
import org.example.model.Ranger;
import org.example.model.Warrior;

import java.util.Random;

public final class ComputerPlayer {
    private final Random random;

    public ComputerPlayer() {
        this(new Random());
    }

    public ComputerPlayer(Random random) {
        this.random = random;
    }

public Character chooseCharacter() {
        return switch (random.nextInt(3)) {
            case 0 -> new Warrior();
            case 1 -> new Mage();
            default -> new Ranger();
        };
    }

public ActionType chooseAction(boolean specialAvailable) {
        if (!specialAvailable || random.nextBoolean()) {
            return ActionType.ATTACK;
        }
        return ActionType.SPECIAL;
    }
}
