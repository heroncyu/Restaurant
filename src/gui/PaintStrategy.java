package gui;

import engine.map.Block;
import engine.map.Map;
import engine.map.Zone;
import engine.mobile.Client;
import engine.mobile.Cuisinier;
import engine.mobile.Meuble;
import engine.mobile.Serveur;
import engine.prestige.Succes;
import engine.process.SimulationUtility;
import gui.info.JourLabel;
import gui.info.MonnaieLabel;
import gui.info.PropreteLabel;
import gui.info.ReputationLabel;
import gui.info.TempsLabel;

import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JLabel;

import static config.GameConfiguration.BLOCK_SIZE;

public class PaintStrategy {
    private Image solHerbe = SimulationUtility.lireImage("src/resources/herbe.jpg");
    private Image solSalle = SimulationUtility.lireImage("src/resources/salle.jpg");
    private Image solCuisine = SimulationUtility.lireImage("src/resources/cuisine.jpg");
    private Image table = SimulationUtility.lireImage("src/resources/table.png");
    private Image four = SimulationUtility.lireImage("src/resources/four.png");
    private Image plante = SimulationUtility.lireImage("src/resources/plante.png");
    private Image porteManteau = SimulationUtility.lireImage("src/resources/porte_manteau.png");
    private Image serveuse = SimulationUtility.lireImage("src/resources/serveuse.png");

    public void paint(Map map, HashMap<String, Zone> zones, Graphics graphics) {
        int blockSize = BLOCK_SIZE;
        Block[][] blocks = map.getBlocks();


        for (int ligne = 0; ligne < map.getLineCount(); ligne++) {
            for (int colonne = 0; colonne < map.getColumnCount(); colonne++) {
                Block block = blocks[ligne][colonne];

                int x = colonne * blockSize;
                int y = ligne * blockSize;

                String cord = String.valueOf(block.getLine());
                graphics.drawString(cord, x + 4, y + 15);

                String nomZone = null;
                Iterator<Zone> it = zones.values().iterator();
                while (it.hasNext()) {
                    Zone zone = it.next();
                    if (zone.appartientBlock(block)) {
                        nomZone = zone.getNom();
                    }
                }

                if (nomZone == null) {
                    if (solHerbe != null){
                        graphics.drawImage(solHerbe, x, y, blockSize, blockSize, null);
                    }
                    else{
                        graphics.setColor(new Color(34, 139, 34));
                        graphics.fillRect(x, y, blockSize, blockSize);
                    }
                } else if (nomZone.equals("CUISINE")) {
                    if(solSalle != null){
                        graphics.drawImage(solCuisine, x, y, blockSize, blockSize, null);
                    }
                    else{
                        graphics.setColor(new Color(200, 200, 200));
                        graphics.fillRect(x, y, blockSize, blockSize);
                    }


                } else if (nomZone.equals("SALLE")) {
                    if(solSalle != null){
                        graphics.drawImage(solSalle, x, y, blockSize, blockSize, null);
                    }
                    else{
                        graphics.setColor(new Color(222, 184, 135));
                        graphics.fillRect(x, y, blockSize, blockSize);
                    }

                } else if (nomZone.equals("RESERVE")) {
                    graphics.setColor(new Color(100, 100, 100));
                    graphics.fillRect(x, y, blockSize, blockSize);
                } else if (nomZone.equals("CONSTRUCTIBLE")) {
                    graphics.setColor(Color.cyan);
                    graphics.fillRect(x, y, blockSize, blockSize);
                }


                graphics.setColor(Color.BLACK);
                graphics.drawRect(colonne * blockSize, ligne * blockSize, blockSize, blockSize);
            }
        }
    }

    public void paint(List<Block> blocksOccupees, Graphics graphics) {
        int blockSize = BLOCK_SIZE;
        for (Block block : blocksOccupees) {
            graphics.setColor(new Color(255, 0, 0, 32));
            graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
        }
    }

    public void paint(Meuble meuble, Graphics graphics) {
        Block position = meuble.getPosition();
        int blockSize = BLOCK_SIZE;

        int x = position.getColumn() * blockSize;
        int y = position.getLine() * blockSize;

        String type = meuble.getType();

        if (type.equals("FOUR")) {
            if (four != null) {
                graphics.drawImage(four, x - 10, y - 10, blockSize + 20, blockSize + 20, null);
            } else {
                graphics.setColor(new Color(240, 231, 230));
                graphics.fillRect(x + 4, y + 4, blockSize - 10, blockSize - 10);
                graphics.setColor(new Color(105, 100, 99));
                graphics.drawRect(x + 4, y + 4, blockSize - 10, blockSize - 10);
                graphics.setColor(new Color(23, 20, 20));
                graphics.fillRect(x + 11, y + 11, blockSize - 24, blockSize - 24);
            }
        } else if (type.equals("TABLE")) {
            if (table != null) {
                graphics.drawImage(table, x - 30, y - 30, blockSize + 60, blockSize + 60, null);
            } else {
                graphics.setColor(new Color(139, 69, 19));
                graphics.fillOval(x + 6, y + 6, blockSize - 12, blockSize - 12);
                graphics.setColor(new Color(100, 50, 10));
                graphics.drawOval(x + 6, y + 6, blockSize - 12, blockSize - 12);
            }

        } else if (type.equals("PLANTE")) {
            if (plante != null) {
                graphics.drawImage(plante, x - 10, y - 15, blockSize + 20, blockSize + 20, null);
            } else {
                graphics.setColor(new Color(139, 90, 43));
                graphics.fillOval(x + 10, y + 10, blockSize - 20, blockSize - 20);
                graphics.setColor(new Color(0, 160, 0));
                graphics.fillOval(x + 15, y + 15, blockSize - 30, blockSize - 30);
                graphics.setColor(new Color(177, 110, 186));
                graphics.fillOval(x + 30, y + 25, blockSize - 70, blockSize - 70);
                graphics.fillOval(x + 50, y + 35, blockSize - 70, blockSize - 70);
            }
        } else if (type.equals("PORTE_MANTEAU")) {
            if (porteManteau != null) {
                graphics.drawImage(porteManteau, x - 10, y - 18, blockSize + 20, blockSize + 20, null);
            } else {
                graphics.drawString("P.Manteau", x, y);
            }
        }
    }

