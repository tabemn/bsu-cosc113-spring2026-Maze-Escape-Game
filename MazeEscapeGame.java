// Name: Nia Silke Tabe

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MazeEscapeGame extends JPanel implements KeyListener {

    // # = wall, E = exit, T = trap, C = coin, K = key, D = locked door
    private char[][] maze = {
        {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
        {'#','.','.','C','.','#','.','.','T','.','.','#','.','.','.','E','.','#'},
        {'#','.','#','#','.','#','.','#','#','#','.','#','.','#','#','#','.','#'},
        {'#','.','#','T','.','.','.','#','C','.','.','.','.','#','T','.','.','#'},
        {'#','.','#','.','#','#','#','#','.','#','#','#','.','#','.','#','#','#'},
        {'#','K','.','.','#','C','.','.','.','#','.','.','.','#','.','.','C','#'},
        {'#','#','#','.','#','.','#','#','#','#','.','#','#','#','#','#','.','#'},
        {'#','.','.','.','.','.','#','T','.','.','.','#','.','.','.','.','.','#'},
        {'#','.','#','#','#','.','#','.','#','#','.','#','.','#','#','#','.','#'},
        {'#','.','.','T','#','.','.','.','#','C','.','.','.','#','T','.','.','#'},
        {'#','#','#','.','#','#','#','.','#','#','#','#','.','#','#','#','.','#'},
        {'#','C','.','.','.','.','#','.','.','.','T','#','.','.','.','#','.','#'},
        {'#','.','#','#','#','.','#','#','#','.','#','#','#','#','.','#','.','#'},
        {'#','.','.','.','#','.','.','C','#','.','.','.','.','.','.','D','.','#'},
        {'#','#','#','.','#','#','#','.','#','#','#','#','#','#','#','#','.','#'},
        {'#','.','.','.','.','T','.','.','.','.','C','.','.','T','.','.','.','#'},
        {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
    };

    private int playerRow = 1;
    private int playerCol = 1;

    // Four Pac-Man enemies
    private int[] enemyRows = {15, 15, 1, 7};
    private int[] enemyCols = {1, 16, 16, 8};

    private int moves = 90;
    private int health = 5;
    private int score = 0;

    private boolean hasKey = false;
    private boolean gameOver = false;

    private String message = "Escape the maze while 4 Pac-Men chase you!";

    public MazeEscapeGame() {
        setFocusable(true);
        addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int tileSize = 35;

        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {

                if (maze[row][col] == '#') {
                    g.setColor(Color.DARK_GRAY);
                } else if (maze[row][col] == 'E') {
                    g.setColor(Color.GREEN);
                } else if (maze[row][col] == 'T') {
                    g.setColor(Color.RED);
                } else if (maze[row][col] == 'C') {
                    g.setColor(Color.YELLOW);
                } else if (maze[row][col] == 'K') {
                    g.setColor(Color.ORANGE);
                } else if (maze[row][col] == 'D') {
                    g.setColor(Color.MAGENTA);
                } else {
                    g.setColor(Color.WHITE);
                }

                g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);

                g.setColor(Color.BLACK);
                g.drawRect(col * tileSize, row * tileSize, tileSize, tileSize);

                g.setFont(new Font("Arial", Font.BOLD, 14));

                if (maze[row][col] == 'C') {
                    g.drawString("$", col * tileSize + 12, row * tileSize + 22);
                }

                if (maze[row][col] == 'K') {
                    g.drawString("K", col * tileSize + 12, row * tileSize + 22);
                }

                if (maze[row][col] == 'D') {
                    g.drawString("D", col * tileSize + 12, row * tileSize + 22);
                }

                if (maze[row][col] == 'T') {
                    g.drawString("!", col * tileSize + 14, row * tileSize + 22);
                }
            }
        }

        // Draw player
        g.setColor(Color.BLUE);
        g.fillOval(playerCol * tileSize + 6, playerRow * tileSize + 6, 24, 24);

        // Draw four Pac-Man enemies
        g.setColor(Color.YELLOW);
        for (int i = 0; i < enemyRows.length; i++) {
            g.fillArc(enemyCols[i] * tileSize + 4,
                      enemyRows[i] * tileSize + 4,
                      27,
                      27,
                      30,
                      300);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 15));

        g.drawString("Moves: " + moves, 20, 625);
        g.drawString("Health: " + health, 120, 625);
        g.drawString("Score: " + score, 220, 625);
        g.drawString("Key: " + (hasKey ? "Yes" : "No"), 320, 625);

        g.drawString(message, 20, 655);
    }

    public void movePlayer(int rowChange, int colChange) {
        if (gameOver) {
            return;
        }

        int newRow = playerRow + rowChange;
        int newCol = playerCol + colChange;

        char nextSpot = maze[newRow][newCol];

        if (nextSpot == '#') {
            message = "A wall blocked you!";
            repaint();
            return;
        }

        if (nextSpot == 'D' && !hasKey) {
            message = "The door is locked. Find the key first!";
            repaint();
            return;
        }

        if (nextSpot == 'T') {
            health--;
            message = "Trap! You lost 1 health.";
        } else if (nextSpot == 'C') {
            score += 10;
            message = "Coin collected!";
            maze[newRow][newCol] = '.';
        } else if (nextSpot == 'K') {
            hasKey = true;
            score += 30;
            message = "You found the key!";
            maze[newRow][newCol] = '.';
        } else if (nextSpot == 'D') {
            message = "You unlocked the door!";
            maze[newRow][newCol] = '.';
        } else {
            message = "Keep moving. They are chasing you!";
        }

        playerRow = newRow;
        playerCol = newCol;
        moves--;

        if (nextSpot == 'E') {
            score += moves + (health * 25);
            message = "You escaped! Final Score: " + score;
            gameOver = true;
            repaint();
            return;
        }

        moveEnemies();

        if (enemyCaughtPlayer()) {
            message = "A Pac-Man caught you. Game over!";
            gameOver = true;
        }

        if (health <= 0) {
            message = "You lost all your health. Game over!";
            gameOver = true;
        }

        if (moves <= 0) {
            message = "You ran out of moves. Game over!";
            gameOver = true;
        }

        repaint();
    }

    public void moveEnemies() {
        for (int i = 0; i < enemyRows.length; i++) {
            int bestRow = enemyRows[i];
            int bestCol = enemyCols[i];

            int currentDistance = distance(enemyRows[i], enemyCols[i], playerRow, playerCol);

            int[][] directions = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
            };

            for (int j = 0; j < directions.length; j++) {
                int newEnemyRow = enemyRows[i] + directions[j][0];
                int newEnemyCol = enemyCols[i] + directions[j][1];

                if (maze[newEnemyRow][newEnemyCol] != '#') {
                    int newDistance = distance(newEnemyRow, newEnemyCol, playerRow, playerCol);

                    if (newDistance < currentDistance) {
                        currentDistance = newDistance;
                        bestRow = newEnemyRow;
                        bestCol = newEnemyCol;
                    }
                }
            }

            enemyRows[i] = bestRow;
            enemyCols[i] = bestCol;
        }
    }

    public boolean enemyCaughtPlayer() {
        for (int i = 0; i < enemyRows.length; i++) {
            if (enemyRows[i] == playerRow && enemyCols[i] == playerCol) {
                return true;
            }
        }
        return false;
    }

    public int distance(int r1, int c1, int r2, int c2) {
        return Math.abs(r1 - r2) + Math.abs(c1 - c2);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            movePlayer(-1, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            movePlayer(1, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            movePlayer(0, -1);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            movePlayer(0, 1);
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Escape Game: 4 Pac-Man Chase Mode");
        MazeEscapeGame game = new MazeEscapeGame();

        frame.add(game);
        frame.setSize(660, 710);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}