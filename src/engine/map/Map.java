package engine.map;

/**
 * L'environnement principal servant de grille 2D du restaurant.
 * 
 * La carte regroupe une grille totale de {@link Block} permettant la navigation.
 * 
 * @see engine.map.Zone
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Map {
    private Block[][] blocks;
    private int lineCount;
    private int columnCount;

    /**
     * Crée une carte de la dimension spécifiée en remplissant sa représentation.
     * 
     * @param lineCount nombre de lignes 
     * @param columnCount nombre de colonnes
     */
    public Map(int lineCount, int columnCount) {
        init(lineCount, columnCount);

        for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                blocks[lineIndex][columnIndex] = new Block(lineIndex, columnIndex);
            }
        }
    }

    /**
     * Initialise la taille du tableau interne et stocke les données de dimensions.
     * 
     * @param lineCount lignes de la nouvelle map
     * @param columnCount colonnes de la nouvelle map
     */
    private void init(int lineCount, int columnCount) {
        this.lineCount = lineCount;
        this.columnCount = columnCount;

        blocks = new Block[lineCount][columnCount];

    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Récupère un bloc particulier aux coordonnées renseignées.
     * 
     * @param line ligne de recherche
     * @param column colonne de recherche
     * @return contenant de la position
     */
    public Block getBlock(int line, int column) {
        return blocks[line][column];
    }

    /**
     * Indique si un bloc est situé sur la bordure supérieure du plateau.
     * 
     * @param block case à examiner
     * @return vrai s'il est au bord en haut, faux sinon
     */
    public boolean isOnTop(Block block) {
        int line = block.getLine();
        return line == 0;
    }

    /**
     * Indique si un bloc est situé sur la bordure inférieure.
     * 
     * @param block case à examiner
     * @return vrai s'il touche le fond, faux sinon
     */
    public boolean isOnBottom(Block block) {
        int line = block.getLine();
        return line == lineCount - 1;
    }

    /**
     * Indique si un bloc est situé sur la bordure gauche.
     * 
     * @param block case à examiner
     * @return vrai si collé à gauche, faux sinon
     */
    public boolean isOnLeftBorder(Block block) {
        int column = block.getColumn();
        return column == 0;
    }

    /**
     * Indique si un bloc est situé sur la bordure droite.
     * 
     * @param block case à examiner
     * @return vrai si dernier élément en colonne droite, faux sinon
     */
    public boolean isOnRightBorder(Block block) {
        int column = block.getColumn();
        return column == columnCount - 1;
    }

    /**
     * Permet de savoir si un bloc se situe sur un périmètre quelconque de la map.
     * 
     * Il rassemble et fait appel aux différentes vérifications de bordures.
     * 
     * @param block bloc visé
     * @return vrai si la condition est remplie sur l'un des bords
     */
    public boolean isOnBorder(Block block) {
        return isOnTop(block) || isOnBottom(block) || isOnLeftBorder(block) || isOnRightBorder(block);
    }

    /**
     * Trouve le block qui correspond aux coordonnées de valeur spatiale de l'utilisateur.
     * 
     * @param mouseX paramètre de la position X
     * @param mouseY paramètre de la position Y (transformée en line)
     * @return un {@link Block} à cette position si atteignable, null sinon
     */
    public Block getBlockCliquer(int mouseX, int mouseY) {
        int line = mouseY;
        int column = mouseX;
        if (line >= 0 && line < lineCount && column >= 0 && column < columnCount) {
            return blocks[line][column];
        }
        return null;
    }
}