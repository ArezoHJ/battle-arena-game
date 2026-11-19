package org.example.model;

public final class Mage extends Character {
    private static final int HEAL_AMOUNT = 15;

    public Mage() {
        super(90, 8, 18);
    }

    @Override
    public String getType() {
        return "Mage";
    }

    @Override
    public String useSpecial(Character opponent) {
        int healed = heal(HEAL_AMOUNT);
        return "Mage läker " + healed + " HP.";
    }
}
