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
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CodeManager {

    private static final String fileCodes = "steam/codes.stm";

    private static final int posGame = 0;
    private static final int posPlayer = 1;
    private static final int posDonwLoad = 2;

    private static final int intSizeBytes = 4;

    public static int[] readCodes() throws IOException {
        int[] codes = new int[3];
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileCodes))) {
            codes[posGame] = dis.readInt();
            codes[posPlayer] = dis.readInt();
            codes[posDonwLoad] = dis.readInt();
        }
        return codes;
    }

    public static int getNextCodeAndUpdate(int type) throws IOException {
        int[] codes = readCodes();
        int nextCode = codes[type];

        codes[type] = nextCode + 1;

        writeAllCodes(codes);

        return nextCode; 
    }

    private static void writeAllCodes(int[] codes) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileCodes, false))) {
            dos.writeInt(codes[posGame]);
            dos.writeInt(codes[posPlayer]);
            dos.writeInt(codes[posDonwLoad]);
        }
    }

    public static int getNextGameCode() throws IOException {
        return getNextCodeAndUpdate(posGame);
    }

    public static int getNextPlayerCode() throws IOException {
        return getNextCodeAndUpdate(posPlayer);
    }

    public static int getNextDownloadCode() throws IOException {
        return getNextCodeAndUpdate(posDonwLoad);
    }
}
