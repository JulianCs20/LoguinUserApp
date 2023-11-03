/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ventanas;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import ventanas.Usuario;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
 

/**
 *
 * @author HOME
 */
/**
 * Ventana de inicio de sesión y registro.
 */
public class VentanaLogin extends javax.swing.JFrame {
    
    private Timer timer;
     // Define una lista de usuarios. 
    // Nota: Considera cambiar esto a un objeto de usuario o usar una base de datos.
    private ArrayList<Usuario> listaUsuarios;
    private boolean correoRegistrado = false;
    private static final String ARCHIVO_USUARIOS = "usuarios.txt";

    public VentanaLogin() {
        initComponents();
         // Aquí podrías inicializar tu lista de usuarios desde una base de datos o archivo.
        //listaUsuarios = cargarUsuariosDesdeArchivo("usuarios.txt");
  
        jPanel5.setVisible(false);
        try {
            // Establece el aspecto de la interfaz de usuario
            UIManager.put("Button.arc", 20);
            UIManager.put("TextComponent.arc", 20);
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
            // Manejo de excepciones si falla la configuración del aspecto
        }
        jButton1.putClientProperty("JComponent.roundRect", true);
        jButton2.putClientProperty("JComponent.roundRect", true);
        listaUsuarios = (ArrayList<Usuario>) Usuario.leerUsuarios(ARCHIVO_USUARIOS);
        jPanel2.setVisible(false);
        jPanel3.setVisible(false);
        labelerror1.setVisible(false);
        labelerror2.setVisible(false);
        labelerror3.setVisible(false);
        labelerror5.setVisible(false);
        labelerror4.setVisible(false);
        jPanel3.setVisible(false);

        if (!archivoUsuariosExiste(ARCHIVO_USUARIOS)) {
            try {
                crearArchivoUsuarios(ARCHIVO_USUARIOS);
            } catch (IOException e) {
                e.printStackTrace();
                // Manejar errores al crear el archivo
            }
        }
    }

    private boolean archivoUsuariosExiste(String nombreArchivo) {
        // Verifica si el archivo existe
        File archivo = new File(nombreArchivo);
        return archivo.exists();
    }

