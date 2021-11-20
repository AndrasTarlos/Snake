import javax.swing.*;

public class GameFrame extends JFrame {
    ImageIcon snake = new ImageIcon("C:\\Users\\andra\\OneDrive - Bildungszentrum Zürichsee\\Coding\\Java\\MyProjects\\Snake\\src\\images\\snake.png");
    GameFrame() {
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setIconImage(snake.getImage());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}