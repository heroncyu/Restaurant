package engine.process;

/**
 * Représente un texte visuel temporaire destiné à s'afficher sur l'écran de jeu.
 * Il possède des coordonnées (x, y) et une durée de vie (life).
 * Utilisé principalement pour afficher les gains d'argent au-dessus des clients.
 *
 * @author Sajid
 * @version 1.1
 */
public class FloatingText {
    private int x;
    private int y;
    private int life;
    private String text;

    /**
     * Constructeur d'un texte flottant.
     * @param x Position horizontale en pixels
     * @param y Position verticale en pixels
     * @param text Le texte à afficher
     */
    public FloatingText(int x, int y, String text) {
        this.x = x;
        this.y = y;
        this.life = 10;
        this.text = text;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getLife() { return life; }
    public void setLife(int life) { this.life = life; }

    public String getText() { return this.text;}
}