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
import static main.Archivo.dirSteam;

public class GameWriter {

    private static final String fileGame = "steam/games.stm";

    public static void writeNewGame(Game newGame) {

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileGame, true))) {

            dos.writeInt(newGame.getCode());

            dos.writeUTF(newGame.getTitulo());

            dos.writeChar(newGame.getSistemaOperativo());
            dos.writeInt(newGame.getEdadMinima());
            dos.writeDouble(newGame.getPrecio());
            dos.writeInt(newGame.getContadorDownloads());
            dos.writeUTF(newGame.getRutaImg());
            
            JOptionPane.showMessageDialog(null, "Juego con código " + newGame.getCode() + " añadido a games.stm.");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR al escribir el nuevo juego: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
