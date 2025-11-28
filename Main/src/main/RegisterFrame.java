/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

/**
 *
 * @author Nathan
 */
public class RegisterFrame extends JFrame {

    private Steam steam;

    private JTextField txtUsername, txtNombre;
    private JPasswordField txtPassword;
    private JComboBox<String> comboTipo;
    private JSpinner spinnerFecha;

    public RegisterFrame(Steam steam) {
        this.steam = steam;

        setTitle("Registrar Usuario");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Usuario:"));
        txtUsername = new JTextField();
        add(txtUsername);

        add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Fecha Nacimiento (ms):"));
        spinnerFecha = new JSpinner(new SpinnerNumberModel(System.currentTimeMillis(), 0, System.currentTimeMillis(), 1000));
        add(spinnerFecha);

        add(new JLabel("Tipo Usuario:"));
        comboTipo = new JComboBox<>(new String[]{"normal", "admin"});
        add(comboTipo);

        JButton btnRegister = new JButton("Registrar");
        add(btnRegister);

        btnRegister.addActionListener(e -> registrar());
    }

    private void registrar() {
        try {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            String nombre = txtNombre.getText();
            long nacimiento = (long) spinnerFecha.getValue();
            String tipo = (String) comboTipo.getSelectedItem();

            byte[] imgBytes = new byte[0]; // Sin imagen por ahora

            boolean ok = steam.addPlayer(user, pass, nombre, nacimiento, imgBytes, tipo);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
