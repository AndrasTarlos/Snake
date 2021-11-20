import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyKeyAdapter extends KeyAdapter {
    GamePanel gamePanel;
    MyKeyAdapter(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            settingsMechanic();
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP:
                if (gamePanel.direction != GamePanel.Direction.DOWN)
                    gamePanel.direction = GamePanel.Direction.UP;
                break;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN:
                if (gamePanel.direction != GamePanel.Direction.UP)
                    gamePanel.direction = GamePanel.Direction.DOWN;
                break;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT:
                if (gamePanel.direction != GamePanel.Direction.RIGHT)
                    gamePanel.direction = GamePanel.Direction.LEFT;
                break;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT:
                if (gamePanel.direction != GamePanel.Direction.LEFT)
                    gamePanel.direction = GamePanel.Direction.RIGHT;
                break;
        }
    }

    public void settingsMechanic() {
        if (gamePanel.settings.isVisible()) {
            gamePanel.settings.setVisible(false);
            gamePanel.inSettings = false;
            if (!gamePanel.running) {
                gamePanel.restartButton.setVisible(true);
            }
        } else {
            gamePanel.settings.setVisible(true);
            gamePanel.inSettings = true;
            gamePanel.restartButton.setVisible(false);
        }
    }
}
