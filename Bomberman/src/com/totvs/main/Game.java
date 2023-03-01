package com.totvs.main;

import com.totvs.entities.Bomb;
import com.totvs.entities.Entity;
import com.totvs.entities.Player;
import com.totvs.graphics.BombColors;
import com.totvs.graphics.Spritesheet;
import com.totvs.net.GameClient;
import com.totvs.net.GameServer;
import com.totvs.net.packet.Packet00Login;
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
    private static JFrame frame;
    private Thread thread;
    private boolean isRunning = true;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final int WIDTH = (int) screenSize.getWidth(), HEIGHT = (int) screenSize.getHeight();
    public static final int SCALE = 2;

    private final BufferedImage image;

    public static List<Entity> entities;
    public static Spritesheet player1Spritesheet, tilesSpritesheet, bombSprite;

    public World world;

    public static Player player;

    private static GameClient socketClient;
    private static GameServer socketServer;

    public Game() {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        entities = new ArrayList<>();

        // iniciando spritesheets
        tilesSpritesheet = new Spritesheet("/tiles_spritesheet.png");
        player1Spritesheet = new Spritesheet("/player1_spritesheet.png");
        bombSprite = new Spritesheet("/bombs_spritesheet.png");

//        // iniciando entidades
//        player = new Player("p1", 0 , 0, 8, 8,
//                player1Spritesheet.getSprite(0, 69, 16, 26), BombColors.GOLDEN);
//        entities.add(player);

        // iniciando o mapa
        world = new World("/test_map.png");
        addKeyListener(this);
        initFrame();
    }

    // inicia a janela
    public void initFrame() {
        frame = new JFrame("Bomberman");
        frame.add(this);
        frame.setResizable(false);
        frame.setSize(((World.map.getWidth() * 16) * SCALE) + 16,
                ((World.map.getHeight() * 16) * SCALE) + 39);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // inicia todas as threads do jogo
    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();

        if (JOptionPane.showConfirmDialog(this, "Você quer rodar o servidor?") == 0) {
            socketServer = new GameServer(this);
            socketServer.start();
        }

        socketClient = new GameClient(this, "localhost");
        socketClient.start();

        socketClient.sendData("ping".getBytes());

        Packet00Login loginPacket = new Packet00Login(JOptionPane.showInputDialog(
                "Por favor, digite o nome de usuário"));
        loginPacket.writeData(socketClient);
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

    public static int getPlayerCount() {
        return socketServer.connectedPlayers.size();
    }

    // Roda a cada frame
    public void tick() {
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).tick();
        }
        for (int i = 0; i < World.tiles.length; i++) {
            World.tiles[i].tick();
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

        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).render(g);
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
        if (e.getKeyCode() == KeyEvent.VK_D)
            player.right = true;
        else if (e.getKeyCode() == KeyEvent.VK_A)
            player.left = true;

        if (e.getKeyCode() == KeyEvent.VK_W)
            player.up = true;
        else if (e.getKeyCode() == KeyEvent.VK_S)
            player.down = true;

        if (e.getKeyCode() == KeyEvent.VK_E && player.hasBombs() && !player.isDead)
            Bomb.placeBomb(player.getX(), player.getY(), Game.bombSprite, player);
    }

    // Botão solto
    @Override
    public void keyReleased(KeyEvent e) {
        // jogador 1
        if (e.getKeyCode() == KeyEvent.VK_D)
            player.right = false;
        else if (e.getKeyCode() == KeyEvent.VK_A)
            player.left = false;

        if (e.getKeyCode() == KeyEvent.VK_W)
            player.up = false;
        else if (e.getKeyCode() == KeyEvent.VK_S)
            player.down = false;
    }
}
