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

public class GameReader {

    private static final String fileGame = "steam/games.stm";

    public static List<Game> readAllGames() {
        List<Game> gamesList = new ArrayList<>();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileGame))) {

            while (true) {
                try {
                    Game game = readNextGame(dis);
                    gamesList.add(game);
                } catch (java.io.EOFException e) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("ERROR al leer el archivo " + fileGame + ": " + e.getMessage());
        }

        System.out.println("Se leyeron " + gamesList.size() + " juegos del archivo.");
        return gamesList;
    }

    private static Game readNextGame(DataInputStream dis) throws IOException {
        Game game = new Game();

        game.setCode(dis.readInt());

        game.setTitulo(dis.readUTF());

        game.setSistemaOperativo(dis.readChar());

        game.setEdadMinima(dis.readInt());

        game.setPrecio(dis.readDouble());

        game.setContadorDownloads(dis.readInt());

        game.setRutaImg(dis.readUTF());

        return game;
    }
}
