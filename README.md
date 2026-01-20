# ⚔️ Small World with AI Player

**Small World with AI Player** is a simple turn-based tactical strategy game developed in Java. The game pits a human player against an intelligent computer opponent on a procedurally generated grid. The goal is to control territory, manage unit "stacks," and utilize biome advantages to defeat the opponent.

## 🚀 Features

* [cite_start]**4 Unique Races:** Play as **Humans** (Blue), **Elfs** (Green), **Dwarfs** (Yellow), or **Goblins** (Red), each with specific biome preferences[cite: 61, 62, 63, 64, 65].
* [cite_start]**Procedural Map Generation:** The game board is randomized every match with 4 distinct biomes: Plains, Forest, Mountains, and Desert[cite: 57, 58, 59, 60].
* [cite_start]**Smart AI:** The computer opponent uses a custom heuristic algorithm to calculate moves, evaluating factors like "overnumber" attacks (+50 pts) and suicide risks (-100 pts)[cite: 46, 47, 50].
* **Dynamic Combat:**
    * **Stack System:** Units are grouped into stacks. [cite_start]Instead of managing lists of objects, the game uses a memory-optimized `quantity` attribute[cite: 19, 20].
    * [cite_start]**Bounce Back Mechanic:** If an attacker wins a battle but fails to wipe out the defending stack completely, they cannot enter the tile and must return to their original position[cite: 32, 36].

## 🛠️ Technical Highlights

[cite_start]This project demonstrates core Object-Oriented Programming (OOP) principles and distinct Design Patterns[cite: 5, 13]:

* [cite_start]**MVC Architecture:** Strict separation between the game logic (`Model`), the graphical interface (`View`), and user input (`Controller`)[cite: 5, 6].
* **Design Patterns Implemented:**
    * [cite_start]**Strategy Pattern:** Used for the AI (`RandomStrategy` vs `SmartStrategy`), allowing dynamic difficulty changes without altering the player code[cite: 44, 45].
    * [cite_start]**Observer Pattern:** The `Board` (Observable) notifies the `VueControleur` (Observer) immediately when game state changes (movements, deaths)[cite: 8].
    * **Prototype Pattern:** Used for **Unit Division**. [cite_start]When a stack splits to move, the game uses a `copy()` method to clone the unit type without needing to know the concrete class (Human, Elf, etc.)[cite: 24, 25, 27].
* **Multithreading:** The game loop runs on a separate thread, ensuring the UI remains responsive while the AI calculates its moves.
* [cite_start]**Polymorphism:** The abstract `Unit` class defines common behaviors, while concrete classes (`Humans`, `Elfs`) override specific methods like `getFavoredBiomeId()`[cite: 14, 15].

## 🎮 Game Rules

1.  **Objective:** The player with the highest score wins. Points are awarded for holding territory.
2.  **Movement & Merging:**
    * [cite_start]**Division:** You can split a stack to move a single unit while leaving others behind[cite: 24].
    * [cite_start]**Merging:** Moving onto a friendly unit merges them into a single, stronger stack[cite: 28, 29].
3.  **Combat (Dice System):**
    * [cite_start]Attacker rolls **X** dice (where X is unit quantity)[cite: 38, 39].
    * [cite_start]Defender rolls **Y** dice (Y is unit quantity)[cite: 40].
    * [cite_start]**Resolution:** If Attacker Sum > Defender Sum, the attacker wins and kills 1 defender[cite: 33, 41].
    * [cite_start]**Bounce Back:** If the defender survives the loss (quantity > 0), the attacker "bounces back" to their starting tile[cite: 36].

## 📂 Project Structure

```text
src/
├── model/
│   ├── board/       # Logic for Board, Tile, and Biome
│   └── game/        # Logic for Game, Player, Unit (Abstract), and CombatManager
├── vuecontroleur/   # UI logic (Swing JFrame, ImagePanel)
├── Main.java        # Entry point
└── data/            # Game assets (icons and terrain images)
