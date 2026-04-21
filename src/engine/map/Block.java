package engine.map;

/**
 * Représente une case unitaire d'un espace en 2 dimensions.
 * 
 * Un Block permet le paramétrage spatial de la {@link Map}.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Block {

        private int line;
        private int column;

        /**
         * Instantie la case avec sa position sur la carte.
         * 
         * @param line position en ordonnée
         * @param column position en abscisse
         */
        public Block(int line, int column) {
            this.line = line;
            this.column = column;
        }

        public int getLine() {
            return line;
        }

        public int getColumn() {
            return column;
        }

        /**
         * Représente textuellement ce block.
         * 
         * @return chaîne d'informations liée au bloc
         */
        @Override
        public String toString() {
            return "Block [line=" + line + ", column=" + column + "]";
        }


}
