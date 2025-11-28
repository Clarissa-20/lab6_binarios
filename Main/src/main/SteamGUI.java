/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author Nathan
 */

/**
 * GUI principal de Steam
 */
public class SteamGUI extends JFrame {

    private Steam steam;
    private Player usuarioActual;

    private JTabbedPane tabs;

    // Paneles para Admin
    private JPanel panelAdminPlayers;
    private JPanel panelAdminGames;
    private JPanel panelAdminReports;

    // Paneles para Usuario Normal
    private JPanel panelUserCatalog;
    private JPanel panelUserProfile;

    public SteamGUI(Steam steam) {
        this.steam = steam;
        setTitle("Steam Simulator");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        loginPanel();
    }

    private void loginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        getContentPane().removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        JLabel lblUser = new JLabel("Usuario:");
        JLabel lblPass = new JLabel("Contraseña:");
        JTextField txtUser = new JTextField(15);
        JPasswordField txtPass = new JPasswordField(15);
        JButton btnLogin = new JButton("Iniciar Sesión");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblUser, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(txtUser, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblPass, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(txtPass, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());
            int code = steam.login(user, pass);
            if(code != -1){
                String tipo = steam.getTipoUsuario(code);
                usuarioActual = getPlayerByCode(code);
                if(tipo.equalsIgnoreCase("admin")){
                    showAdminGUI();
                } else {
                    showUserGUI();
                }
            } else {
                JOptionPane.showMessageDialog(this,"Usuario o contraseña incorrecta");
            }
        });

        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    private Player getPlayerByCode(int code){
        List<Player> players = PlayerReader.readAllPlayers();
        for(Player p: players){
            if(p.getCode() == code) return p;
        }
        return null;
    }

    // ================== ADMIN GUI ===================
    private void showAdminGUI(){
        tabs = new JTabbedPane();
        panelAdminPlayers = new JPanel();
        panelAdminGames = new JPanel();
        panelAdminReports = new JPanel();

        setupAdminPlayers();
        setupAdminGames();
        setupAdminReports();

        tabs.addTab("Players", panelAdminPlayers);
        tabs.addTab("Juegos", panelAdminGames);
        tabs.addTab("Reportes", panelAdminReports);

        getContentPane().removeAll();
        getContentPane().add(tabs);
        revalidate();
        repaint();
    }

    private void setupAdminPlayers(){
        panelAdminPlayers.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(new String[]{"Código","Usuario","Nombre","Tipo","Descargas"},0);
        JTable table = new JTable(model);
        refreshPlayerTable(model);

        JPanel buttonsPanel = new JPanel();

        JButton btnAdd = new JButton("Agregar Player");
        JButton btnEdit = new JButton("Modificar Player");
        JButton btnDelete = new JButton("Eliminar Player");

        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnEdit);
        buttonsPanel.add(btnDelete);

        panelAdminPlayers.add(new JScrollPane(table), BorderLayout.CENTER);
        panelAdminPlayers.add(buttonsPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            Player p = showPlayerForm(null);
            if(p!=null){
                try{
                    PlayerWriter.writeNewPlayer(p);
                    refreshPlayerTable(model);
                } catch(Exception ex){
                    JOptionPane.showMessageDialog(this,"Error agregando player: "+ex.getMessage());
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if(sel==-1) return;
            int code = Integer.parseInt(table.getValueAt(sel,0).toString());
            Player pOld = getPlayerByCode(code);
            Player pNew = showPlayerForm(pOld);
            if(pNew!=null){
                // Modificar archivo reemplazando
                updatePlayer(pNew);
                refreshPlayerTable(model);
            }
        });

        btnDelete.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if(sel==-1) return;
            int code = Integer.parseInt(table.getValueAt(sel,0).toString());
            int resp = JOptionPane.showConfirmDialog(this,"¿Eliminar player con código "+code+"?","Confirmar",JOptionPane.YES_NO_OPTION);
            if(resp==JOptionPane.YES_OPTION){
                deletePlayer(code);
                refreshPlayerTable(model);
            }
        });
    }

    private void refreshPlayerTable(DefaultTableModel model){
        model.setRowCount(0);
        List<Player> players = PlayerReader.readAllPlayers();
        for(Player p: players){
            model.addRow(new Object[]{p.getCode(),p.getUsername(),p.getNombre(),p.getTipoUsuario(),p.getContadorDownloads()});
        }
    }

    private Player showPlayerForm(Player p){
        JTextField txtUser = new JTextField();
        JTextField txtNombre = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        String[] tipos = {"admin","normal"};
        JComboBox<String> cbTipo = new JComboBox<>(tipos);
        JButton btnImg = new JButton("Seleccionar Imagen");
        JLabel lblImg = new JLabel();

        byte[] imagenBytes = null;

        if(p!=null){
            txtUser.setText(p.getUsername());
            txtNombre.setText(p.getNombre());
            txtPass.setText(p.getPassword());
            cbTipo.setSelectedItem(p.getTipoUsuario());
            lblImg.setText(p.getRutaImg());
        }

        btnImg.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("Imagenes", "jpg","png","jpeg"));
            int res = fc.showOpenDialog(this);
            if(res==JFileChooser.APPROVE_OPTION){
                File f = fc.getSelectedFile();
                lblImg.setText(f.getAbsolutePath());
            }
        });

        JPanel panel = new JPanel(new GridLayout(0,2,5,5));
        panel.add(new JLabel("Usuario:")); panel.add(txtUser);
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Contraseña:")); panel.add(txtPass);
        panel.add(new JLabel("Tipo:")); panel.add(cbTipo);
        panel.add(btnImg); panel.add(lblImg);

        int op = JOptionPane.showConfirmDialog(this,panel,"Player Form",JOptionPane.OK_CANCEL_OPTION);
        if(op==JOptionPane.OK_OPTION){
            try{
                String username = txtUser.getText();
                String nombre = txtNombre.getText();
                String password = new String(txtPass.getPassword());
                String tipo = cbTipo.getSelectedItem().toString();
                long nacimiento = Calendar.getInstance().getTimeInMillis(); // temporal
                String rutaImg = lblImg.getText();
                int code = (p==null)? CodeManager.getNextPlayerCode():p.getCode();
                return new Player(code, username,password,nombre,nacimiento,0,rutaImg,tipo);
            } catch(Exception e){
                JOptionPane.showMessageDialog(this,"Error creando player: "+e.getMessage());
            }
        }
        return null;
    }

    private void updatePlayer(Player pNew){
        List<Player> players = PlayerReader.readAllPlayers();
        try{
            File f = new File("steam/players.stm");
            f.delete();
            Archivo.iniciarSistema(); // asegura que codes.stm y directorios existan
            for(Player p: players){
                if(p.getCode()==pNew.getCode()) PlayerWriter.writeNewPlayer(pNew);
                else PlayerWriter.writeNewPlayer(p);
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(this,"Error modificando player: "+e.getMessage());
        }
    }

    private void deletePlayer(int code){
        List<Player> players = PlayerReader.readAllPlayers();
        try{
            File f = new File("steam/players.stm");
            f.delete();
            Archivo.iniciarSistema();
            for(Player p: players){
                if(p.getCode()!=code) PlayerWriter.writeNewPlayer(p);
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(this,"Error eliminando player: "+e.getMessage());
        }
    }

    // ================== Admin Juegos ===================
    private void setupAdminGames(){
        panelAdminGames.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(new String[]{"Código","Título","SO","Edad","Precio","Descargas"},0);
        JTable table = new JTable(model);
        refreshGameTable(model);

        JPanel buttonsPanel = new JPanel();

        JButton btnAdd = new JButton("Agregar Juego");
        JButton btnEdit = new JButton("Modificar Juego");
        JButton btnDelete = new JButton("Eliminar Juego");
        JButton btnChangePrice = new JButton("Cambiar Precio");

        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnEdit);
        buttonsPanel.add(btnDelete);
        buttonsPanel.add(btnChangePrice);

        panelAdminGames.add(new JScrollPane(table), BorderLayout.CENTER);
        panelAdminGames.add(buttonsPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            Game g = showGameForm(null);
            if(g!=null){
                GameWriter.writeNewGame(g);
                refreshGameTable(model);
            }
        });

        btnEdit.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if(sel==-1) return;
            int code = Integer.parseInt(table.getValueAt(sel,0).toString());
            Game gOld = getGameByCode(code);
            Game gNew = showGameForm(gOld);
            if(gNew!=null){
                updateGame(gNew);
                refreshGameTable(model);
            }
        });

        btnDelete.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if(sel==-1) return;
            int code = Integer.parseInt(table.getValueAt(sel,0).toString());
            int resp = JOptionPane.showConfirmDialog(this,"¿Eliminar juego con código "+code+"?","Confirmar",JOptionPane.YES_NO_OPTION);
            if(resp==JOptionPane.YES_OPTION){
                deleteGame(code);
                refreshGameTable(model);
            }
        });

        btnChangePrice.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if(sel==-1) return;
            int code = Integer.parseInt(table.getValueAt(sel,0).toString());
            String precioStr = JOptionPane.showInputDialog(this,"Ingrese nuevo precio:");
            try{
                double newPrice = Double.parseDouble(precioStr);
                steam.updatePriceFor(code,newPrice);
                refreshGameTable(model);
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Precio inválido");
            }
        });
    }

    private void refreshGameTable(DefaultTableModel model){
        model.setRowCount(0);
        List<Game> games = GameReader.readAllGames();
        for(Game g: games){
            model.addRow(new Object[]{g.getCode(),g.getTitulo(),g.getSistemaOperativo(),g.getEdadMinima(),g.getPrecio(),g.getContadorDownloads()});
        }
    }

    private Game getGameByCode(int code){
        List<Game> games = GameReader.readAllGames();
        for(Game g: games){
            if(g.getCode()==code) return g;
        }
        return null;
    }

    private Game showGameForm(Game g){
        JTextField txtTitulo = new JTextField();
        JTextField txtEdad = new JTextField();
        JTextField txtPrecio = new JTextField();
        JComboBox<String> cbSO = new JComboBox<>(new String[]{"W","M","L"});
        JButton btnImg = new JButton("Seleccionar Imagen");
        JLabel lblImg = new JLabel();

        if(g!=null){
            txtTitulo.setText(g.getTitulo());
            txtEdad.setText(""+g.getEdadMinima());
            txtPrecio.setText(""+g.getPrecio());
            cbSO.setSelectedItem(""+g.getSistemaOperativo());
            lblImg.setText(g.getRutaImg());
        }

        btnImg.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("Imagenes","jpg","png","jpeg"));
            int res = fc.showOpenDialog(this);
            if(res==JFileChooser.APPROVE_OPTION){
                File f = fc.getSelectedFile();
                lblImg.setText(f.getAbsolutePath());
            }
        });

        JPanel panel = new JPanel(new GridLayout(0,2,5,5));
        panel.add(new JLabel("Título:")); panel.add(txtTitulo);
        panel.add(new JLabel("Edad mínima:")); panel.add(txtEdad);
        panel.add(new JLabel("Precio:")); panel.add(txtPrecio);
        panel.add(new JLabel("Sistema SO(W/M/L):")); panel.add(cbSO);
        panel.add(btnImg); panel.add(lblImg);

        int op = JOptionPane.showConfirmDialog(this,panel,"Juego Form",JOptionPane.OK_CANCEL_OPTION);
        if(op==JOptionPane.OK_OPTION){
            try{
                String titulo = txtTitulo.getText();
                int edad = Integer.parseInt(txtEdad.getText());
                double precio = Double.parseDouble(txtPrecio.getText());
                char so = cbSO.getSelectedItem().toString().charAt(0);
                String rutaImg = lblImg.getText();
                int code = (g==null)? CodeManager.getNextGameCode():g.getCode();
                return new Game(code,titulo,so,edad,precio,0,rutaImg);
            } catch(Exception e){
                JOptionPane.showMessageDialog(this,"Error creando juego: "+e.getMessage());
            }
        }
        return null;
    }

    private void updateGame(Game gNew){
        List<Game> games = GameReader.readAllGames();
        try{
            File f = new File("steam/games.stm");
            f.delete();
            Archivo.iniciarSistema();
            for(Game g: games){
                if(g.getCode()==gNew.getCode()) GameWriter.writeNewGame(gNew);
                else GameWriter.writeNewGame(g);
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(this,"Error modificando juego: "+e.getMessage());
        }
    }

    private void deleteGame(int code){
        List<Game> games = GameReader.readAllGames();
        try{
            File f = new File("steam/games.stm");
            f.delete();
            Archivo.iniciarSistema();
            for(Game g: games){
                if(g.getCode()!=code) GameWriter.writeNewGame(g);
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(this,"Error eliminando juego: "+e.getMessage());
        }
    }

    // ================== Admin Reports ===================
    private void setupAdminReports(){
        panelAdminReports.setLayout(new BorderLayout());

        JButton btnReportClient = new JButton("Generar Reporte Cliente");
        JButton btnViewDownloads = new JButton("Ver Descargas");

        JPanel panel = new JPanel();
        panel.add(btnReportClient);
        panel.add(btnViewDownloads);

        panelAdminReports.add(panel, BorderLayout.NORTH);

        btnReportClient.addActionListener(e -> {
            String codeStr = JOptionPane.showInputDialog(this,"Código de cliente:");
            try{
                int code = Integer.parseInt(codeStr);
                String filename = "reporte_client_"+code+".txt";
                steam.reportForClient(code,filename);
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Código inválido");
            }
        });

        btnViewDownloads.addActionListener(e -> {
            File dir = new File("steam/downloads");
            File[] files = dir.listFiles();
            StringBuilder sb = new StringBuilder();
            if(files!=null){
                for(File f: files){
                    sb.append(f.getName()).append("\n");
                }
            }
            JOptionPane.showMessageDialog(this,new JScrollPane(new JTextArea(sb.toString())));
        });
    }

    // ================== USER GUI ===================
    private void showUserGUI(){
        tabs = new JTabbedPane();
        panelUserCatalog = new JPanel();
        panelUserProfile = new JPanel();

        setupUserCatalog();
        setupUserProfile();

        tabs.addTab("Catálogo Juegos", panelUserCatalog);
        tabs.addTab("Mi Perfil", panelUserProfile);

        getContentPane().removeAll();
        getContentPane().add(tabs);
        revalidate();
        repaint();
    }

    private void setupUserCatalog(){
        panelUserCatalog.setLayout(new GridLayout(0,1));
        List<Game> games = GameReader.readAllGames();
        panelUserCatalog.removeAll();

        for(Game g: games){
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lblImg = new JLabel(new ImageIcon(g.getRutaImg()));
            lblImg.setPreferredSize(new Dimension(100,100));
            JLabel lblInfo = new JLabel("<html>Título: "+g.getTitulo()+"<br>SO: "+g.getSistemaOperativo()+"<br>Edad: "+g.getEdadMinima()+"<br>Precio: "+g.getPrecio()+"</html>");
            JButton btnDownload = new JButton("Descargar");

            btnDownload.addActionListener(e -> {
                int edadUsuario = (int)((Calendar.getInstance().getTimeInMillis() - usuarioActual.getNacimiento()) / 3.154e10);
                if(edadUsuario<g.getEdadMinima()){
                    JOptionPane.showMessageDialog(this,"No cumple la edad mínima para este juego.");
                    return;
                }
                DownloadManager.processDownload(usuarioActual,g);
            });

            p.add(lblImg);
            p.add(lblInfo);
            p.add(btnDownload);

            panelUserCatalog.add(p);
        }
        panelUserCatalog.revalidate();
        panelUserCatalog.repaint();
    }

    private void setupUserProfile(){
        panelUserProfile.setLayout(new GridLayout(0,2,5,5));

        JTextField txtUser = new JTextField(usuarioActual.getUsername());
        JTextField txtNombre = new JTextField(usuarioActual.getNombre());
        JPasswordField txtPass = new JPasswordField(usuarioActual.getPassword());
        JButton btnImg = new JButton("Seleccionar Imagen");
        JLabel lblImg = new JLabel(usuarioActual.getRutaImg());
        JLabel lblDownloads = new JLabel("Descargas: "+usuarioActual.getContadorDownloads());

        btnImg.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("Imagenes","jpg","png","jpeg"));
            int res = fc.showOpenDialog(this);
            if(res==JFileChooser.APPROVE_OPTION){
                File f = fc.getSelectedFile();
                lblImg.setText(f.getAbsolutePath());
                usuarioActual.setRutaImg(f.getAbsolutePath());
                updatePlayer(usuarioActual);
            }
        });

        JButton btnSave = new JButton("Guardar Cambios");
        btnSave.addActionListener(e -> {
            usuarioActual.setUsername(txtUser.getText());
            usuarioActual.setNombre(txtNombre.getText());
            usuarioActual.setPassword(new String(txtPass.getPassword()));
            updatePlayer(usuarioActual);
            JOptionPane.showMessageDialog(this,"Perfil actualizado");
        });

        panelUserProfile.add(new JLabel("Usuario:")); panelUserProfile.add(txtUser);
        panelUserProfile.add(new JLabel("Nombre:")); panelUserProfile.add(txtNombre);
        panelUserProfile.add(new JLabel("Contraseña:")); panelUserProfile.add(txtPass);
        panelUserProfile.add(btnImg); panelUserProfile.add(lblImg);
        panelUserProfile.add(lblDownloads); panelUserProfile.add(btnSave);
    }
}