    private void crearArchivoUsuarios(String nombreArchivo) throws IOException {
        // Crea el archivo "usuarios.txt" si no existe
        BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo));
        writer.close();
    }

    /**
     * Registra un nuevo usuario.
     */
    private void registrarUsuario() {
        // Oculta los mensajes de error
        labelerror1.setVisible(false);
        labelerror2.setVisible(false);
        labelerror3.setVisible(false);

        // Obtiene los datos ingresados por el usuario
        String email = CajaRegistro.getText();
        String password = new String(CajaContraseña2.getPassword());
        String confirmPassword = new String(CajaConfirmarC.getPassword());

         // Verifica si los campos están vacíos, si el correo es válido, si la contraseña tiene al menos 8 caracteres,
    // si las contraseñas coinciden y si el usuario aceptó los términos y condiciones.
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            labelerror1.setVisible(true);
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            labelerror1.setVisible(true);
        } else if (password.length() < 8) {
            labelerror2.setVisible(true);
        } else if (!password.equals(confirmPassword)) {
            labelerror2.setVisible(true);
        } else if (!CheckTerminos.isSelected()) {
            labelerror2.setText("Debes aceptar los términos y condiciones para registrarte.");
            labelerror2.setVisible(true);
        } else {
            // Los datos son válidos, puedes crear la cuenta del usuario
            Usuario nuevoUsuario = new Usuario(email, password);
            listaUsuarios.add(nuevoUsuario);
            labelerror3.setVisible(true);
            CheckTerminos.setVisible(true);

            // Guarda la lista de usuarios en el archivo
            Usuario.escribirUsuarios(listaUsuarios, "usuarios.txt");

            // Cambia la visibilidad de los paneles para volver al "Panel 1"
            jPanel1.setVisible(true);
            jPanel2.setVisible(false);
        }
    }

    /**
     * Inicia sesión con un usuario existente.
     */
    private void iniciarSesion() {
        labelerror4.setVisible(false); // Oculta los mensajes de error

        String email = CajaLogin.getText();
        String password = new String(CajaContraseña.getPassword());

        // Verifica si el usuario existe en la lista
        boolean usuarioEncontrado = false;
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getEmail().equals(email) && usuario.getPassword().equals(password)) {
                usuarioEncontrado = true;
                break;
            }
        }

        if (usuarioEncontrado) {
            // Inicio de sesión exitoso, puedes realizar acciones como mostrar otra ventana.
            labelerror5.setVisible(true); // Muestra un mensaje de inicio de sesión exitoso

            // Aquí puedes agregar el código para mostrar la barra de progreso y cambiar de panel
            Thread hilo = new Thread() {
                @Override
                public void run() {
                    System.out.println("Mostrando barra de progreso...");
                    jPanel5.setVisible(true);
                    for (int i = 1; i <= 100; i++) {
                        try {
                            Thread.sleep(10);
                            jProgressBar1.setValue(i);
                        } catch (InterruptedException e) {
                        }
                    }
                    System.out.println("Ocultando barra de progreso y mostrando jPanel6...");
                    jPanel5.setVisible(false);
                    jProgressBar1.setVisible(false);
                    jPanel1.setVisible(false);
                    jPanel6.setVisible(true);
                }
            };
            hilo.start();
        } else {
            // Credenciales incorrectas
            labelerror4.setVisible(true);
        }
    }

    /**
     * Recupera la contraseña de un usuario.
     */
    private void recuperarContraseña() {
        // Obtiene el correo electrónico ingresado por el usuario
        String emailRecuperar = CajaRecuperarC.getText();
        
        // Verifica si el correo electrónico no está vacío
        if (emailRecuperar != null && !emailRecuperar.isEmpty()) {
            boolean encontrado = false;
            // Busca el correo electrónico en la lista de usuarios
            for (Usuario usuario : listaUsuarios) {
                if (usuario.getEmail().equals(emailRecuperar)) {
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                // El correo electrónico se encuentra registrado
                correoRegistrado = true;
                jPanel4.setVisible(true);

                // Muestra los componentes para cambiar la contraseña
                CajaRecuperarContra.setVisible(true);
                BotomRecuperar.setVisible(true);

                // Resto del código para recuperación de contraseña
            } else {
                correoRegistrado = false;
                jLabel6.setText("El correo electrónico ingresado no se encuentra registrado.");
                jLabel6.setVisible(true); // Muestra el mensaje de error
            }
        }
    }
/**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        LabelLogo = new javax.swing.JLabel();
        LabelNameLogo = new javax.swing.JLabel();
        LabelEmailLogin = new javax.swing.JLabel();
        LabelIniciarSesion = new javax.swing.JLabel();
        LabelContraseña = new javax.swing.JLabel();
        CajaLogin = new javax.swing.JTextField();
        CajaContraseña = new javax.swing.JPasswordField();
        LabelRegistrarse = new javax.swing.JLabel();
        LabelOlvidasteC = new javax.swing.JLabel();
        labelerror4 = new javax.swing.JLabel();
        labelerror5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        LabelRegistrarse2 = new javax.swing.JLabel();
        LabelEmail2 = new javax.swing.JLabel();
        CajaRegistro = new javax.swing.JTextField();
        LabelContraseña2 = new javax.swing.JLabel();
        CajaContraseña2 = new javax.swing.JPasswordField();
        jLabel12 = new javax.swing.JLabel();
        CajaConfirmarC = new javax.swing.JPasswordField();
        LabelTerminos = new javax.swing.JLabel();
        CheckTerminos = new javax.swing.JCheckBox();
        LabelLogo1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        labelerror1 = new javax.swing.JLabel();
        labelerror2 = new javax.swing.JLabel();
        labelerror3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        CajaRecuperarC = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        CajaRecuperarContra = new javax.swing.JPasswordField();
        BotomRecuperar1 = new javax.swing.JButton();
        BotomRecuperar = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(0, 0));
        setResizable(false);

        labelerror1.setVisible(false);
        labelerror2.setVisible(false);
        labelerror3.setVisible(false);
        jPanel3.setVisible(false);
        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        LabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/1698777567794 (2).png"))); // NOI18N
        LabelLogo.setToolTipText("Logo");

        LabelNameLogo.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 24)); // NOI18N
        LabelNameLogo.setForeground(new java.awt.Color(255, 215, 0));
        LabelNameLogo.setText("Innovatech");
        LabelNameLogo.setToolTipText("Nombre del Logo");

        LabelEmailLogin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LabelEmailLogin.setForeground(new java.awt.Color(255, 215, 0));
        LabelEmailLogin.setText("Email");

        LabelIniciarSesion.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LabelIniciarSesion.setForeground(new java.awt.Color(255, 215, 0));
        LabelIniciarSesion.setText("Iniciar Sesion");

        LabelContraseña.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LabelContraseña.setForeground(new java.awt.Color(255, 215, 0));
        LabelContraseña.setText("Contraseña");

        CajaLogin.setBackground(new java.awt.Color(0, 0, 0));
        CajaLogin.setForeground(new java.awt.Color(255, 215, 0));
        CajaLogin.setToolTipText("Ingresar Correo");
        CajaLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CajaLoginActionPerformed(evt);
            }
        });

        CajaContraseña.setBackground(new java.awt.Color(0, 0, 0));
        CajaContraseña.setForeground(new java.awt.Color(255, 215, 0));
        CajaContraseña.setToolTipText("Ingresar Contraseña");
        CajaContraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CajaContraseñaActionPerformed(evt);
            }
        });

        LabelRegistrarse.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        LabelRegistrarse.setForeground(new java.awt.Color(255, 215, 0));
        LabelRegistrarse.setText("Registrarse");
        LabelRegistrarse.setToolTipText("Boton para registrase");
        LabelRegistrarse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LabelRegistrarseMouseClicked(evt);
            }
        });

        LabelOlvidasteC.setForeground(new java.awt.Color(255, 215, 0));
        LabelOlvidasteC.setText("¿Olvidaste tu contraseña?");
        LabelOlvidasteC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LabelOlvidasteCMouseClicked(evt);
            }
        });

        labelerror4.setForeground(new java.awt.Color(255, 215, 0));
        labelerror4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelerror4.setText("Credenciales incorrectas. Verifica tu correo y contraseña.");
        labelerror4.setToolTipText("Aviso, verificar datos");

        labelerror5.setForeground(new java.awt.Color(255, 215, 0));
        labelerror5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelerror5.setText("Inicio de sesión exitoso.");

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 215, 0));
        jButton1.setText("Iniciar Sesion");
        jButton1.setToolTipText("Boton para iniciar sesion");
        jButton1.setBorder(null);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(CajaContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelNameLogo)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(labelerror4, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(LabelContraseña))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CajaLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelEmailLogin)
                            .addComponent(LabelLogo)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(204, 204, 204)
                        .addComponent(LabelIniciarSesion))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(193, 193, 193)
                        .addComponent(labelerror5, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(222, 222, 222)
                        .addComponent(LabelRegistrarse))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(194, 194, 194)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelOlvidasteC)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(LabelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(LabelNameLogo)
                .addGap(18, 18, 18)
                .addComponent(LabelIniciarSesion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelerror5)
                .addGap(27, 27, 27)
                .addComponent(LabelEmailLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CajaLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(LabelContraseña)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CajaContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelerror4)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(LabelRegistrarse)
                .addGap(18, 18, 18)
                .addComponent(LabelOlvidasteC)
                .addGap(63, 63, 63))
        );

        LabelEmailLogin.getAccessibleContext().setAccessibleDescription("Juan es gay");

        labelerror5.setVisible(false);
        labelerror4.setVisible(false);
        jPanel4.setVisible(false);
        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        LabelRegistrarse2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LabelRegistrarse2.setForeground(new java.awt.Color(255, 215, 0));
        LabelRegistrarse2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelRegistrarse2.setText("Registrarse");

        LabelEmail2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LabelEmail2.setForeground(new java.awt.Color(255, 215, 0));
        LabelEmail2.setText("Email");

        CajaRegistro.setBackground(new java.awt.Color(0, 0, 0));
        CajaRegistro.setForeground(new java.awt.Color(255, 215, 0));
        CajaRegistro.setToolTipText("Ingresar correo");
        CajaRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CajaRegistroActionPerformed(evt);
            }
        });

        LabelContraseña2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LabelContraseña2.setForeground(new java.awt.Color(255, 215, 0));
        LabelContraseña2.setText("Contraseña");

        CajaContraseña2.setBackground(new java.awt.Color(0, 0, 0));
        CajaContraseña2.setForeground(new java.awt.Color(255, 215, 0));
        CajaContraseña2.setToolTipText("Ingresar contraseña");
        CajaContraseña2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CajaContraseña2ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 215, 0));
        jLabel12.setText("Confirmar contraseña");

        CajaConfirmarC.setBackground(new java.awt.Color(0, 0, 0));
        CajaConfirmarC.setForeground(new java.awt.Color(255, 215, 0));
        CajaConfirmarC.setToolTipText("Ingresar contraseña nuevamente");

        LabelTerminos.setForeground(new java.awt.Color(255, 215, 0));
        LabelTerminos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelTerminos.setText("<html><body style='width: 150px'>Acepto los Términos de servicios y la Política de privacidad. </body></html>");
        LabelTerminos.setToolTipText("Terminos y condiciones");

        CheckTerminos.setBackground(new java.awt.Color(0, 0, 0));
        CheckTerminos.setForeground(new java.awt.Color(102, 255, 51));
        CheckTerminos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckTerminosActionPerformed(evt);
            }
        });

        LabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/1698777567794 (2).png"))); // NOI18N

        jLabel2.setForeground(new java.awt.Color(255, 215, 0));
        jLabel2.setText("¿Tienes una cuenta? ");
        jLabel2.setToolTipText("Si tiene una cuenta creada, Iniciar sesion");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 215, 0));
        jLabel3.setText("Iniciar Sesion");
        jLabel3.setToolTipText("Boton para iniciar sesion");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        labelerror1.setForeground(new java.awt.Color(255, 215, 0));
        labelerror1.setText("Todos los campos son obligatorios.");
        labelerror1.setToolTipText("Aviso, verificar datos");

        labelerror2.setForeground(new java.awt.Color(255, 215, 0));
        labelerror2.setText("Las contraseñas no coinciden.");
        labelerror2.setToolTipText("Aviso, verificar datos");

        labelerror3.setForeground(new java.awt.Color(255, 215, 0));
        labelerror3.setText("Cuenta creada exitosamente.");

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setForeground(new java.awt.Color(255, 215, 0));
        jButton2.setText("Crear Cuenta");
        jButton2.setToolTipText("Boton para crear cuenta");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(LabelContraseña2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(CheckTerminos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LabelTerminos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(LabelLogo1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelerror3)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(labelerror1)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(LabelEmail2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CajaRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                                    .addComponent(CajaContraseña2)
                                    .addComponent(CajaConfirmarC)
                                    .addComponent(jLabel12))))
                        .addGap(25, 196, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(213, 213, 213)
                        .addComponent(LabelRegistrarse2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addComponent(jButton2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(151, 151, 151)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(179, 179, 179)
                        .addComponent(labelerror2)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelLogo1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelRegistrarse2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelerror3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelerror1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LabelEmail2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CajaRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(LabelContraseña2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CajaContraseña2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CajaConfirmarC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelerror2)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(CheckTerminos)
                        .addGap(48, 48, 48))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(LabelTerminos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addGap(6, 6, 6)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setVisible(false);
        jPanel4.setVisible(false);
        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 215, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Recupera tu cuenta");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 215, 0));
        jLabel4.setText("Email");

        CajaRecuperarC.setBackground(new java.awt.Color(0, 0, 0));
        CajaRecuperarC.setForeground(new java.awt.Color(255, 215, 0));
        CajaRecuperarC.setToolTipText("Ingresar correo");
        CajaRecuperarC.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 215, 0)));
        CajaRecuperarC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CajaRecuperarCActionPerformed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 215, 0));
        jLabel5.setText("Escriba el email con el que registro su cuenta para la recuperacion.");

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setForeground(new java.awt.Color(255, 215, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("El correo electrónico ingresado no se encuentra registrado.");
        jLabel6.setToolTipText("Aviso, verificar datos");

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setForeground(new java.awt.Color(255, 215, 0));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 215, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Nueva contraseña");

        jLabel8.setBackground(new java.awt.Color(0, 0, 0));
        jLabel8.setForeground(new java.awt.Color(255, 215, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Contraseña cambiada con exito");

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setForeground(new java.awt.Color(255, 215, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Debe ingresar una nueva contraseña válida de al menos 8 caracteres.");
        jLabel9.setToolTipText("Aviso, verificar datos");

        CajaRecuperarContra.setBackground(new java.awt.Color(0, 0, 0));
        CajaRecuperarContra.setForeground(new java.awt.Color(255, 215, 0));
        CajaRecuperarContra.setToolTipText("Ingresar nueva contraseña");
        CajaRecuperarContra.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 215, 0), 1, true));

        BotomRecuperar1.setBackground(new java.awt.Color(0, 0, 0));
        BotomRecuperar1.setForeground(new java.awt.Color(255, 215, 0));
        BotomRecuperar1.setText("Cambiar");
        BotomRecuperar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BotomRecuperar1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(213, 213, 213))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(191, 191, 191)
                        .addComponent(jLabel8))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jLabel9))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(232, 232, 232)
                        .addComponent(BotomRecuperar1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(182, 182, 182)
                        .addComponent(CajaRecuperarContra, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jLabel7)
                .addGap(16, 16, 16)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(CajaRecuperarContra, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BotomRecuperar1)
                .addContainerGap(160, Short.MAX_VALUE))
        );

        BotomRecuperar.setBackground(new java.awt.Color(0, 0, 0));
        BotomRecuperar.setForeground(new java.awt.Color(255, 215, 0));
        BotomRecuperar.setText("Recuperar");
        BotomRecuperar.setToolTipText("Boton para recuperar contraseña");
        BotomRecuperar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BotomRecuperarMouseClicked(evt);
            }
        });
        BotomRecuperar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotomRecuperarActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/volver.png"))); // NOI18N
        jButton3.setToolTipText("Volver a Iniciar Sesion");
        jButton3.setBorder(null);
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(108, 108, 108)
                                .addComponent(jLabel1)
                                .addGap(140, 140, 140)))
                        .addGap(71, 71, 71))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(225, 225, 225)
                        .addComponent(BotomRecuperar))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(178, 178, 178)
                        .addComponent(CajaRecuperarC, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(250, 250, 250)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton3)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(22, 22, 22)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CajaRecuperarC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(BotomRecuperar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
        );

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setForeground(new java.awt.Color(0, 0, 0));
        jPanel5.setOpaque(false);

        jProgressBar1.setBackground(new java.awt.Color(0, 0, 0));
        jProgressBar1.setForeground(new java.awt.Color(255, 255, 0));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(171, 171, 171)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(195, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(142, 142, 142)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(125, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 0));
        jLabel10.setText("Sesion Iniciada");
        jPanel6.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 270, 180, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CajaRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CajaRegistroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CajaRegistroActionPerformed

    private void CajaContraseña2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CajaContraseña2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CajaContraseña2ActionPerformed

    private void CheckTerminosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckTerminosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CheckTerminosActionPerformed
    /**
 * Este método se llama cuando se hace clic en jLabel3.
 * Muestra el jPanel1 y oculta el jPanel2.
 */
    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
   jPanel1.setVisible(true);
   jPanel2.setVisible(false);
    }//GEN-LAST:event_jLabel3MouseClicked
    /**
 * Este método se llama cuando se hace clic en LabelRegistrarse.
 * Oculta el jPanel1 y muestra el jPanel2.
 */
    private void LabelRegistrarseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LabelRegistrarseMouseClicked
    jPanel1.setVisible(false);
    jPanel2.setVisible(true);
    }//GEN-LAST:event_LabelRegistrarseMouseClicked

    private void CajaContraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CajaContraseñaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CajaContraseñaActionPerformed

    private void CajaLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CajaLoginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CajaLoginActionPerformed

    private void CajaRecuperarCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CajaRecuperarCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CajaRecuperarCActionPerformed
        
    /**
 * Este método se llama cuando se hace clic en LabelOlvidasteC.
 * Muestra el jPanel3 y oculta los demás paneles.
 */
    private void LabelOlvidasteCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LabelOlvidasteCMouseClicked
        jPanel3.setVisible(true);
        jPanel2.setVisible(false);
        jPanel1.setVisible(false);
        jPanel4.setVisible(false);
        
    }//GEN-LAST:event_LabelOlvidasteCMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
        
    /**
 * Este método se llama cuando se hace clic en jButton1.
 * Inicia la sesión del usuario.
 */
    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked

       iniciarSesion();
             
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void BotomRecuperarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotomRecuperarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BotomRecuperarActionPerformed
    
    /**
 * Este método se llama cuando se hace clic en BotomRecuperar.
 * Recupera la contraseña del usuario y oculta los jLabel8 y jLabel9.
 */
    private void BotomRecuperarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotomRecuperarMouseClicked
        recuperarContraseña();
        jLabel8.setVisible(false);
        jLabel9.setVisible(false);
    }//GEN-LAST:event_BotomRecuperarMouseClicked
