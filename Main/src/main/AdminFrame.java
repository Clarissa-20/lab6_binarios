/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Nathan
 */

public class AdminFrame extends JFrame {

    private Steam steam;

    private JButton btnAddGame, btnAddPlayer, btnListGames, btnChangePrice, btnGenerateReport;

    public AdminFrame(Steam steam) {
        this.steam = steam;

        setTitle("Steam - Admin");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        btnAddGame = new JButton("Registrar Juego");
        btnAddPlayer = new JButton("Registrar Player");
        btnListGames = new JButton("Ver Lista de Juegos");
        btnChangePrice = new JButton("Cambiar Precio Juego");
        btnGenerateReport = new JButton("Generar Reporte Cliente");

        panel.add(btnAddGame);
        panel.add(btnAddPlayer);
        panel.add(btnListGames);
        panel.add(btnChangePrice);
        panel.add(btnGenerateReport);

        add(panel);

        btnAddGame.addActionListener(e -> registrarJuego());
        btnAddPlayer.addActionListener(e -> registrarPlayer());
        btnListGames.addActionListener(e -> steam.printGames());
        btnChangePrice.addActionListener(e -> cambiarPrecio());
        btnGenerateReport.addActionListener(e -> generarReporte());
    }

    private void registrarJuego() {
        String titulo = JOptionPane.showInputDialog("Título del juego:");
        char so = JOptionPane.showInputDialog("SO (W=Windows, M=Mac, L=Linux):").charAt(0);
        int edad = Integer.parseInt(JOptionPane.showInputDialog("Edad mínima:"));
        double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));
        String rutaImg = JOptionPane.showInputDialog("Ruta imagen:");

        boolean ok = steam.addGame(titulo, so, edad, precio, rutaImg);
        if (ok) JOptionPane.showMessageDialog(this, "Juego agregado correctamente.");
    }

    private void registrarPlayer() {
        String username = JOptionPane.showInputDialog("Username:");
        String password = JOptionPane.showInputDialog("Password:");
        String nombre = JOptionPane.showInputDialog("Nombre:");
        long nacimiento = System.currentTimeMillis(); // simplificación
        String tipo = JOptionPane.showInputDialog("Tipo (admin/normal):");
        byte[] img = new byte[0]; 

        boolean ok = steam.addPlayer(username, password, nombre, nacimiento, img, tipo);
        if (ok) JOptionPane.showMessageDialog(this, "Player agregado correctamente.");
    }

    private void cambiarPrecio() {
        int code = Integer.parseInt(JOptionPane.showInputDialog("Código del juego:"));
        double precio = Double.parseDouble(JOptionPane.showInputDialog("Nuevo precio:"));

        boolean ok = steam.updatePriceFor(code, precio);
        if (ok) JOptionPane.showMessageDialog(this, "Precio actualizado.");
    }

    private void generarReporte() {
        int code = Integer.parseInt(JOptionPane.showInputDialog("Código del cliente:"));
        String filename = JOptionPane.showInputDialog("Nombre archivo reporte:");
        steam.reportForClient(code, filename);
    }
}

