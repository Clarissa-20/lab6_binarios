/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author HP
 */
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;

public class PlayerWriter {

    private static final String filePlayers = "steam/players.stm";

    public static void writeNewPlayer(Player newPlayer) throws IOException {

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePlayers, true))) {

            dos.writeInt(newPlayer.getCode());
            dos.writeUTF(newPlayer.getUsername()); 
            dos.writeUTF(newPlayer.getPassword());
            dos.writeUTF(newPlayer.getNombre());
            dos.writeLong(newPlayer.getNacimiento());
            dos.writeInt(newPlayer.getContadorDownloads());
            dos.writeUTF(newPlayer.getRutaImg());
            dos.writeUTF(newPlayer.getTipoUsuario());
            
            JOptionPane.showMessageDialog(null, "Jugador con código " + newPlayer.getCode() + " añadido a players.stm.");
        }
    }
}
