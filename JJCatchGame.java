import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;


public class JJCatchGame extends JFrame {

    private Clip backgroundMusic;
    private Clip starCaughtSound;
    private Clip gameOverSound;
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JJCatchGame().setVisible(true));
    }

    public JJCatchGame() {
        initUI();
        loadBackgroundMusic();
        loadStarCaughtSound();
        loadGameOverSound();
    }

    private void initUI() {
        setTitle("JJ CATCH!!!");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new CardLayout());

       
        try {
            setIconImage(ImageIO.read(new File("jj.png"))); 
        } catch (IOException e) {
            e.printStackTrace();
        }

        GamePanel gamePanel = new GamePanel(this);
        add(gamePanel, "GamePanel");

        MenuPanel menuPanel = new MenuPanel(this);
        add(menuPanel, "MenuPanel");

        IntroPanel introPanel = new IntroPanel(this, menuPanel);
        add(introPanel, "IntroPanel");

        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "IntroPanel");
    }

    private void loadBackgroundMusic() {
        try {
            File audioFile = new File("TAKE ON ME 8-BIT.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playBackgroundMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            backgroundMusic.start();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
    
    public void playSound(String soundFile) {
        try {
            File audioFile = new File(soundFile);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
  }

   private void loadStarCaughtSound() {
    try {
        File audioFile = new File("starCaught.wav"); // Replace with your actual audio file path
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile);
        starCaughtSound = AudioSystem.getClip();
        starCaughtSound.open(audioIn);
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        e.printStackTrace();
    }
}

   public void playStarCaughtSound() {
    if (starCaughtSound != null) {
        starCaughtSound.stop();
        starCaughtSound.setFramePosition(0);
        starCaughtSound.start();
    }
}

   private void loadGameOverSound() {
        try {
            File audioFile = new File("gameOver.wav"); // Adjust path as necessary
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile);
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playGameOverSound() {
        if (gameOverSound != null) {
            gameOverSound.stop();
            gameOverSound.setFramePosition(0);
            gameOverSound.start();
        }
    }

class IntroPanel extends JPanel {
    private final JJCatchGame parent;
    private final MenuPanel menuPanel;
    private BufferedImage welcomeImage;
    private BufferedImage backgroundImage;
    private JLayeredPane layeredPane;
    private JLabel startLabel;
    private Image gifImage;

    public IntroPanel(JJCatchGame parent, MenuPanel menuPanel) {
        this.parent = parent;
        this.menuPanel = menuPanel;
        initUI();
        loadImages();
        setupLayeredPane();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        startLabel = new JLabel();
        customizeLabel(startLabel, "start.png");
        startLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) parent.getContentPane().getLayout();
                cl.show(parent.getContentPane(), "MenuPanel");
                parent.playSound("button_click.wav");
            }
        });
    }

    private void loadImages() {
        try { 
            welcomeImage = ImageIO.read(new File("welcome.png"));
            ImageIcon gifBackground = new ImageIcon("background.gif"); // Replace with your GIF path
            gifImage = gifBackground.getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void customizeLabel(JLabel label, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH); 
        label.setIcon(new ImageIcon(image));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
    }

    private void setupLayeredPane() {
        layeredPane.setPreferredSize(new Dimension(800, 600));

        
       JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (gifImage != null) {
                    g.drawImage(gifImage, 0, 0, getWidth(), getHeight(), this);
                }
                if (welcomeImage != null) {
                    int x = getWidth() / 2 - welcomeImage.getWidth() / 2;
                    int y = getHeight() / 2 - welcomeImage.getHeight() / 2;
                    g.drawImage(welcomeImage, x, y, this);
                }
            }
        };
        
        backgroundPanel.setBounds(0, 0, 800, 600);
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        
        startLabel.setBounds(300, 360, 200, 100); 
        layeredPane.add(startLabel, JLayeredPane.PALETTE_LAYER);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600); 
    }
}

class MenuPanel extends JPanel {
    private final JJCatchGame parent;
    private ImageIcon gifBackground;
    private Image gifImage;
    private JLabel player1Label;
    private JLabel player2Label;

    public MenuPanel(JJCatchGame parent) {
        this.parent = parent;
        initUI();
        loadImages();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLayeredPane layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        JPanel selectionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (gifImage != null) {
                    g.drawImage(gifImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        selectionPanel.setBounds(0, 0, 800, 600);
        layeredPane.add(selectionPanel, JLayeredPane.DEFAULT_LAYER);

        JLabel chooseCharacterLabel = new JLabel();
        customizeLabel(chooseCharacterLabel, "chooseCharacterLabel.png");
        chooseCharacterLabel.setBounds(10, 20, 800, 200); 

        player1Label = new JLabel();
        player2Label = new JLabel();

        customizeImageLabel(player1Label, "JannarvasaV2.png", 300, 300); 
        customizeImageLabel(player2Label, "JayasavranV2.png", 300, 300); 

        player1Label.setBounds(80, 180, 300, 300); 
        player2Label.setBounds(420, 180, 300, 300); 

        player1Label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                parent.playSound("button_click.wav");  // Play sound effect on click
                startGame("Jannarvasa.png");
            }
        });

