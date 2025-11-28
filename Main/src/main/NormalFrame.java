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

        setTitle("Steam - Catálogo de Juegos");
        setSize(1000, 700); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        add(createHeaderPanel(), BorderLayout.NORTH); 
        
        initComponents();
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(27, 40, 56)); 
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel title = new JLabel("CATÁLOGO DE JUEGOS");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(title, BorderLayout.WEST);
        
        return header;
    }

    private void initComponents() {
        catalogPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20)); 
        catalogPanel.setBackground(new Color(23, 26, 33)); 
        
        JScrollPane scroll = new JScrollPane(catalogPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder()); 
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scroll, BorderLayout.CENTER);

        mostrarCatalogo();
    }

    private void mostrarCatalogo() {
        catalogPanel.removeAll();
        ArrayList<Game> juegos = steam.getAllGames();

        for (Game g : juegos) {
            JPanel cardPanel = createGameCard(g); 
            catalogPanel.add(cardPanel);
        }

        catalogPanel.revalidate();
        catalogPanel.repaint();
    }
    
    private JPanel createGameCard(Game g) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(300, 250));
        panel.setBackground(new Color(42, 71, 94)); 
        panel.setBorder(BorderFactory.createLineBorder(new Color(102, 192, 244), 1)); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER;
        
        ImageIcon originalIcon = new ImageIcon(g.getRutaImg());
        Image scaledImage = originalIcon.getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH);
        JLabel lblImg = new JLabel(new ImageIcon(scaledImage));
        panel.add(lblImg, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JLabel lblTitle = new JLabel(g.getTitulo());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);
        panel.add(lblTitle, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        String infoText = String.format("<html>SO: %c<br>Edad: %d<br>Descargas: %d</html>", 
                                        g.getSistemaOperativo(), 
                                        g.getEdadMinima(), 
                                        g.getContadorDownloads());
        JLabel lblInfo = new JLabel(infoText);
        lblInfo.setForeground(new Color(182, 196, 209)); // Gris claro
        panel.add(lblInfo, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblPrice = new JLabel(String.format("$%.2f", g.getPrecio()));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrice.setForeground(new Color(155, 206, 24)); 
        panel.add(lblPrice, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        
        JButton btnDownload = new JButton("Descargar");
        btnDownload.setBackground(new Color(102, 192, 244));
        btnDownload.setForeground(Color.BLACK);
        btnDownload.setFocusPainted(false);
        btnDownload.addActionListener(e -> {
            boolean ok = steam.downloadGame(g.getCode(), codePlayer, String.valueOf(g.getSistemaOperativo()));
            if (ok) JOptionPane.showMessageDialog(this, "Descarga completada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            else JOptionPane.showMessageDialog(this, "No se pudo descargar el juego", "Error", JOptionPane.ERROR_MESSAGE);
        });

        panel.add(btnDownload, gbc);
        
        return panel;
    }
    
}