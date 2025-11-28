/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package main;

import javax.swing.*;
import java.io.File;
import java.io.IOException; 

/**
 *
 * @author HP
 */
public class Main {

    public static void main(String[] args) {

        // 1. Inicializar directorios y archivos necesarios
        Archivo.iniciarSistema();

        // 2. Inicializar Steam
        Steam steam = new Steam();

        // 3. Verificar que exista un admin por defecto
        ensureDefaultAdmin(steam);

        // 4. Lanzar GUI
        SwingUtilities.invokeLater(() -> {
            SteamGUI gui = new SteamGUI(steam);
            gui.setVisible(true);
        });
    }

    private static void ensureDefaultAdmin(Steam steam){
        try {
            boolean adminExists = false;
            for(Player p : PlayerReader.readAllPlayers()){
                if(p.getTipoUsuario().equalsIgnoreCase("admin")){
                    adminExists = true;
                    break;
                }
            }

            if(!adminExists){
                // Crear admin por defecto
                int code = CodeManager.getNextPlayerCode();
                long nacimiento = System.currentTimeMillis() - 25L*365*24*3600*1000; // 25 años aprox.
                Player admin = new Player(code,"admin","admin","Administrador",nacimiento,0,"","admin");
                PlayerWriter.writeNewPlayer(admin);
                JOptionPane.showMessageDialog(null,"Se creó usuario admin por defecto:\nUsuario: admin\nContraseña: admin");
            }
        } catch(IOException e){
            JOptionPane.showMessageDialog(null,"Error al crear admin por defecto: "+e.getMessage());
            e.printStackTrace();
        }
    }
}