    public void paint(Client client, Graphics graphics) {
        Block position = client.getPosition();
        int blockSize = BLOCK_SIZE;

        int x = position.getColumn() * blockSize;
        int y = position.getLine() * blockSize;

        graphics.setColor(Color.BLUE);
        graphics.fillOval(x + (blockSize - 20) / 2, y + (blockSize - 20) / 2, 20, 20);

        graphics.setFont(new Font("Dialog", Font.PLAIN, 18));
        graphics.drawString((String.valueOf(client.getSatisfaction())), x + 5, y + 5);
    }

    public void paint(Cuisinier cuisinier, Graphics graphics) {
        Block position = cuisinier.getPosition();
        int blockSize = BLOCK_SIZE;

        int y = position.getLine() * blockSize;
        int x = position.getColumn() * blockSize;

        graphics.setColor(Color.YELLOW);
        graphics.fillOval(x + (blockSize - 20) / 2, y + (blockSize - 20) / 2, 20, 20);
        graphics.setColor(new Color(240, 231, 230));
        graphics.setFont(new Font("Dialog", Font.PLAIN, 18));
        graphics.drawString(cuisinier.getName(), x + 5, y + 5);
    }

    public void paint(Serveur serveur, Graphics graphics, int animationTick) {
        Block position = serveur.getPosition();
        int blockSize = BLOCK_SIZE;

        int y = position.getLine() * blockSize;
        int x = position.getColumn() * blockSize;

        if(serveuse != null){
            graphics.drawImage(serveuse, x, y, blockSize, blockSize, null);
        }
        else {
            graphics.setColor(Color.RED);
            graphics.fillOval(x + (blockSize - 20) / 2, y + (blockSize - 20) / 2, 20, 20);
        }

        graphics.setColor(new Color(240, 231, 230));
        graphics.setFont(new Font("Dialog", Font.PLAIN, 18));
        graphics.drawString(serveur.getName(), x + 5, y + 5);
    }

    public void paint(JLabel label, Graphics graphics) {
        int weight = label.getWidth();
        int height = label.getHeight();

        if (label instanceof MonnaieLabel) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setColor(new Color(101, 67, 33));
            g2d.fillRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
            g2d.setColor(new Color(255, 215, 0));
            g2d.fillOval(5, height / 2 - 8, 16, 16);
            g2d.setColor(new Color(184, 134, 11));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(5, height / 2 - 8, 16, 16);

        } else if (label instanceof JourLabel) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setColor(Color.darkGray);
            g2d.fillRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);

        } else if (label instanceof TempsLabel) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setColor(Color.darkGray);
            g2d.fillRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);

        } else if (label instanceof ReputationLabel) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setColor(Color.CYAN);
            g2d.fillRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);

        } else if (label instanceof PropreteLabel) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setColor(Color.GREEN);
            g2d.fillRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
        }
    }

    public void paint(Graphics graphics, String message, int prix) {
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Comis Sans MS", Font.BOLD, 24));
        if (prix > 0) {
            graphics.drawString(message + " ( " + prix + " G )", 10, 30);
        } else {
            graphics.drawString(message, 10, 30);
        }
    }

    public void paint(Graphics graphics, int panelWidth) {
        String message = "Manque d'ingrédients, veuillez en acheter, sinon plus de nouveaux clients...";

        graphics.setFont(new Font("Comis Sans MS", Font.BOLD, 24));
        int textWidth = graphics.getFontMetrics().stringWidth(message);
        int x = (panelWidth - textWidth) / 2;
        int y = 40;

        graphics.setColor(new Color(255, 50, 50));
        graphics.drawString(message, x, y);
    }
    public void paint(Succes s, Graphics g, int panelWidth, int panelHeight) {
        int largeur = 300;
        int hauteur = 100;
        int x = (panelWidth - largeur) / 2;
        int y = 30;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(30, 30, 30, 220));
        g2d.fillRoundRect(x, y, largeur, hauteur, 20, 20);
        g2d.setColor(new Color(200, 160, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, largeur, hauteur, 20, 20);

        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        g2d.setColor(new Color(200, 160, 0));
        g2d.drawString("Succes debloque !", x + 20, y + 28);

        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        g2d.setColor(Color.WHITE);
        g2d.drawString(s.getNom(), x + 20, y + 55);

        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
        g2d.setColor(new Color(200, 160, 0));
        g2d.drawString("+" + s.getRecompense() + " gold", x + 20, y + 80);
    }
}