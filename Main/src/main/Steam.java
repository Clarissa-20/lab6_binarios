/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Nathan
 */
public class Steam {

    private RandomAccessFile codesFile;
    private RandomAccessFile gamesFile;
    private RandomAccessFile playerFile;

    public Steam() {
        try {
            crearCarpetas();
            inicializarArchivos();
        } catch (Exception e) {
            mostrarError("Error al inicializar Steam: " + e.getMessage());
        }
    }

    public RandomAccessFile getPlayerFile() {
        return playerFile;
    }

    public RandomAccessFile getGamesFile() {
        return gamesFile;
    }

    // ------------------ LOGIN Y TIPOS ------------------
    public int login(String username, String password) {
        try {
            playerFile.seek(0);
            while (playerFile.getFilePointer() < playerFile.length()) {
                long pos = playerFile.getFilePointer();
                int code = playerFile.readInt();
                String user = playerFile.readUTF();
                String pass = playerFile.readUTF();
                playerFile.readUTF(); // nombre
                playerFile.readLong(); // nacimiento
                playerFile.readInt(); // contador
                int lenImg = playerFile.readInt();
                playerFile.skipBytes(lenImg);
                String tipo = playerFile.readUTF();

                if (user.equals(username) && pass.equals(password)) {
                    return code;
                }
            }
        } catch (Exception e) {
            mostrarError("Error login: " + e.getMessage());
        }
        return -1;
    }

    public String getTipoUsuario(int codePlayer) {
        try {
            long pos = buscarPlayer(codePlayer);
            if (pos != -1) {
                playerFile.seek(pos);
                playerFile.readInt();
                playerFile.readUTF();
                playerFile.readUTF();
                playerFile.readUTF();
                playerFile.readLong();
                playerFile.readInt();
                int lenImg = playerFile.readInt();
                playerFile.skipBytes(lenImg);
                return playerFile.readUTF();
            }
        } catch (Exception e) {
            mostrarError("Error obteniendo tipo usuario: " + e.getMessage());
        }
        return null;
    }

    // ------------------ CREAR DIRECTORIOS Y ARCHIVOS ------------------
    private void crearCarpetas() {
        File carpetaSteam = new File("steam");
        File carpetaDownloads = new File("steam/downloads");

        if (!carpetaSteam.exists()) {
            carpetaSteam.mkdir();
        }
        if (!carpetaDownloads.exists()) {
            carpetaDownloads.mkdir();
        }
    }