        player2Label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                parent.playSound("button_click.wav");  // Play sound effect on click
                startGame("Jayasavran.png");
            }
        });

        layeredPane.add(chooseCharacterLabel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(player1Label, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(player2Label, JLayeredPane.PALETTE_LAYER);
    }

      private void loadImages() {
        gifBackground = new ImageIcon("background.gif"); // Load GIF background
        gifImage = gifBackground.getImage();
    }

    private void customizeImageLabel(JLabel label, String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(image));
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
    }

    private void customizeLabel(JLabel label, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage().getScaledInstance(800, 200, Image.SCALE_SMOOTH); 
        label.setIcon(new ImageIcon(image));
        label.setHorizontalAlignment(JLabel.CENTER);
    }

    private void startGame(String playerImagePath) {
        GamePanel gamePanel = new GamePanel(parent);
        gamePanel.setPlayerImagePath(playerImagePath);
        parent.getContentPane().add(gamePanel, "GamePanel");
        parent.playBackgroundMusic(); 

        CardLayout cl = (CardLayout) parent.getContentPane().getLayout();
        cl.show(parent.getContentPane(), "GamePanel");

        gamePanel.requestFocusInWindow();
        gamePanel.startGame();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gifImage != null) {
            g.drawImage(gifImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

class GamePanel extends JPanel implements ActionListener {

    private final Timer timer;
    private final JJCatchGame parent;
    private final ArrayList<Star> stars;
    private final ArrayList<Rock> rocks;
    private Basket basket;
    private int score;
    private int lives;
    private String playerImagePath;
    private boolean gameOver;
    private static final int INITIAL_DELAY = 50;
    private BufferedImage backgroundImage;
    private BufferedImage lifeImage;
    private BufferedImage starImage;
    private BufferedImage rockImage;
    private JLabel restartLabel;
    private JLabel endLabel;

    private ImageIcon gifBackground;
    private Image gifImage;

    public GamePanel(JJCatchGame parent) {
        this.parent = parent;
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(800, 600));

        try {
            gifBackground = new ImageIcon("background.gif"); // Replace with your GIF path
            gifImage = gifBackground.getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        timer = new Timer(INITIAL_DELAY, this);
        stars = new ArrayList<>();
        rocks = new ArrayList<>();
        addKeyListener(new TAdapter());
        loadImages();
        initUI();
        startGame();
    }

    private void loadImages() {
        try {
            backgroundImage = ImageIO.read(new File("background.png"));
            lifeImage = ImageIO.read(new File("life.png"));
            starImage = ImageIO.read(new File("star.png"));
            rockImage = ImageIO.read(new File("rock.png")); // Add your rock image here
            basket = new Basket("basket.png"); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        setLayout(null);

        restartLabel = new JLabel();
        endLabel = new JLabel();

        restartLabel.setBounds(190, 300, 200, 100);
        endLabel.setBounds(400, 300, 200, 100);

        restartLabel.setSize(200, 100);
        endLabel.setSize(200, 100);

        restartLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                restartGame();
                parent.playSound("button_click.wav");
            }
        });

        endLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.exit(0);
            }
        });

        add(restartLabel);
        add(endLabel);

        hideGameOverComponents();
    }

    public void setPlayerImagePath(String playerImagePath) {
        this.playerImagePath = playerImagePath;
    }
    
    public void startGame() {
        basket = new Basket(playerImagePath);
        score = 0;
        lives = 3;
        gameOver = false;
        stars.clear();
        rocks.clear();
        timer.start();
        hideGameOverComponents();
    }

    private void endGame() {
        timer.stop();
        gameOver = true;
        repaint();
        showGameOverComponents();

        if (parent != null) {
            parent.playGameOverSound(); // Trigger game over sound
        }
    }

    private void showGameOverComponents() {
        customizeLabel(restartLabel, "restartButton.png");
        customizeLabel(endLabel, "endButton.png");

        restartLabel.setVisible(true);
        endLabel.setVisible(true);
    }

    private void hideGameOverComponents() {
        restartLabel.setVisible(false);
        endLabel.setVisible(false);
    }

    private void customizeLabel(JLabel label, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(image));
    }

    private void restartGame() {
        remove(restartLabel);
        remove(endLabel);
        CardLayout cl = (CardLayout) parent.getContentPane().getLayout();
        cl.show(parent.getContentPane(), "IntroPanel");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw GIF background
        if (gifImage != null) {
            g.drawImage(gifImage, 0, 0, getWidth(), getHeight(), this);
        }

        if (gameOver) {
            drawGameOver(g);
        } else {
            drawGame(g);
        }
    }

    private void drawGame(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        basket.draw(g2d);
        for (Star star : stars) {
            star.draw(g2d, starImage);
        }
        for (Rock rock : rocks) {
            rock.draw(g2d, rockImage);
        }
        drawScore(g2d);
        drawLives(g2d);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawScore(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Gotham", Font.BOLD, 18));
        g2d.drawString("Score: " + score, 10, 20);
    }

    private void drawLives(Graphics2D g2d) {
        for (int i = 0; i < lives; i++) {
            g2d.drawImage(lifeImage, 10 + (i * 35), 30, null);
        }
    }

    private void drawGameOver(Graphics g) {
        String msg = "GAME OVER HUHU >_<";
        Font small = new Font("Gotham", Font.BOLD, 50);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }

        updateStars();
        updateRocks();
        checkCollisions();
        basket.move();
        repaint();
    }

    private void updateStars() {
        for (int i = 0; i < stars.size(); i++) {
            Star star = stars.get(i);
            if (star.getY() > getHeight()) {
                stars.remove(i);
                i--;
                if (--lives <= 0) {
                    endGame();
                }
            } else {
                star.move();
            }
        }

        // Adjust the threshold for adding new stars
        if (Math.random() < 0.01) { // Adjusted to 2% chance of adding a new star
            stars.add(new Star());
        }
    }

    private void updateRocks() {
        for (int i = 0; i < rocks.size(); i++) {
            Rock rock = rocks.get(i);
            if (rock.getY() > getHeight()) {
                rocks.remove(i);
                i--;
            } else {
                rock.move();
            }
        }

        // Adjust the threshold for adding new rocks
        if (Math.random() < 0.01) { // Adjusted to 1% chance of adding a new rock
            rocks.add(new Rock());
        }
    }

    private void checkCollisions() {
        Rectangle basketTopBounds = new Rectangle(basket.getX(), basket.getY(), basket.getWidth(), 20); // Adjusted to check only the top part

        for (int i = 0; i < stars.size(); i++) {
            Star star = stars.get(i);
            if (basketTopBounds.intersects(star.getBounds())) {
                score += 10;
                stars.remove(i);
                i--;

                starCaught();
            }
        }

        for (int i = 0; i < rocks.size(); i++) {
            Rock rock = rocks.get(i);
            if (basketTopBounds.intersects(rock.getBounds())) {
                rocks.remove(i);
                i--;
                if (--lives <= 0) {
                    endGame();
                }
            }
        }
    }

    public void starCaught() {
        if (parent != null) {
            parent.playStarCaughtSound();
        }
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            basket.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            basket.keyPressed(e);
        }
    }
}

