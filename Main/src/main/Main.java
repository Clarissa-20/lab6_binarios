/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package main;

import javax.swing.*;

/**
 *
 * @author HP
 */

public class Main {
    public static void main(String[] args) {
        // Inicializar Steam
        Steam steam = new Steam();

        // Mostrar ventana de login
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame(steam);
            login.setVisible(true);
        });
    }
}


