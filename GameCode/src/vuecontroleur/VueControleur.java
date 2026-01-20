package vuecontroleur;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import model.board.Board;
import model.board.Tile;
import model.game.Play;
import model.game.Humans;
import model.game.Elfs;
import model.game.Dwarfs;
import model.game.Goblins;
import model.game.Game;
import model.game.Unit;


/** This class has two responsibilities:
 *  (1) View: provide a graphical representation of the application (tile graphics, etc.)
 *  (2) Controller: listen to keyboard/mouse events and trigger appropriate model actions (click start tile -> target tile)
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Board board; // Reference to the model: allows access to model data for refresh and to send keyboard/mouse actions
    private Game game;
    private final int sizeX; // size of the displayed grid
    private final int sizeY;
    private static final int pxCase = 100; // number of pixels per tile
    // icons displayed in the grid
    private Image humanIcon;
    private Image elfIcon;
    private Image dwarfIcon;
    private Image goblinIcon;
    private Image plainsIcon;
    private Image forestIcon;
    private Image mountainIcon;
    private Image desertIcon;
    private Image movePreviewIcon;

    private JComponent IPGrid;
    private Tile clickTile1; // store clicked tiles
    private Tile clickTile2;


    private ImagePanel[][] tabIP; // graphical tiles (during refresh each tile gets a background and front icon depending on the model)

    // Stats panel components (counts by race)
    private JPanel statsPanel;
    private JLabel lblHumansCount;
    private JLabel lblElfsCount;
    private JLabel lblDwarfsCount;
    private JLabel lblGoblinsCount;

    // VueController constructor
    public VueControleur(Game _game) {
        game = _game;
        board = game.getBoard();
        sizeX = board.SIZE_X;
        sizeY = board.SIZE_Y;

        loadIcon();
        placeGraphicComponents();

        board.addObserver(this);

        updateDisplay();
    }

    // Load icons used for game display
    private void loadIcon() {
        humanIcon = new ImageIcon("./data/units/unit_blue.png").getImage();
    	elfIcon = new ImageIcon("./data/units/unit_green.png").getImage();
    	dwarfIcon = new ImageIcon("./data/units/unit_yellow.png").getImage();
    	goblinIcon = new ImageIcon("./data/units/unit_red.png").getImage();
        plainsIcon = new ImageIcon("./data/terrain/plain.png").getImage();
        forestIcon = new ImageIcon("./data/terrain/forest.png").getImage();
        mountainIcon = new ImageIcon("./data/terrain/mountain.png").getImage();
        desertIcon = new ImageIcon("./data/terrain/desert.png").getImage();
        movePreviewIcon = new ImageIcon("./data/units/unit_none.png").getImage();
    }


    // Create the game window
    private void placeGraphicComponents() {
        setTitle("Smallworld");
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes the application when the window is closed

        IPGrid = new JPanel(new GridLayout(sizeY, sizeX)); // IPGrid will contain the graphical tiles and layout them as a grid
        // give the grid a preferred size so pack() accounts for it (lets room for the stats panel)
        IPGrid.setPreferredSize(new Dimension(sizeX * pxCase, sizeY * pxCase));

        tabIP = new ImagePanel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                ImagePanel iP = new ImagePanel();

                tabIP[x][y] = iP; // we keep the graphical tiles in tabIP to have convenient access (see updateDisplay())

                final int xx = x; // allows the anonymous class below to access these variables
                final int yy = y;
                // click listener
                iP.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if (clickTile1 == null) {
                            clickTile1 = board.getTile()[xx][yy];
                            // show preview immediately
                            updateDisplay();
                        } else {
                            clickTile2 = board.getTile()[xx][yy];
                            game.sendPlay(new Play(clickTile1, clickTile2));
                            clickTile1 = null;
                            clickTile2 = null;
                        }
                    }
                });
                IPGrid.add(iP);
            }
        }

        // Add grid and stats panel to the frame
        // content pane default layout is BorderLayout
        add(IPGrid, BorderLayout.CENTER);

        // Stats panel initialisation
        statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Font statsFont = new Font("SansSerif", Font.BOLD, 14);
        lblHumansCount = new JLabel("Humans: 0");
        lblHumansCount.setFont(statsFont);
        lblElfsCount = new JLabel("Elfs: 0");
        lblElfsCount.setFont(statsFont);
        lblDwarfsCount = new JLabel("Dwarfs: 0");
        lblDwarfsCount.setFont(statsFont);
        lblGoblinsCount = new JLabel("Goblins: 0");
        lblGoblinsCount.setFont(statsFont);

        statsPanel.add(lblHumansCount);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(lblElfsCount);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(lblDwarfsCount);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(lblGoblinsCount);

        add(statsPanel, BorderLayout.SOUTH);

        // Let Swing compute window size including the stats panel
        pack();
        setLocationRelativeTo(null); // center on screen
    }
    
    /**
     * There is a grid on the model side (game.getGrid()) and a grid in the view (`tabIP`)
     */
    
    // Update game display
    private void updateDisplay() {
    	System.out.println("Updating display.");

        Point[] movePreview = null;

        // Counters for each race (sum of quantities)
        int countHumans = 0;
        int countElfs = 0;
        int countDwarfs = 0;
        int countGoblins = 0;

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
            	Tile tile = board.getTile()[x][y];
            	if (tile.getBiome().getBiomeType() == 1) {
            		tabIP[x][y].setBackground(plainsIcon);
            	} else if (tile.getBiome().getBiomeType() == 2) {
            		tabIP[x][y].setBackground(forestIcon);
            	} else if (tile.getBiome().getBiomeType() == 3) {
            		tabIP[x][y].setBackground(mountainIcon);
            	} else {
            		tabIP[x][y].setBackground(desertIcon);
            	}
                tabIP[x][y].setFront(null);
                if (tile != null) {
                    Unit unit = tile.getUnits();

                    if (unit instanceof Humans) {
                        tabIP[x][y].setFront(humanIcon);
                        countHumans += unit.getQuantity();
                    } else if (unit instanceof Elfs) {
                    	tabIP[x][y].setFront(elfIcon);
                    	countElfs += unit.getQuantity();
                    } else if (unit instanceof Dwarfs) {
                    	tabIP[x][y].setFront(dwarfIcon);
                    	countDwarfs += unit.getQuantity();
                    } else if (unit instanceof Goblins) {
                    	tabIP[x][y].setFront(goblinIcon);
                    	countGoblins += unit.getQuantity();
                    }
                    // only compute preview when the clicked tile matches and there is a unit
                    if (clickTile1 == tile) {
                        if (tile.getUnits() != null) {
                        	int range = tile.getUnits().getMovementRange();
                        	System.out.println("Computing preview for (" + x + "," + y + ") with range " + range);
                        	Point start = new Point(x, y);
                        	movePreview = board.finishTileListing(start, range);
                        } else {
                            System.out.println("Selected tile has no unit -> no preview");
                        }
                    }
                }
            }
        }

        // Update stats labels
        lblHumansCount.setText("Humans: " + countHumans);
        lblElfsCount.setText("Elfs: " + countElfs);
        lblDwarfsCount.setText("Dwarfs: " + countDwarfs);
        lblGoblinsCount.setText("Goblins: " + countGoblins);

        if (movePreview != null) {
        	System.out.println("Preview array length: " + movePreview.length);
        	for (int i = 0; i < movePreview.length; i++) {
        		Point pt = movePreview[i];
        		if (pt == null) {
        			break;
        		}
       			tabIP[pt.x][pt.y].setFront(movePreviewIcon);
        	}
        }

        IPGrid.repaint();
        statsPanel.revalidate();
        statsPanel.repaint();
    }

    // Game update
    @Override
    public void update(Observable obs, Object arg) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateDisplay();
            }
        }); 
    }
}
