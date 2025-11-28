/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Nathan
 */
public class UserFrame extends JFrame {

    private Steam steam;
    private int codePlayer;

    public UserFrame(Steam steam, int codePlayer) {
        this.steam = steam;
        this.codePlayer = codePlayer;

        setTitle("Usuario Panel");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 5, 5));

        JButton btnDownload = new JButton("Descargar Juego");
        JButton btnReport = new JButton("Generar Reporte");
        JButton btnViewGames = new JButton("Ver Juegos");
        JButton btnLogout = new JButton("Cerrar Sesión");

        add(btnDownload);
        add(btnReport);
        add(btnViewGames);
        add(btnLogout);

        btnDownload.addActionListener(e -> descargarJuego());
        btnReport.addActionListener(e -> generarReporte());
        btnViewGames.addActionListener(e -> steam.printGames());
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame(steam).setVisible(true);

        });
    }

    private void descargarJuego() {
        int codeGame = Integer.parseInt(JOptionPane.showInputDialog("Código del juego:"));
        char so = JOptionPane.showInputDialog("SO actual (W/M/L):").charAt(0);
        boolean ok = steam.downloadGame(codeGame, codePlayer, so);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Juego descargado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo descargar.");
        }
    }

    private void generarReporte() {
        String filename = JOptionPane.showInputDialog("Nombre del archivo (ej: reporte.txt):");
        steam.reportForClient(codePlayer, filename);
    }
}
