# ⚔️ Small World with AI Player

**Small World with AI Player** is a simple turn-based tactical strategy game developed in Java. The game pits a human player against an intelligent computer opponent on a procedurally generated grid. The goal is to control territory, manage unit "stacks," and utilize biome advantages to defeat the opponent.

## 🚀 Features

* **4 Unique Races:** Play as **Humans** (Blue), **Elfs** (Green), **Dwarfs** (Yellow), or **Goblins** (Red), each with specific biome preferences.
* **Procedural Map Generation:** The game board is randomized every match with 4 distinct biomes: Plains, Forest, Mountains, and Desert.
* **Smart AI:** The computer opponent uses a custom heuristic algorithm to calculate moves, evaluating factors like "overnumber" attacks (+50 pts) and suicide risks (-100 pts).
* **Dynamic Combat:**
    * **Stack System:** Units are grouped into stacks. Instead of managing lists of objects, the game uses a memory-optimized `quantity` attribute.
    * **Bounce Back Mechanic:** If an attacker wins a battle but fails to wipe out the defending stack completely, they cannot enter the tile and must return to their original position.

## 🛠️ Technical Highlights

This project demonstrates core Object-Oriented Programming (OOP) principles and distinct Design Patterns:

* **MVC Architecture:** Strict separation between the game logic (`Model`), the graphical interface (`View`), and user input (`Controller`).
* **Design Patterns Implemented:**
    * **Strategy Pattern:** Used for the AI (`RandomStrategy` vs `SmartStrategy`), allowing dynamic difficulty changes without altering the player code.
    * **Observer Pattern:** The `Board` (Observable) notifies the `VueControleur` (Observer) immediately when game state changes (movements, deaths).
    * **Prototype Pattern:** Used for **Unit Division**. When a stack splits to move, the game uses a `copy()` method to clone the unit type without needing to know the concrete class (Human, Elf, etc.).
* **Multithreading:** The game loop runs on a separate thread, ensuring the UI remains responsive while the AI calculates its moves.
* **Polymorphism:** The abstract `Unit` class defines common behaviors, while concrete classes (`Humans`, `Elfs`) override specific methods like `getFavoredBiomeId()`.

## 🎮 Game Rules

1.  **Objective:** The player with the highest score wins. Points are awarded for holding territory.
2.  **Movement & Merging:**
    * **Division:** You can split a stack to move a single unit while leaving others behind.
    * **Merging:** Moving onto a friendly unit merges them into a single, stronger stack.
3.  **Combat (Dice System):**
    * Attacker rolls **X** dice (where X is unit quantity).
    * Defender rolls **Y** dice (Y is unit quantity).
    * **Resolution:** If Attacker Sum > Defender Sum, the attacker wins and kills 1 defender.
    * **Bounce Back:** If the defender survives the loss (quantity > 0), the attacker "bounces back" to their starting tile.

## 📂 Project Structure

```text
GameCode/
├── src/
│   ├── model/
│   │   ├── board/       # Logic for Board, Tile, and Biome
│   │   └── game/        # Logic for Game, Player, Unit (Abstract), and CombatManager
│   ├── vuecontroleur/   # UI logic (Swing JFrame, ImagePanel)
│   └── Main.java        # Entry point
└── data/                # Game assets (icons and terrain images)
