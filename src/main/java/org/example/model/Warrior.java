package org.example.model;

public final class Warrior extends Character {

    public Warrior() {
        super(120, 5, 15);
    }

    @Override
    public String getType() {
        return "Warrior";
    }

    @Override
    public String useSpecial(Character opponent) {
        prepareDefend();
        return "Warrior går i försvar och halverar nästa inkommande attack.";
    }
}
