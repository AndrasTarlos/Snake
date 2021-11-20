import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.TimerTask;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX, appleY;
    boolean running;
    boolean countdown;
    boolean inSettings;
    int countdownCounter;

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    Direction direction;
    Timer timer;
    Random random;
    JButton restartButton;
    JButton settingsButton;
    Icon snake = new ImageIcon("C:\\Users\\andra\\OneDrive - Bildungszentrum Zürichsee\\Coding\\Java\\MyProjects\\Snake\\src\\images\\settings.jpeg");
    Settings settings;
    MyKeyAdapter keyAdapter;

    GamePanel() {
        random = new Random();
        settings = new Settings(SCREEN_WIDTH, SCREEN_HEIGHT);
        keyAdapter = new MyKeyAdapter(this);

        settingsButton = new JButton(snake);
        settingsButton.setVisible(true);
        settingsButton.setBounds(SCREEN_WIDTH - snake.getIconWidth(), 0, snake.getIconWidth(), snake.getIconHeight());
        settingsButton.addActionListener(this);
        settingsButton.setFocusable(false);
        settingsButton.setBorder(BorderFactory.createEmptyBorder());
        settingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        restartButton = new JButton("Restart");
        restartButton.setBounds(SCREEN_WIDTH / 2 - 50, SCREEN_HEIGHT / 2 + 50, 100, 50);
        restartButton.setVisible(false);
        restartButton.setFocusable(false);
        restartButton.addActionListener(this);
        restartButton.setFont(new Font("Ink Free", Font.BOLD, 17));
        restartButton.setForeground(Color.red);
        restartButton.setBackground(Color.black);
        restartButton.setBorder(BorderFactory.createEmptyBorder());
        restartButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.setLayout(null);
        this.addKeyListener(keyAdapter);
        this.add(settingsButton);
        this.add(restartButton);
        this.add(settings);
        startGame();
    }

    public void startGame() {
        running = false;
        countdown = true;
        inSettings = false;
        settings.setVisible(false);
        countdownCounter = 4;
        countdownTimer();
        newApple();
        direction = Direction.RIGHT;
        // Spawn all the body parts on the right place
        for (int i = 0; i < x.length; i++) {
            x[i] = -UNIT_SIZE * i;
            y[i] = 0;
        }
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void countdownTimer() {
        java.util.Timer timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                countdownCounter--;
                if (countdownCounter <= 0) {
                    countdown = false;
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        draw(g2d);
    }

    public void draw(Graphics2D g2d) {
        if (running) {
            if (countdown) {
                countDownDraw(g2d);
            }
            g2d.setColor(Color.red);
            g2d.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g2d.setColor(Color.green);
                } else {
                    g2d.setColor(new Color(135, 180, 0));
                }
                g2d.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
            g2d.setColor(Color.red);
            g2d.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g2d.getFont());
            g2d.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g2d.getFont().getSize());
        } else {
            gameOver(g2d);
        }
    }

    public void gameOver(Graphics2D g2d) {
        g2d.setColor(Color.red);
        g2d.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g2d.getFont());
        g2d.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g2d.getFont().getSize());

        g2d.setColor(Color.red);
        g2d.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g2d.getFont());
        g2d.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        if (!inSettings) {
            restartButton.setVisible(true);
        }
    }

    public void countDownDraw(Graphics2D g2d) {
        g2d.setColor(Color.red);
        g2d.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g2d.getFont());
        g2d.drawString(Integer.toString(countdownCounter), (SCREEN_WIDTH - metrics.stringWidth(Integer.toString(countdownCounter))) / 2, SCREEN_HEIGHT / 2);
    }

    public void newApple() {
        int xPos;
        int yPos;
        // Check if new apple spawn pos is in the snake
        // If it is then generate new random positions
        while (true) {
            xPos = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
            yPos = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
            boolean inSnake = false;
            for (int i = 0; i < x.length; i++) {
                if (x[i] != xPos && y[i] != yPos) {
                    inSnake = true;
                    break;
                }
            }
            if (inSnake) {
                break;
            }
        }
        appleX = xPos;
        appleY = yPos;
    }

    public void move() {
        // Change position of the body parts
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        // Change head position according to direction
        switch (direction) {
            case UP -> y[0] = y[0] - UNIT_SIZE;
            case DOWN -> y[0] = y[0] + UNIT_SIZE;
            case LEFT -> x[0] = x[0] - UNIT_SIZE;
            case RIGHT -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            applesEaten++;
            bodyParts++;
            newApple();
        }
    }

    public void checkCollisions() {
        // check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // check if head touches left & right & upper & down border
        if (x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            if (!countdown && !inSettings) {
                move();
                checkApple();
                checkCollisions();
            }
        }

        if (e.getSource() == restartButton) {
            applesEaten = 0;
            bodyParts = 6;
            timer.stop();
            running = true;
            restartButton.setVisible(false);
            startGame();
        }

        if (e.getSource() == settingsButton) {
            keyAdapter.settingsMechanic();
        }
        repaint();
    }
}