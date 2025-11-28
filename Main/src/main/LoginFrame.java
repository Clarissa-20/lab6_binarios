/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Nathan
 */

public class LoginFrame extends JFrame {

    private Steam steam;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame(Steam steam) {
        this.steam = steam;

        setTitle("Steam - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(new JLabel("Usuario:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Iniciar Sesión");
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(e -> login());
    }

    private void login() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        int code = steam.login(user, pass); 
        if (code == -1) {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            return;
        }

        String tipo = steam.getTipoUsuario(code); 
        if (tipo.equalsIgnoreCase("admin")) {
            AdminFrame admin = new AdminFrame(steam);
            admin.setVisible(true);
        } else {
            NormalFrame normal = new NormalFrame(steam, code);
            normal.setVisible(true);
        }

        this.dispose();
    }
}

