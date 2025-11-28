/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 *
 * @author Nathan
 */

public class NormalFrame extends JFrame {

    private Steam steam;
    private int codePlayer;

    private JPanel catalogPanel;

    public NormalFrame(Steam steam, int codePlayer) {
        this.steam = steam;
        this.codePlayer = codePlayer;

        setTitle("Steam - Usuario");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        catalogPanel = new JPanel();
        catalogPanel.setLayout(new BoxLayout(catalogPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(catalogPanel);
        add(scroll);

        mostrarCatalogo();
    }

    private void mostrarCatalogo() {
        catalogPanel.removeAll();
        ArrayList<Game> juegos = steam.getAllGames();

        for (Game g : juegos) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder(g.getTitulo()));

           
            ImageIcon icon = new ImageIcon(g.getRutaImg());
            JLabel lblImg = new JLabel(icon);
            panel.add(lblImg, BorderLayout.WEST);

            
            JTextArea info = new JTextArea("SO: " + g.getSistemaOperativo() +
                                           "\nEdad mínima: " + g.getEdadMinima() +
                                           "\nPrecio: $" + g.getPrecio());
            info.setEditable(false);
            panel.add(info, BorderLayout.CENTER);

            JButton btnDownload = new JButton("Descargar");
            btnDownload.addActionListener(e -> {
                boolean ok = steam.downloadGame(g.getCode(), codePlayer, String.valueOf(g.getSistemaOperativo()));
                if (ok) JOptionPane.showMessageDialog(this, "Descarga completada");
                else JOptionPane.showMessageDialog(this, "No se pudo descargar el juego");
            });

            panel.add(btnDownload, BorderLayout.EAST);

            catalogPanel.add(panel);
        }

        catalogPanel.revalidate();
        catalogPanel.repaint();
    }
}
