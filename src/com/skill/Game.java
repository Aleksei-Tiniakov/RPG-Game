package com.skill;

import java.util.Scanner;

public class Game {
    static Scanner in = new Scanner(System.in);

    static Player player;
    public static boolean isRunning;

    public static String[] encounters = {"Battle", "Battle", "Battle", "Rest", "Rest"};
    public static String[] enemies = {"Zombie", "Skeleton"};

    public static int readInt(String str, int userChoices) {
        int input;

        do {
            System.out.println(str);
            try {
                input = Integer.parseInt(in.next());
            } catch (Exception e) {
                input = -1;
                System.out.println("Please enter an integer");
            }
        } while (input < 1 || input > userChoices);
        return input;
    }

    //очищение консоли
    public static void clearConsole() {
        for (int i = 0; i < 2; i++)
            System.out.println();
    }

    //разделитель
    public static void printSeparator(int n) {
        for (int i = 0; i < n; i++)
            System.out.print("-");
        System.out.println();
    }

    public static void printHeading(String title) {
        printSeparator(30);
        System.out.println(title);
        printSeparator(30);
    }

    public static void pressToContinue() {
        System.out.println("\nEnter anything to continue");
        in.next();
    }

    public static void startGame() {
        boolean nameSet = false;
        String name;
        clearConsole();
        printSeparator(40);
        printSeparator(30);
        System.out.println("Some story");
        System.out.println("and more some story");
        printSeparator(30);
        printSeparator(40);
        pressToContinue();

        do {
            clearConsole();
            printHeading("Enter your name");
            name = in.next();
            clearConsole();
            printHeading("You name is " + name + "\nIs that correct?");
            System.out.println("(1) Yes!");
            System.out.println("(2) No, I want to rename.");
            int input = readInt("->", 2);
            if (input == 1)
                nameSet = true;
        } while (!nameSet);

        player = new Player(name);

        isRunning = true;
        gameLoop();
    }

    public static void checkLocation() {

        clearConsole();
        printHeading("Where you want to go? ");
        System.out.println("""
                (1) Town
                (2) Dungeon
                (3) Forest
                (4) Exit""");
        int input = readInt("->", 4);
        clearConsole();
        if (input == 1) {
            printSeparator(40);
            System.out.println("You arrived in at the Town");
            System.out.println("Do you want to go to Shop or take a rest?");
            printSeparator(40);
            System.out.println("""                    
                    (1) Continue journey
                    (2) Take a rest
                    (3) Go to the shop
                    (4) Character info""");
            input = readInt("->", 4);
                if (input == 1)
                continueJourney();
                if (input == 2)
                    takeRest();
                if (input == 3)
                    shop();
                if (input == 4)
                    characterInfo();

        } else if (input == 2) {
            System.out.println("You arrived in at the Dungeon");
            randomEncounter();
            dragon();
            continueJourney();
        } else if (input == 3) {
            System.out.println("You arrived in at the Forest");
            randomEncounter();
            continueJourney();
        } else if (input == 4)
            isRunning = false;
    }

    // возможность случайной свтречи
    public static void randomEncounter() {
        int encounter = (int) (Math.random() * encounters.length);
        if (encounters[encounter].equals("Battle")) {
            randomBattle();
        } else if (encounters[encounter].equals("Rest")) {
            takeRest();
        } else {
            shop();
        }
    }

    public static void continueJourney() {
        checkLocation();
    }

    public static void characterInfo() {
        clearConsole();
        printHeading("Character info");
        System.out.println(player.name + "\tHP: " + player.hp + "/" + player.maxHp);
        printSeparator(20);
        System.out.println("XP: " + player.xp + "\tGold: " + player.gold);
        printSeparator(20);
        System.out.println("# of Potions: " + player.pots);
        printSeparator(20);

        if (player.numAtkUpgrades > 0) {
            System.out.println("Offensive trait: " + player.numAtkUpgrades);
            printSeparator(20);
        }
        if (player.numDefUpgrades > 0) {
            System.out.println("Defensive trait:" + player.numDefUpgrades);
        }
        pressToContinue();
    }

    //магаз
    public static void shop() {
        clearConsole();
        printHeading("You meet a merchant");
        int price = (int) (Math.random() * (10 + player.pots * 3) + 5 + player.pots);
        System.out.println("Potion: " + price + " gold");
        printSeparator(20);
        System.out.println("Do you want to buy one?\n(1) Yes\n(2) No");
        int input = readInt("->", 2);
        if (input == 1) {
            clearConsole();
            if (player.gold >= price) {
                printHeading("You bought a potion for price: " + price + " gold");
                player.pots++;
                player.gold -= price;
            } else {
                printHeading("not enough gold to buy this");
                pressToContinue();
            }
        }
    }

