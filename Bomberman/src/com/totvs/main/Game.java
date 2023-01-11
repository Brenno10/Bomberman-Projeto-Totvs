package com.totvs.main;

import com.totvs.entities.Entity;
import com.totvs.entities.Player;
import com.totvs.graphics.Spritesheet;
import com.totvs.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Game extends Canvas implements Runnable, KeyListener {
    public static JFrame frame;
    private Thread thread;
    private boolean isRunning = true;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final int WIDTH = (int) screenSize.getWidth(), HEIGHT = (int) screenSize.getHeight();
    private final int SCALE = 3;

    private final BufferedImage image;

    public List<Entity> entities;
    public static Spritesheet spritesheet;

    public World world;

    private final Player player;

    public Game() {
        addKeyListener(this);
        this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();

        world = new World("/tiles_spritesheet.png");
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        entities = new ArrayList<>();
        spritesheet = new Spritesheet("/player1_spritesheet.png");

        // inicia as entidades
        player = new Player(0, 0, 32, 32,
                spritesheet.getSprite(0, 69, 14, 25));

        // adiciona as entidades na janela
        entities.add(player);
    }

    // inicia a janela
    public void initFrame() {
        frame = new JFrame("Bomberman");
        frame.add(this);
        frame.setResizable(false);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // inicia todas as threads do jogo
    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    // fecha todas as threads do jogo
    public synchronized void stop() throws InterruptedException {
        isRunning = false;
        thread.join();
    }

    // método principal
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    // Roda a cada frame
    public void tick() {
        for (Entity e : entities) {
            e.tick();
        }
    }

    // Renderiza as texturas da janela
    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();

        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        world.render(g);

        for (Entity e : entities) {
            e.render(g);
        }

        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                frames++;
                delta--;
            }
            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }

        // caso aconteça algum erro no loop while, fecha todos os threads para conservar memória
        try {
            stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Eventos do teclado

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // Botão apertado
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
            player.left = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
            player.up = true;
        } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
            player.down = true;
        }
    }

    // Botão solto
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
            player.left = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
            player.up = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
            player.down = false;
        }
    }
}
