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
        int damage = ThreadLocalRandom.current().nextInt(12, 21); // Random damage between 12 and 20
        int actualDamage = opponent.receiveDamage(damage);
        return "Ranger använder sin specialförmåga!" + " Den gör " + actualDamage + " skada.";
    }
}