/**
 * Este método se llama cuando se hace clic en BotomRecuperar1.
 * Recupera la contraseña del usuario, actualiza la contraseña en la lista de usuarios y en el archivo,
 * y muestra un mensaje de confirmación.
 */
    private void BotomRecuperar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotomRecuperar1MouseClicked

        String nuevaContraseña = new String(CajaRecuperarContra.getPassword());

        if (nuevaContraseña.length() >= 8) {
            // Actualiza la contraseña en la lista de usuarios
            for (Usuario usuario : listaUsuarios) {
                if (usuario.getEmail().equals(CajaRecuperarC.getText())) {
                    usuario.setPassword(nuevaContraseña);
                    break;
                }
            }

            // Guarda la lista de usuarios actualizada en el archivo
            Usuario.escribirUsuarios(listaUsuarios, "usuarios.txt");

            // Restablece la visibilidad de los componentes
            CajaRecuperarContra.setVisible(false);
            BotomRecuperar1.setVisible(false);

            // Oculta los componentes del "Panel 3" si es necesario
            jPanel3.setVisible(false);

            // Muestra un mensaje de confirmación si lo deseas
            jLabel8.setVisible(true);

            // Configura el temporizador para esperar 1 segundo y luego volver al "Panel 1"
            timer = new Timer(1000, (ActionEvent e) -> {
                jPanel1.setVisible(true); // Vuelve al "Panel 1"
                jLabel8.setVisible(false); // Oculta el mensaje de confirmación
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            jLabel9.setVisible(true);
        }
    }//GEN-LAST:event_BotomRecuperar1MouseClicked
/**
 * Este método se llama cuando se hace clic en jButton2.
 * Registra un nuevo usuario.
 */
    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
       registrarUsuario();

    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed
    /**
 * Este método se llama cuando se hace clic en jButton3.
 * Muestra el jPanel1 y oculta los demás paneles.
 */
    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
      
    jPanel1.setVisible(true);
    jPanel2.setVisible(false);
    jPanel3.setVisible(false);
    jPanel4.setVisible(false);
    
                
    }//GEN-LAST:event_jButton3MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
              
                  VentanaLogin ventana = new VentanaLogin();
                ventana.setLocationRelativeTo(null); 
                ventana.setVisible(true);
                
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotomRecuperar;
    private javax.swing.JButton BotomRecuperar1;
    private javax.swing.JPasswordField CajaConfirmarC;
    private javax.swing.JPasswordField CajaContraseña;
    private javax.swing.JPasswordField CajaContraseña2;
    private javax.swing.JTextField CajaLogin;
    private javax.swing.JTextField CajaRecuperarC;
    private javax.swing.JPasswordField CajaRecuperarContra;
    private javax.swing.JTextField CajaRegistro;
    private javax.swing.JCheckBox CheckTerminos;
    private javax.swing.JLabel LabelContraseña;
    private javax.swing.JLabel LabelContraseña2;
    private javax.swing.JLabel LabelEmail2;
    private javax.swing.JLabel LabelEmailLogin;
    private javax.swing.JLabel LabelIniciarSesion;
    private javax.swing.JLabel LabelLogo;
    private javax.swing.JLabel LabelLogo1;
    private javax.swing.JLabel LabelNameLogo;
    private javax.swing.JLabel LabelOlvidasteC;
    private javax.swing.JLabel LabelRegistrarse;
    private javax.swing.JLabel LabelRegistrarse2;
    private javax.swing.JLabel LabelTerminos;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JLabel labelerror1;
    private javax.swing.JLabel labelerror2;
    private javax.swing.JLabel labelerror3;
    private javax.swing.JLabel labelerror4;
    private javax.swing.JLabel labelerror5;
    // End of variables declaration//GEN-END:variables
}
