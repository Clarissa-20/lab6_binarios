/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author HP
 */
public class Game {
    private int code;
    private String titulo;
    private char sistemaOperativo;
    private int edadMinima;
    private double precio;
    private int contadorDownloads;
    private String rutaImg;

    public Game(int code, String titulo, char sistemaOperativo, int edadMinima, double precio, int contadorDownloads, String rutaImg) {
        this.code = code;
        this.titulo = titulo;
        this.sistemaOperativo = sistemaOperativo;
        this.edadMinima = edadMinima;
        this.precio = precio;
        this.contadorDownloads = contadorDownloads;
        this.rutaImg = rutaImg;
    }

    public int getCode() {
        return code;
    }

    public String getTitulo() {
        return titulo;
    }

    public char getSistemaOperativo() {
        return sistemaOperativo;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public double getPrecio() {
        return precio;
    }

    public int getContadorDownloads() {
        return contadorDownloads;
    }

    public String getRutaImg() {
        return rutaImg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setSistemaOperativo(char sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public void setEdadMinima(int edadMinima) {
        this.edadMinima = edadMinima;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setContadorDownloads(int contadorDownloads) {
        this.contadorDownloads = contadorDownloads;
    }

    public void setRutaImg(String rutaImg) {
        this.rutaImg = rutaImg;
    }
    
    
}
