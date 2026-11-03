package view.janelas;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Tema {

    public static final Color AZUL = new Color(37, 99, 235);
    public static final Color FUNDO = new Color(226, 230, 236);   
    public static final Color CARTAO = new Color(244, 246, 249);  
    public static final Color CAMPO = new Color(250, 251, 252);   
    public static final Color BORDA = new Color(207, 213, 222);
    public static final Color TEXTO = new Color(31, 41, 55);

    private static String hex(Color c) {
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }

    public static void aplicar() {
        if (aplicaFlatLaf()) {
            System.out.println("Tema: FlatLaf");
        } else {
            aplicaNimbus();
            System.out.println("Tema: Nimbus (FlatLaf não encontrado)");
        }
    }

    private static boolean aplicaFlatLaf() {
        try {
            
            Class.forName("com.formdev.flatlaf.FlatLaf")
                    .getMethod("setGlobalExtraDefaults", Map.class)
                    .invoke(null, Map.of(
                            "@accentColor", hex(AZUL),
                            "@background", hex(FUNDO),
                            "@componentBackground", hex(CAMPO)));

            Class.forName("com.formdev.flatlaf.FlatLightLaf")
                    .getMethod("setup")
                    .invoke(null);

            UIManager.put("Component.arc", 12);
            UIManager.put("Button.arc", 12);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.trackArc", 999);

            UIManager.put("TabbedPane.tabHeight", 40);
            UIManager.put("Table.rowHeight", 32);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.gridColor", BORDA);
            UIManager.put("Table.alternateRowColor", new Color(241, 243, 247));
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));

            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));
            return true;
        } catch (Exception | LinkageError e) {
            return false; 
        }
    }

    private static void aplicaNimbus() {
        try {
            UIManager.put("nimbusBase", AZUL);
            UIManager.put("nimbusBlueGrey", new Color(190, 198, 210));
            UIManager.put("control", FUNDO);
            UIManager.put("nimbusLightBackground", CAMPO);
            UIManager.put("nimbusSelectionBackground", AZUL);
            UIManager.put("nimbusFocus", AZUL);
            UIManager.put("text", TEXTO);
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));

            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Não foi possível aplicar o tema: " + e.getMessage());
        }
    }

    public static JLabel criaTitulo(String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 24f));
        titulo.setForeground(TEXTO);
        return titulo;
    }

    public static JPanel criaCartao(LayoutManager layout) {
        JPanel cartao = new JPanel(layout);
        cartao.setBackground(CARTAO);
        cartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        return cartao;
    }
    
    public static void destaque(JButton botao) {
        botao.setBackground(AZUL);
        botao.setForeground(Color.WHITE);
        botao.setFont(botao.getFont().deriveFont(Font.BOLD));
        botao.setFocusPainted(false);
    }
}