    private void inicializarArchivos() {
        try {
            File codes = new File("steam/codes.stm");
            File games = new File("steam/games.stm");
            File players = new File("steam/player.stm");

            codesFile = new RandomAccessFile(codes, "rw");
            gamesFile = new RandomAccessFile(games, "rw");
            playerFile = new RandomAccessFile(players, "rw");

            if (codes.length() == 0) {
                codesFile.writeInt(1); // games
                codesFile.writeInt(1); // players
                codesFile.writeInt(1); // downloads
            }

        } catch (IOException ex) {
            mostrarError("No se pudieron inicializar los archivos.\n" + ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "ERROR DE ARCHIVO", JOptionPane.ERROR_MESSAGE);
    }

    // ------------------ PROXIMO CODIGO ------------------
    private int obtenerProximoCodigo(int tipo) {
        try {
            long pos = tipo * 4;
            codesFile.seek(pos);
            int codigo = codesFile.readInt();
            codesFile.seek(pos);
            codesFile.writeInt(codigo + 1);
            return codigo;
        } catch (IOException ex) {
            mostrarError("Error obteniendo código: " + ex.getMessage());
            return -1;
        }
    }

    public int nextGameCode() {
        return obtenerProximoCodigo(0);
    }

    public int nextPlayerCode() {
        return obtenerProximoCodigo(1);
    }

    public int nextDownloadCode() {
        return obtenerProximoCodigo(2);
    }

    // ------------------ ADD GAME ------------------
    public boolean addGame(String titulo, char sistemaOperativo, int edadMin, double precio, String rutaImg) {
        try {
            int code = nextGameCode();
            gamesFile.seek(gamesFile.length());
            gamesFile.writeInt(code);
            gamesFile.writeUTF(titulo);
            gamesFile.writeChar(Character.toUpperCase(sistemaOperativo));
            gamesFile.writeInt(edadMin);
            gamesFile.writeDouble(precio);
            gamesFile.writeInt(0); // contador downloads
            gamesFile.writeUTF(rutaImg);
            return true;
        } catch (IOException e) {
            mostrarError("No se pudo agregar el juego: " + e.getMessage());
            return false;
        }
    }

    // ------------------ ADD PLAYER ------------------
    public boolean addPlayer(String username, String password, String nombre, long nacimiento, byte[] imagenBytes, String tipo) {
        try {
            int code = nextPlayerCode();
            playerFile.seek(playerFile.length());
            playerFile.writeInt(code);
            playerFile.writeUTF(username);
            playerFile.writeUTF(password);
            playerFile.writeUTF(nombre);
            playerFile.writeLong(nacimiento);
            playerFile.writeInt(0); // contador downloads
            playerFile.writeInt(imagenBytes.length);
            playerFile.write(imagenBytes);
            playerFile.writeUTF(tipo);
            return true;
        } catch (IOException e) {
            mostrarError("No se pudo agregar el jugador: " + e.getMessage());
            return false;
        }
    }

    // ------------------ DOWNLOAD GAME ------------------
    public boolean downloadGame(int codeGame, int codePlayer, String so) {
        try {
            // Buscar el juego por su código
            long posGame = buscarJuego(codeGame);
            if (posGame == -1) {
                return false; // Juego no encontrado
            }

            gamesFile.seek(posGame);
            int codJuego = gamesFile.readInt();
            String titulo = gamesFile.readUTF();
            char sistema = gamesFile.readChar();
            int edadMin = gamesFile.readInt();
            double precio = gamesFile.readDouble();
            int contadorDownloadsJuego = gamesFile.readInt();
            String rutaImg = gamesFile.readUTF();

            // Validar sistema operativo
            if (Character.toUpperCase(sistema) != Character.toUpperCase(so.charAt(0))) {
                return false;
            }

            // Buscar el jugador por su código
            long posPlayer = buscarPlayer(codePlayer);
            if (posPlayer == -1) {
                return false; // Jugador no encontrado
            }

            playerFile.seek(posPlayer);
            int codPl = playerFile.readInt();
            String username = playerFile.readUTF();
            String password = playerFile.readUTF();
            String nombre = playerFile.readUTF();
            long nacimiento = playerFile.readLong();
            int contadorDownloadsPlayer = playerFile.readInt();
            int lenImg = playerFile.readInt();
            playerFile.skipBytes(lenImg);
            String tipo = playerFile.readUTF();

            // Validar edad mínima
            int edad = calcularEdad(nacimiento);
            if (edad < edadMin) {
                return false;
            }

            // Generar nuevo código de descarga
            int codDownload = nextDownloadCode();
            File f = new File("steam/downloads/download_" + codDownload + ".stm");
            try (PrintWriter pw = new PrintWriter(f)) {
                pw.println("FECHA: " + new java.util.Date());
                pw.println("IMAGEN: " + rutaImg);
                pw.println("Download #" + codDownload);
                pw.println(nombre + " ha bajado " + titulo);
                pw.println("Precio: $" + precio);
            }

            // Actualizar contador de descargas en games.stm
            gamesFile.seek(posGame + 4 + titulo.length() * 2 + 2 + 2 + 4); // Ajuste exacto de bytes
            gamesFile.writeInt(contadorDownloadsJuego + 1);

            // Actualizar contador de descargas en player.stm
            playerFile.seek(posPlayer + 4 + username.length() * 2 + 2 + password.length() * 2 + 2 + nombre.length() * 2 + 2 + 8);
            playerFile.writeInt(contadorDownloadsPlayer + 1);

            return true;

        } catch (Exception e) {
            mostrarError("Error en downloadGame: " + e.getMessage());
            return false;
        }
    }

    // ------------------ UPDATE PRICE ------------------
    public boolean updatePriceFor(int code, double newPrice) {
        try {
            long pos = buscarJuego(code);
            if (pos == -1) {
                return false;
            }
            gamesFile.seek(pos);
            gamesFile.readInt(); // code
            gamesFile.readUTF(); // titulo
            gamesFile.readChar(); // SO
            gamesFile.readInt(); // edadMin
            gamesFile.writeDouble(newPrice);
            return true;
        } catch (IOException e) {
            mostrarError("Error al actualizar precio: " + e.getMessage());
            return false;
        }
    }

    // ------------------ REPORT CLIENT ------------------
    public void reportForClient(int codePlayer, String filename) {
        try {
            long pos = buscarPlayer(codePlayer);
            if (pos == -1) {
                JOptionPane.showMessageDialog(null, "NO SE PUEDE CREAR REPORTE");
                return;
            }

            playerFile.seek(pos);
            int code = playerFile.readInt();
            String username = playerFile.readUTF();
            String password = playerFile.readUTF();
            String nombre = playerFile.readUTF();
            long nacimiento = playerFile.readLong();
            int descargas = playerFile.readInt();
            int lenImg = playerFile.readInt();
            playerFile.skipBytes(lenImg);
            String tipo = playerFile.readUTF();

            try (PrintWriter pw = new PrintWriter("steam/" + filename)) {
                pw.println("===== REPORTE DE CLIENTE =====");
                pw.println("Código: " + code);
                pw.println("Usuario: " + username);
                pw.println("Nombre: " + nombre);
                pw.println("Nacimiento: " + new Date(nacimiento));
                pw.println("Descargas: " + descargas);
                pw.println("Tipo: " + tipo);
            }

            JOptionPane.showMessageDialog(null, "REPORTE CREADO");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "NO SE PUEDE CREAR REPORTE");
        }
    }

