/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author HP
 */
public class Archivo {
    public static final String dirSteam = "steam";
    public static final String dirDownloads = dirSteam + File.separator + "downloads";
    public static final String fileCodes = dirSteam + File.separator + "codes.stm";

    public static void iniciarSistema() {
        System.out.println("Iniciando la estructura de datos de archivos del sistema Steam...");

        File steamDir = new File(dirSteam);
        File downloadsDir = new File(dirDownloads);

        if (!steamDir.exists()) {
            if(steamDir.mkdir()){
                System.out.println("Directorio "+dirSteam+" creado con exito");
            } else{
                System.out.println("No se pudo crear el directorio "+dirSteam);
                return;
            }
        } else{
            System.out.println("Directorio "+dirSteam+" ya existe");
        }
        
        if(!downloadsDir.exists()){
            if(downloadsDir.mkdir()){
                System.out.println("Subdirectorio "+dirDownloads+" creado con exito");
            } else{
                System.out.println("No se pudo crear el Subdirectorio "+dirDownloads);
                return;
            }
        } else{
            System.out.println("Subdirectorio "+dirDownloads+" ya existe");
        }
        
        File codesFile = new File(fileCodes);
        if(!codesFile.exists()){
            try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(codesFile)) ){
                dos.writeInt(1); //este para games
                dos.writeInt(1); //este para players
                dos.writeInt(1); //este para downloads
                
                System.out.println("Archivo "+codesFile+" creado  e inicializado en (1, 1, 1)");
                
            } catch(IOException e){
                System.out.println("Error al escribir en "+codesFile);
                e.printStackTrace();
            }
        } else{
            System.out.println("Archivo "+codesFile+" ya existe. No se modifico");
        }
    }
    
    public static void main(String[] args) {
        iniciarSistema();
    }
}