class Star {
    private int x;
    private int y;
    private int speed;

    public Star() {
        this.x = (int) (Math.random() * 800);
        this.y = 0;
        this.speed = 3 + (int) (Math.random() * 2); // Adjusted for slower speed
    }

    public void move() {
        y += speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 10, 10);
    }

    public void draw(Graphics2D g2d, BufferedImage starImage) {
        g2d.drawImage(starImage, x, y, null);
    }
}

class Rock {
    private int x;
    private int y;
    private int speed;

    public Rock() {
        this.x = (int) (Math.random() * 800);
        this.y = 0;
        this.speed = 4 + (int) (Math.random() * 3); // Adjusted for varying speeds
    }

    public void move() {
        y += speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 20, 20); // Adjust the size of the rectangle as needed
    }

    public void draw(Graphics2D g2d, BufferedImage rockImage) {
        g2d.drawImage(rockImage, x, y, null);
    }
  }


class Basket {
    private int x;
    private int y;
    private int dx;
    private int width;
    private int height;
    private BufferedImage image;

    public Basket(String imagePath) {
        this.x = 400;
        this.y = 500; //600
        this.dx = 0;

        // Check if imagePath is valid, otherwise use a default path
        if (imagePath == null || imagePath.isEmpty()) {
            imagePath = "basket.png"; // Path to a default image
        }

        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                throw new IOException("File not found: " + imagePath);
            }
            this.image = ImageIO.read(imageFile);
            this.width = image.getWidth();
            this.height = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
            // Set default dimensions if image loading fails
            this.width = 100;
            this.height = 100;
        }
    }

    public void move() {
        x += dx;
        if (x < 0) {
            x = 0;
        }
        if (x > 700) {
            x = 700;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            dx = -14; // Faster left movement
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = 14; // Faster right movement
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public void draw(Graphics2D g2d) {
        if (image != null) {
            g2d.drawImage(image, x, y, null);
        }
    }
}
}

