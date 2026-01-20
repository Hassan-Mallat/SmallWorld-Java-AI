# ⚔️ Small World with AI Player

**Small World with AI Player** is a simple turn-based tactical strategy game developed in Java. The game pits a human player against an intelligent computer opponent on a procedurally generated grid. The goal is to control territory, manage unit stacks, and utilize biome advantages to defeat the opponent.

## 🚀 Features

* **4 Unique Races:** Play as Humans, Elfs, Dwarfs, or Goblins, each with specific biome preferences.
* **Procedural Map Generation:** The game board is randomized every match, ensuring no two games are the same.
* **Smart AI:** The computer opponent uses a custom evaluation function to calculate the best possible moves based on risk, territory value, and combat probability.
* **Combat System:** A dice-based combat mechanic where stack size (quantity of units) and biome advantages influence the outcome.
* **GUI:** Fully functional Graphical User Interface built with **Java Swing**.

## 🛠️ Technical Highlights

This project demonstrates core Object-Oriented Programming (OOP) principles and software design patterns:

* **MVC Architecture:** Strict separation of concerns between the logic (`model`), the interface (`vuecontroleur`), and the game flow.
* **Design Patterns:**
    * **Strategy Pattern:** Used for the AI Logic (`RandomStrategy` vs `SmartStrategy`), allowing for easy difficulty adjustments.
    * **Observer Pattern:** The Board notifies the View (`VueControleur`) whenever the state changes to update the UI dynamically.
* **Multithreading:** The game loop runs on a separate thread from the UI (EDT), allowing the AI to "think" without freezing the interface.
* **Polymorphism:** An abstract `Unit` class allows for easy extension of new races (`Humans`, `Elfs`, etc.) without rewriting core logic.

## 🎮 Game Rules

1.  **Objective:** The player with the highest score after 6 turns wins. Points are awarded for every tile occupied at the end of a turn.
2.  **Movement:** Units can move to adjacent tiles.
    * Moving to an empty tile occupies it.
    * Moving to a friendly tile merges the stacks (increasing power).
    * Moving to an enemy tile initiates **Combat**.
3.  **Combat:**
    * Attacker rolls X dice (where X is unit quantity).
    * Defender rolls Y dice (Y is unit quantity + 1 if on favored biome).
    * Highest sum wins. If the attacker wins, they take the tile.
4.  **Biome Advantages:**
    * **Humans:** Plains
    * **Elfs:** Forest
    * **Dwarfs:** Mountains
    * **Goblins:** Desert

## 📂 Project Structure

```text
src/
├── model/
│   ├── board/       # Logic for Grid, Tiles, and Biomes
│   └── game/        # Logic for Units, Players, AI, and Combat
├── vuecontroleur/   # UI logic (Swing JFrame) and Image handling
├── Main.java        # Entry point
└── data/            # Game assets (icons and terrain images)