    public static void takeRest() {
        clearConsole();
        if (player.restsLeft >= 1) {
            printHeading("Do you want to take a rest? " + player.restsLeft);
            System.out.println("(1) Yes\n(2) No");
            int input = readInt("->", 2);
            if (input == 1) {
                clearConsole();
                if (player.hp < player.maxHp) {
                    int hpRestored = (int) (Math.random() * (player.xp / 4 + 1) + 25);
                    player.hp += hpRestored;
                    if (player.hp > player.maxHp)
                        player.hp = player.maxHp;
                    System.out.println("You took rest and restored up to " + hpRestored + " health.");
                    System.out.println("You are now at " + player.hp + "/" + player.maxHp + " health.");
                    player.restsLeft--;
                } else
                    System.out.println("You are at full health.");
                pressToContinue();
            }
        }
    }

    public static void randomBattle() {
        clearConsole();
        printHeading("You encountered monster ");
        pressToContinue();
        battle(new Enemy(enemies[(int) (Math.random() * enemies.length)], player.xp));
    }

    public static void dragon() {
        battle(new Enemy("Dragon", 300));
    }

    public static void battle(Enemy enemy) {

        while (true) {
            clearConsole();
            printHeading(enemy.name + "\nHP: " + enemy.hp + "/" + enemy.maxHp);
            printHeading(player.name + "\nHP: " + player.hp + "/" + player.maxHp);
            printSeparator(20);
            System.out.println("Choice an action: ");
            printSeparator(20);
            System.out.println("(1) Fight\n(2) Use Potion\n(3) Run Away");
            int input = readInt("->", 3);

            if (input == 1) {
                int dmg = player.attack() - enemy.defend();
                int dmgTook = enemy.attack() - player.defend();

                if (dmgTook < 0) {
                    dmg -= dmgTook /2;
                    dmgTook = 0;
                }
                if (dmg < 0)
                    dmg = 0;
                player.hp -= dmgTook;
                enemy.hp -= dmg;
                clearConsole();
                printHeading("Battle");
                System.out.println("You dealt " + dmg + " damage to the " + enemy.name + ".");
                printSeparator(20);
                System.out.println("The " + enemy.name + " dealt " + dmgTook + " damage to you.");
                pressToContinue();

                //проверка на смерть
                if (player.hp <= 0) {
                    playerDied();
                    break;
                } else if (enemy.hp <= 0) {
                    clearConsole();
                    printHeading("You defeated the " + enemy.name + "!");
                    player.xp += enemy.xp;
                    System.out.println("You earned " + enemy.xp + "XP!");
                    pressToContinue();
                    break;
                }

                //дроп и отдых
                boolean addRest = (Math.random() * 5 + 1 <= 1.25);
                int goldEarned = (int) (Math.random() * enemy.xp);
                if (addRest) {
                    player.restsLeft++;
                    System.out.println("You earned the chance to get an additional rest");
                }
                if (goldEarned > 0) {
                    player.gold += goldEarned;
                    System.out.println("You collect " + goldEarned + " gold from the " + enemy.name);
                    pressToContinue();
                    break;
                }
                //поушены
            } else if (input == 2) {
                clearConsole();
                if (player.pots > 0 && player.hp < player.maxHp)
                    printHeading("Do you want to drink a potion? (" + player.pots + "left).");
                System.out.println("(1) Yes\n(2) No");
                input = readInt("->", 2);
                if (input == 1)
                    player.hp = player.maxHp;
                clearConsole();
                printHeading("You restored your health back to " + player.maxHp);
                pressToContinue();

                //  попытка выйти из битвы 35%
            } else {
                clearConsole();
                if (Math.random() * 10 + 1 <= 3.5) {
                    printHeading("You ran away from the " + enemy.name);
                    pressToContinue();
                    continueJourney();
                    break;
                } else {
                    printHeading("You didn't manage to escape.");
                    int dmgTook = enemy.attack();
                    System.out.println("You took damage: " + dmgTook);
                    pressToContinue();
                    if (player.hp <= 0)
                        playerDied();
                }
            }
        }
    }

    public static void printMenu () {
        clearConsole();
        System.out.println("Choice an actions");
        printSeparator(30);
        System.out.println("(1) Continue you journey");
        System.out.println("(2) Character info");
        System.out.println("(3) Exit");
    }

    public static void playerDied () {
        clearConsole();
        printHeading("You died....");
        printHeading("You earned " + player.xp + " XP on your journey.");
        isRunning = false;
    }

    public static void gameLoop () {
        while (isRunning) {
            printMenu();
            int input = readInt("->", 3);
            if (input == 1) {
                continueJourney();
            } else if (input == 2) {
                characterInfo();
            } else {
                isRunning = false;
            }
        }
    }
}

