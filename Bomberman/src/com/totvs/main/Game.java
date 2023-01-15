package com.totvs.main;

import com.totvs.entities.Bomb;
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
    private final int SCALE = 2;

    private final BufferedImage image;

    public static List<Entity> entities;
    public static Spritesheet player1Spritesheet, tilesSpritesheet, bombSprite;

    public World world;

    public static Player player1;
    public static Player player2;

    public Game() {
        addKeyListener(this);
        this.setPreferredSize(new Dimension(WIDTH , HEIGHT));
        initFrame();

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        entities = new ArrayList<>();

        // iniciando spritesheets
        tilesSpritesheet = new Spritesheet("/tiles_spritesheet.png");
        player1Spritesheet = new Spritesheet("/player1_spritesheet.png");
        bombSprite = new Spritesheet("/bombs_spritesheet.png");

        // iniciando entidades
        player1 = new Player(0 , 0, 8, 8,
                player1Spritesheet.getSprite(0, 69, 16, 26));
        entities.add(player1);

        player2 = new Player(0 , 0, 8, 8,
                player1Spritesheet.getSprite(0, 69, 16, 26));
        entities.add(player2);

        // iniciando o mapa
        world = new World("/test_map.png");
    }

    // inicia a janela
    public void initFrame() {
        frame = new JFrame("Bomberman");
        frame.add(this);
        frame.setResizable(false);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
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
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).tick();
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
        g.setColor(new Color(135, 206, 235));
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
        // jogador 1
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player1.right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            player1.left = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_W) {
            player1.up = true;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            player1.down = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_E && player1.hasBombs()) {
            Bomb.placeBomb(player1.getX(), player1.getY(), Game.bombSprite, player1);
            player1.placedBombs++;
        }

        // jogador 2
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player2.right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player2.left = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            player2.up = true;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            player2.down = true;
        }
    }

    // Botão solto
    @Override
    public void keyReleased(KeyEvent e) {
        // jogador 1
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player1.right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            player1.left = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_W) {
            player1.up = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            player1.down = false;
        }

        // jogador 2
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player2.right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player2.left = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            player2.up = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            player2.down = false;
        }
    }
}