    // ------------------ PRINT GAMES ------------------
    public void printGames() {
        try {
            gamesFile.seek(0);
            String lista = "";
            while (gamesFile.getFilePointer() < gamesFile.length()) {
                int code = gamesFile.readInt();
                String titulo = gamesFile.readUTF();
                char os = gamesFile.readChar();
                int edad = gamesFile.readInt();
                double precio = gamesFile.readDouble();
                int descargas = gamesFile.readInt();
                String rutaImg = gamesFile.readUTF();

                lista += "Código: " + code
                        + "\nTítulo: " + titulo
                        + "\nSO: " + os
                        + "\nEdad mínima: " + edad
                        + "\nPrecio: $" + precio
                        + "\nDescargas: " + descargas
                        + "\n-------------------------\n";
            }
            JOptionPane.showMessageDialog(null, new javax.swing.JScrollPane(new javax.swing.JTextArea(lista)));
        } catch (IOException e) {
            mostrarError("Error al leer juegos: " + e.getMessage());
        }
    }

    // ------------------ AUXILIARES ------------------
    private long buscarJuego(int codeBuscado) throws IOException {
        gamesFile.seek(0);
        while (gamesFile.getFilePointer() < gamesFile.length()) {
            long pos = gamesFile.getFilePointer();
            int code = gamesFile.readInt();
            String titulo = gamesFile.readUTF();
            char sistema = gamesFile.readChar();
            int edadMin = gamesFile.readInt();
            double precio = gamesFile.readDouble();
            int descargas = gamesFile.readInt();
            String rutaImg = gamesFile.readUTF();
            if (code == codeBuscado) {
                return pos;
            }
        }
        return -1;
    }

    private long buscarPlayer(int codeBuscado) throws IOException {
        playerFile.seek(0);
        while (playerFile.getFilePointer() < playerFile.length()) {
            long pos = playerFile.getFilePointer();
            int code = playerFile.readInt();
            String username = playerFile.readUTF();
            String password = playerFile.readUTF();
            String nombre = playerFile.readUTF();
            long nacimiento = playerFile.readLong();
            int descargas = playerFile.readInt();
            int lenImg = playerFile.readInt();
            playerFile.skipBytes(lenImg);
            String tipo = playerFile.readUTF();
            if (code == codeBuscado) {
                return pos;
            }
        }
        return -1;
    }

    private int calcularEdad(long nacimiento) {
        Calendar calNac = Calendar.getInstance();
        calNac.setTime(new Date(nacimiento));
        Calendar hoy = Calendar.getInstance();
        int edad = hoy.get(Calendar.YEAR) - calNac.get(Calendar.YEAR);
        if (hoy.get(Calendar.MONTH) < calNac.get(Calendar.MONTH)
                || (hoy.get(Calendar.MONTH) == calNac.get(Calendar.MONTH) && hoy.get(Calendar.DAY_OF_MONTH) < calNac.get(Calendar.DAY_OF_MONTH))) {
            edad--;
        }
        return edad;
    }
}
