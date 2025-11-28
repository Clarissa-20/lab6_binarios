/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author HP
 */
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PlayerReader {

    private static final String filePlayers = "steam/players.stm";

    public static List<Player> readAllPlayers() {
        List<Player> playersList = new ArrayList<>();
        
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePlayers))) {
            
            while (true) {
                try {
                    Player player = readNextPlayer(dis);
                    playersList.add(player);
                } catch (java.io.EOFException e) {
                    break; 
                }
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR al leer el archivo " + filePlayers);
        }
        
        JOptionPane.showMessageDialog(null, "Se leyeron " + playersList.size() + " jugadores del archivo.");
        return playersList;
    }

    private static Player readNextPlayer(DataInputStream dis) throws IOException {
        Player player = new Player();
        
        player.setCode(dis.readInt());
        player.setUsername(dis.readUTF());
        player.setPassword(dis.readUTF());
        player.setNombre(dis.readUTF());
        player.setNacimiento(dis.readLong());
        player.setContadorDownloads(dis.readInt());
        player.setRutaImg(dis.readUTF());
        player.setTipoUsuario(dis.readUTF());
        
        return player;
    }
}
