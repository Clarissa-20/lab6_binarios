/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author HP
 */
public class Player {
    private int code;
    private String username;
    private String password;
    private String nombre;
    private long nacimiento;
    private int contadorDownloads;
    private String rutaImg;
    private String tipoUsuario;

    public Player(int code, String username, String password, String nombre, long nacimiento, int contadorDownloads, String rutaImg, String tipoUsuario) {
        this.code = code;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.contadorDownloads = contadorDownloads;
        this.rutaImg = rutaImg;
        this.tipoUsuario = tipoUsuario;
    }

    public int getCode() {
        return code;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNombre() {
        return nombre;
    }

    public long getNacimiento() {
        return nacimiento;
    }

    public int getContadorDownloads() {
        return contadorDownloads;
    }

    public String getRutaImg() {
        return rutaImg;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNacimiento(long nacimiento) {
        this.nacimiento = nacimiento;
    }

    public void setContadorDownloads(int contadorDownloads) {
        this.contadorDownloads = contadorDownloads;
    }

    public void setRutaImg(String rutaImg) {
        this.rutaImg = rutaImg;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
    
}
