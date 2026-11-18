package org.example.model;

import java.util.concurrent.ThreadLocalRandom;

public final class Ranger extends Character {
    public Ranger() {
        super(105, 6, 16);
    }

    @Override
    public String getType() {
        return "Ranger";
    }

    @Override
    public String useSpecial(Character opponent) {
        return "Ranger använder sin specialförmåga!";
    }
}
