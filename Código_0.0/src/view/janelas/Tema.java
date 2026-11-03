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

/**
 * Visual único para todas as abas. Chame Tema.aplicar() uma vez, antes de criar a janela.
 *
 * - Se o .jar do FlatLaf estiver no projeto, usa o FlatLaf (visual plano e moderno).
 * - Se não estiver, usa o Nimbus (já vem no Java). O programa funciona nos dois casos.
 *
 * Para voltar ao visual padrão do Java, é só comentar a chamada Tema.aplicar().
 */
public class Tema {

    // Paleta (neutros suaves + um azul de destaque)
    // Para escurecer ou clarear, mexa só nestes três valores: FUNDO, CARTAO e CAMPO.
    public static final Color AZUL = new Color(37, 99, 235);
    public static final Color FUNDO = new Color(226, 230, 236);   // fundo da janela
    public static final Color CARTAO = new Color(244, 246, 249);  // "papel" dos cartões
    public static final Color CAMPO = new Color(250, 251, 252);   // campos, caixas e tabela (quase branco)
    public static final Color BORDA = new Color(207, 213, 222);
    public static final Color TEXTO = new Color(31, 41, 55);

    /** Converte uma cor para o texto "#RRGGBB" que o FlatLaf entende. */
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

    // ---------- FlatLaf (via reflexão: o código compila mesmo sem o .jar) ----------

    private static boolean aplicaFlatLaf() {
        try {
            // cores principais: precisam ser definidas ANTES de ligar o tema
            Class.forName("com.formdev.flatlaf.FlatLaf")
                    .getMethod("setGlobalExtraDefaults", Map.class)
                    .invoke(null, Map.of(
                            "@accentColor", hex(AZUL),
                            "@background", hex(FUNDO),
                            "@componentBackground", hex(CAMPO)));

            Class.forName("com.formdev.flatlaf.FlatLightLaf")
                    .getMethod("setup")
                    .invoke(null);

            // cantos arredondados
            UIManager.put("Component.arc", 12);
            UIManager.put("Button.arc", 12);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.trackArc", 999);

            // abas e tabela mais arejadas
            UIManager.put("TabbedPane.tabHeight", 40);
            UIManager.put("Table.rowHeight", 32);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.gridColor", BORDA);
            UIManager.put("Table.alternateRowColor", new Color(241, 243, 247));
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));

            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));
            return true;
        } catch (Exception | LinkageError e) {
            return false; // sem FlatLaf: cai para o Nimbus
        }
    }

    // ---------- Nimbus (plano B, sem instalar nada) ----------

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

    // ---------- Peças prontas para as telas ficarem iguais ----------

    /** Título grande no topo de cada aba. */
    public static JLabel criaTitulo(String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 24f));
        titulo.setForeground(TEXTO);
        return titulo;
    }

    /** Cartão branco com borda fina (o "papel" onde ficam os campos ou a tabela). */
    public static JPanel criaCartao(LayoutManager layout) {
        JPanel cartao = new JPanel(layout);
        cartao.setBackground(CARTAO);
        cartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        return cartao;
    }

    /** Botão de ação principal: fundo azul e letra branca. */
    public static void destaque(JButton botao) {
        botao.setBackground(AZUL);
        botao.setForeground(Color.WHITE);
        botao.setFont(botao.getFont().deriveFont(Font.BOLD));
        botao.setFocusPainted(false);
    }
}