/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author HP
 */
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;

public class DownloadManager {

    private static final String dirDownloads = "steam/downloads";
    
    public static void processDownload(Player player, Game game) {
        int downloadCode = -1;
        try {
            downloadCode = CodeManager.getNextDownloadCode();
            
            createDownloadFile(downloadCode, player, game);
            player.setContadorDownloads(player.getContadorDownloads() + 1);
            game.setContadorDownloads(game.getContadorDownloads() + 1);
            
            JOptionPane.showMessageDialog(null, "Descarga #" + downloadCode + " registrada con éxito.");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR al procesar la descarga #" + downloadCode);
            e.printStackTrace();
        }
    }

    private static void createDownloadFile(int code, Player player, Game game) throws IOException {
        String fileName = dirDownloads + "/download_" + code + ".stm";
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.println("--- REGISTRO DE DESCARGA STEAM ---");
            pw.println("CODIGO_DESCARGA: " + code);
            pw.println("FECHA_HORA: " + LocalDateTime.now());
            pw.println("---------------------------------");
            pw.println("JUEGO_CODIGO: " + game.getCode());
            pw.println("JUEGO_TITULO: " + game.getTitulo());
            pw.println("---------------------------------");
            pw.println("JUGADOR_CODIGO: " + player.getCode());
            pw.println("JUGADOR_USERNAME: " + player.getUsername());
            pw.println("---------------------------------");
            
            JOptionPane.showMessageDialog(null, "Archivo de registro: " + fileName);
        }
    }
}
