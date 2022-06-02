package com.skill;

public class Player extends Character{

    public int numAtkUpgrades, numDefUpgrades;

    public int gold, restsLeft, pots;


    public Player(String name) {
        super(name,100,0);

        this.numAtkUpgrades = 3;
        this.numDefUpgrades = 0;
        this.gold = 15;
        this.restsLeft = 1;
        this.pots = 2;
        this.hp = maxHp;

    }

    @Override
    public int attack() {
        return (int) (Math.random() * (xp/4 + numAtkUpgrades * 3 + 3) + xp/10 + numAtkUpgrades * 2 + numDefUpgrades + 1);
    }

    @Override
    public int defend() {
        return (int) (Math.random() + (xp/3 + numDefUpgrades * 3 + 3) + xp/10 + numDefUpgrades * 2 + numAtkUpgrades + 1);
    }
}

