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

public class PlayerReader {

    private static final String FILE_PLAYERS = "steam/players.stm";

    public static List<Player> readAllPlayers() {
        List<Player> playersList = new ArrayList<>();
        
        try (DataInputStream dis = new DataInputStream(new FileInputStream(FILE_PLAYERS))) {
            
            while (true) {
                try {
                    Player player = readNextPlayer(dis);
                    playersList.add(player);
                } catch (java.io.EOFException e) {
                    // Fin del archivo
                    break; 
                }
            }
            
        } catch (IOException e) {
            System.err.println("ERROR al leer el archivo " + FILE_PLAYERS + ": " + e.getMessage());
        }
        
        System.out.println("Se leyeron " + playersList.size() + " jugadores del archivo.");
        return playersList;
    }

    private static Player readNextPlayer(DataInputStream dis) throws IOException {
        Player player = new Player();
        
        // El orden de lectura DEBE coincidir exactamente con el orden de escritura
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
