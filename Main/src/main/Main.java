/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package main;

import javax.swing.*;
import java.util.Calendar;
/**
 *
 * @author HP
 */
public class Main {

    public static void main(String[] args) {


        Steam steam = new Steam();

        if (!existeAdmin(steam)) {
            String username = "admin";
            String password = "admin123";
            String nombre = "Administrador";
            long nacimiento = Calendar.getInstance().getTimeInMillis(); 
            String tipo = "admin"; 
            byte[] imagenVacia = new byte[0];

            boolean creado = steam.addPlayer(username, password, nombre, nacimiento, imagenVacia, tipo);
            if (creado) {
                JOptionPane.showMessageDialog(null, "Usuario ADMIN creado por defecto.\nUsuario: admin\nContraseña: admin123");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo crear el ADMIN por defecto.");
            }
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame(steam);
            login.setVisible(true);
        });
    }

    private static boolean existeAdmin(Steam steam) {
        try {
            steam.getPlayerFile().seek(0);

            while (steam.getPlayerFile().getFilePointer() < steam.getPlayerFile().length()) {
                steam.getPlayerFile().readInt(); 
                steam.getPlayerFile().readUTF(); 
                steam.getPlayerFile().readUTF(); 
                steam.getPlayerFile().readUTF(); 
                steam.getPlayerFile().readLong(); 
                steam.getPlayerFile().readInt();
                int lenImg = steam.getPlayerFile().readInt();
                steam.getPlayerFile().skipBytes(lenImg);
                String tipo = steam.getPlayerFile().readUTF();

                if ("admin".equalsIgnoreCase(tipo)) {
                    return true;
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error verificando admin: " + e.getMessage());
        }

        return false;
    }
}
