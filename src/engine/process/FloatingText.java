package engine.process;

public class FloatingText {
    private int x;
    private int y;
    private String text;
    private int life;
    private int maxLife;

    public FloatingText(int x, int y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.maxLife = 15;
        this.life = maxLife;
    }

    public void update() {
        this.y -= 4;
        this.life--;
    }

    public boolean isDead() {
        return life <= 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public String getText() { return text; }
    public int getLife() { return life; }
    public int getMaxLife() { return maxLife; }
}