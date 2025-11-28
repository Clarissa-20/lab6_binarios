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
public class AdminFrame extends JFrame {

    private Steam steam;

    public AdminFrame(Steam steam) {
        this.steam = steam;

        setTitle("Admin Panel");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 5, 5));

        JButton btnAddGame = new JButton("Agregar Juego");
        JButton btnUpdatePrice = new JButton("Actualizar Precio Juego");
        JButton btnPrintGames = new JButton("Ver Todos los Juegos");
        JButton btnLogout = new JButton("Cerrar Sesión");

        add(btnAddGame);
        add(btnUpdatePrice);
        add(btnPrintGames);
        add(btnLogout);

        btnAddGame.addActionListener(e -> agregarJuego());
        btnUpdatePrice.addActionListener(e -> actualizarPrecio());
        btnPrintGames.addActionListener(e -> steam.printGames());
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame(steam).setVisible(true);
        });
    }

    private void agregarJuego() {
        String titulo = JOptionPane.showInputDialog("Título:");
        char so = JOptionPane.showInputDialog("SO (W/M/L):").charAt(0);
        int edadMin = Integer.parseInt(JOptionPane.showInputDialog("Edad mínima:"));
        double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));
        String rutaImg = JOptionPane.showInputDialog("Ruta Imagen:");

        boolean ok = steam.addGame(titulo, so, edadMin, precio, rutaImg);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Juego agregado correctamente.");
        }
    }

    private void actualizarPrecio() {
        int code = Integer.parseInt(JOptionPane.showInputDialog("Código del juego:"));
        double precio = Double.parseDouble(JOptionPane.showInputDialog("Nuevo precio:"));
        if (steam.updatePriceFor(code, precio)) {
            JOptionPane.showMessageDialog(this, "Precio actualizado.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar.");
        }
    }
}
