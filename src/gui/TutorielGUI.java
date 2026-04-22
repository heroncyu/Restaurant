package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import engine.process.SimulationUtility;

public class TutorielGUI extends JDialog {

    private Font fontTitre = new Font("comic sans ms", Font.BOLD, 22);
    private Font fontTexte = new Font("comic sans ms", Font.PLAIN, 14);

    private JPanel panel = new JPanel();

    public TutorielGUI(JFrame owner) {
        super(owner, "Tutoriel", true);
        setSize(800, 750);
        setLocationRelativeTo(owner);
        setResizable(false);

        initPanel();

        setVisible(true);
    }

    public TutorielGUI() {
        this(null);
    }

    private void initPanel() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.darkGray);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(creerSection(
                "Clients & Personnel",
                "Des clients affamés débarquent dans ton restaurant ! Assigne des serveurs pour prendre les commandes et des cuisiniers pour préparer les plats. Surveille la satisfaction de tes clients, si elle tombe à 0, ils repartent sans payer. Recrute et améliore ton équipe pour encaisser un maximum !",
                "src/resources/client.png",
                new Color(200, 160, 0)
        ));

        panel.add(Box.createVerticalStrut(15));

        panel.add(creerSection(
                "Gestion",
                "Un bon restaurant sans ingrédients, c'est un restaurant fermé. Achète tes ingrédients, surveille ton stock et garde un oeil sur ta réputation. Plus elle est haute, plus les clients affluent. Pense aussi à nettoyer ton restaurant, personne ne veut manger dans un endroit sale !",
                "src/resources/tajine.png",
                new Color(50, 180, 50)
        ));

        panel.add(Box.createVerticalStrut(15));
        panel.add(creerSection(
                "Mode Construction",
                "Ton restaurant est trop petit ? Agrandis le terrain pour accueillir plus de clients. Place de nouveaux meubles comme des tables ou des fours pour booster ta production. Chaque agrandissement a un coût, investis intelligemment !",
                "src/resources/table.png",
                new Color(180, 80, 80)
        ));

        panel.add(Box.createVerticalStrut(15));
        panel.add(creerSection(
                "Succès",
                "Des défis t'attendent en coulisses ! Sers tes premiers clients, accumule de la richesse, bâtis ta réputation... Chaque succès débloqué te rapporte des récompenses en gold. Quand le bouton succès devient orange, fonce le réclamer !",
                "src/resources/succes.png",
                new Color(50, 120, 200)
        ));

        JScrollPane scroll = new JScrollPane(panel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        add(scroll);
    }

    private JPanel creerSection(String titre, String texte, String cheminImage, Color couleur) {
        JPanel section = new JPanel(new BorderLayout(15, 0));
        section.setBackground(new Color(50, 50, 50));
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(couleur, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        section.setMaximumSize(new Dimension(760, 170));
        section.setPreferredSize(new Dimension(760, 170));

        JLabel imgLabel = new JLabel();
        Image img = SimulationUtility.lireImage(cheminImage);
        if (img != null) {
            imgLabel.setIcon(new ImageIcon(img.getScaledInstance(100, 80, Image.SCALE_SMOOTH)));
        }
        imgLabel.setPreferredSize(new Dimension(90, 90));
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel infos = new JPanel();
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setOpaque(false);

        JLabel titreLabel = new JLabel(titre);
        titreLabel.setFont(fontTitre);
        titreLabel.setForeground(couleur);
        infos.add(titreLabel);
        infos.add(Box.createVerticalStrut(8));

        JTextArea textArea = new JTextArea(texte);
        textArea.setFont(fontTexte);
        textArea.setForeground(Color.WHITE);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        infos.add(textArea);

        section.add(imgLabel, BorderLayout.WEST);
        section.add(infos, BorderLayout.CENTER);

        return section;
    }
}