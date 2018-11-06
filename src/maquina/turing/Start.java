package maquina.turing;

import java.awt.Color;
import java.awt.MenuBar;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

public class Start extends javax.swing.JFrame {

    Turing maquinaTuring; 
    CriaLinha numeroDeLinhas;
    String codigoVerficado;
    private File ficheiro;
    private FileInputStream is;

    /**
     * Creates new form AplicacaoMT
     */
    public Start() {
        super("MÁQUINA-TURING");

        initComponents();

        maquinaTuring = new Turing();
        maquinaTuring.inicializarFita();
        
        numeroDeLinhas = new CriaLinha(this.jTextArea1, 2);
        jScrollPane2.setRowHeader(new JViewport());
        jScrollPane2.getRowHeader().add(numeroDeLinhas);

        int largura = this.getToolkit().getScreenSize().width - 500;
        int altura = this.getToolkit().getScreenSize().height - 100;

        this.setSize(largura, altura);
        // this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);

        this.jScrollPane3.setToolTipText("Simulação da Fita");
        this.panelFita.setBackground(Color.DARK_GRAY);
        this.jTextArea1.setToolTipText("Editor de Texto");
        this.consolaMensagem.setToolTipText("Compilar e Executar");
        this.btExecutar.setEnabled(false);
        this.btRepetir.setEnabled(false);
        this.btEntrada.setEnabled(false);
        this.btExecutar1.setEnabled(false);

        desabilitarMenuSimular();
   

    }

    private String eliminarComentario(String codigo) {
        String s = " ";

        String cod[] = codigo.split("<");

        for (String cod1 : cod) {
            if (!cod1.startsWith("!")) {
                s = s + cod1;
            }
        }

        return s;

    }

    private boolean analizadorDeSintax(String codigo) {
        if (codigo.isEmpty()) {
            this.consolaMensagem.setText("Sem código para ser compilado!...");
            return false;
        }
        String vCodigo[] = codigo.replaceAll(" ", " ").trim().split(">");

        for (int i = 0; i < vCodigo.length; i++) {
            String elem[] = vCodigo[i].split(",");

            if (!(elem[0].contains("<q")) || !(elem[1].length() == 1) || !(elem[2].length() == 1) || !(elem[3].length() == 1) || !(elem.length == 5) || !(elem[4].contains("q"))) {

                this.maquinaTuring.mensagem.setMsg("Erro na especificação " + vCodigo[i] + " na linha " + (i + 1));
                this.jTextArea1.setSelectionColor(Color.red);
                try {
                    this.jTextArea1.select(this.jTextArea1.getLineStartOffset(i), this.jTextArea1.getLineEndOffset(i));
                } catch (BadLocationException ex) {
                    Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                }

                this.maquinaTuring.mensagem.mostrarMsg(consolaMensagem);
                return false;

            }
        }
        this.jTextArea1.setSelectionColor(Color.green);
        return true;
    }

    private void sobreProgramadores() {
        JOptionPane.showMessageDialog(null, "SIMULADOR DE MÁQUINA DE TURING"
                + "\n Carlos Gaspar Domingos\n Edvaldo Baptista Núnio Martins \nFélix Kayeye"
                + "Angola/Luanda 09 de 11 de 2018\n\n"
        );

    }

    private void sobreApp() {
        JOptionPane.showMessageDialog(null, "Este programa é um simulador da Máquina de Turing.\n"
                + "A sintaxe das especifições é: <q0,a,A,D,qn>\n"
                + "Onde 'a' é o simbolo lido e 'A' é a acção desencadeada\n"
                + "Nota:\n "
                + " B representa o símbolo branco\n"
                + "E e D são simbolos de movimentação (esquerda e direita)\n"
                + "Usa S ou outro simbolo para servir de auxilio em funções computávei\n"
        );
    }

    public void novoDocumento() {
        if (JOptionPane.showConfirmDialog(null, "Pretendes guardar a simulaão actual?", "Alerta", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            this.guardarFicheiroMT();
        } else {
            this.jTextArea1.setText("");
            this.maquinaTuring.limparFita();
            this.panelFita.setBackground(Color.DARK_GRAY);
            this.panelFita.removeAll();
            this.panelFita.updateUI();
            this.btEntrada.setEnabled(false);
            this.btExecutar.setEnabled(false);
            this.btExecutar1.setEnabled(false);
            this.btRepetir.setEnabled(false);
        }
    }

    //Função que guarda o código com formato mt para uso posterior
    private void guardarFicheiroMT() {
        JFileChooser chooser = new JFileChooser();

        chooser.setApproveButtonText("Guardar");
        chooser.setFileFilter(new FileNameExtensionFilter("PJ", "pj"));
        chooser.showOpenDialog(null);
        ficheiro = chooser.getSelectedFile();

        try {
            String text = this.jTextArea1.getText();
            java.io.FileWriter fileWriter = new java.io.FileWriter(ficheiro + ".pj");
            try (java.io.BufferedWriter br = new java.io.BufferedWriter(fileWriter)) {
                br.write(text);
            }
        } catch (IOException ex) {
        }
    }

    //Função que faz a abertura do ficheiro de extensão maquinaTuring 
    private void abrirFicheiroMT() {
        String linha, conteudo = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PJ", "pj"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result != JFileChooser.CANCEL_OPTION) {
            try {
                File fileName = fileChooser.getSelectedFile();
                if ((fileName == null) || (fileName.getName().equals(""))) {
                    JOptionPane.showMessageDialog(this, "Nome invalido", "Verifica o ficheiro", JOptionPane.ERROR_MESSAGE);
                }
                is = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(is);
                try (BufferedReader br = new BufferedReader(isr)) {
                    linha = br.readLine(); // primeira linha

                    // criar um carregando
                    while (linha != null) {
                        conteudo += linha + "\n";
                        linha = br.readLine();
                    }
                    // fechar o carregando
                    jTextArea1.setText(conteudo);
                }
            } // }
            catch (FileNotFoundException ex) {
                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // } 
    }

    private boolean textaContinuidade() {
        return (JOptionPane.showConfirmDialog(null, "Suspeitamos que a simulação caiu num loop.\n Pretende continuar?", "Alerta", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
    }

    public void compilar(String codigo) {
        if (this.maquinaTuring.montarMaquinaTuring(codigo)) {
            this.btEntrada.setEnabled(true);
            this.jTextArea1.setForeground(Color.GREEN.darker());

        }
        this.maquinaTuring.mensagem.mostrarMsg(consolaMensagem);
        this.panelFita.updateUI();
    }

    public void executar() {
        int contador = 0;
        try {
            while (!this.maquinaTuring.estadoActual.adj.isEmpty()) {
                contador++;
                //this.maquinaTuring.espera(2);
                this.maquinaTuring.estadoActual = this.maquinaTuring.funcTransicao(this.maquinaTuring.estadoActual, this.panelFita);

                if (contador == 1000) {
                    if (this.textaContinuidade()) {
                        contador = 0;

                    } else {

                        JOptionPane.showMessageDialog(null, "Loop ou estado de rejeição");

                        this.maquinaTuring.mostrarFita(panelFita);
                        this.maquinaTuring.mensagem.mostrarMsg(consolaMensagem);
                        this.panelFita.updateUI();

                        break;
                    }
                }

            }

            this.maquinaTuring.estadoActual = this.maquinaTuring.funcTransicao(this.maquinaTuring.estadoActual, this.panelFita);
            this.maquinaTuring.mostrarFita(panelFita);
            this.maquinaTuring.mensagem.mostrarMsg(consolaMensagem);
            this.panelFita.updateUI();
            this.btRepetir.setEnabled(true);

        } catch (NullPointerException ex) {
            this.consolaMensagem.setText("Resultado: Erro!! Cria primeiro uma Máquina de Turing");
        }

    }

    private void execucaoPorPassos() {
        try {
            this.maquinaTuring.estadoActual = this.maquinaTuring.funcTransicao(this.maquinaTuring.estadoActual, this.panelFita);

            this.maquinaTuring.mostrarFita(panelFita);
            this.maquinaTuring.mensagem.mostrarMsg(consolaMensagem);
            this.panelFita.updateUI();
            this.btRepetir.setEnabled(true);
        } catch (NullPointerException ex) {
            this.consolaMensagem.setText("Resultado: Erro! Cria primeiro uma Máquina de Turing");
        }
    }

    private void imprimirCodigo() {
        try {
            // TODO add your handling code here:
            JTextPane textoImprimir = new JTextPane();
            int contador_de_linhas = 1;
            String dado = "", linhas = "";
            String vetor[] = this.jTextArea1.getText().split("\n");
            for (String string : vetor) {
                linhas += contador_de_linhas + " " + string + "\n";
                contador_de_linhas++;
            }

            textoImprimir.print();
        } catch (PrinterException ex) {
            Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

   

    private void desabilitarMenuSimular() {

        //Menu Simulação
        jMenuItem10.setEnabled(false);
        jMenuItem11.setEnabled(false);
        jMenuItem12.setEnabled(false);
    }

    private void abilitarMenuSimular() {
        jMenuItem10.setEnabled(true);
        jMenuItem11.setEnabled(true);
        jMenuItem12.setEnabled(true);
        this.btExecutar.setEnabled(true);
        this.btExecutar1.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        btAbrir = new javax.swing.JButton();
        btGuardar = new javax.swing.JButton();
        btCopiar = new javax.swing.JButton();
        btCortar = new javax.swing.JButton();
        btColar = new javax.swing.JButton();
        btCompilar = new javax.swing.JButton();
        btExecutar = new javax.swing.JButton();
        btEntrada = new javax.swing.JButton();
        btExecutar1 = new javax.swing.JButton();
        btRepetir = new javax.swing.JButton();
        btInfo = new javax.swing.JButton();
        jSplitPane3 = new javax.swing.JSplitPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        panelFita = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        consolaMensagem = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/document-new (2).png"))); // NOI18N
        jButton1.setText(" ");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        btAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/document-open-4.png"))); // NOI18N
        btAbrir.setToolTipText("Abrir") ;
        btAbrir.setText(" ");
        btAbrir.setFocusable(false);
        btAbrir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btAbrir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAbrirActionPerformed(evt);
            }
        });
        jToolBar1.add(btAbrir);

        btGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/document-export (2).png"))); // NOI18N
        btGuardar.setToolTipText("Guardar");
        btGuardar.setText(" ");
        btGuardar.setFocusable(false);
        btGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGuardarActionPerformed(evt);
            }
        });
        jToolBar1.add(btGuardar);

        btCopiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/edit-copy-6.png"))); // NOI18N
        btCopiar.setToolTipText("Copiar");
        btCopiar.setText(" ");
        btCopiar.setFocusable(false);
        btCopiar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btCopiar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCopiarActionPerformed(evt);
            }
        });
        jToolBar1.add(btCopiar);

        btCortar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/edit-cut-6.png"))); // NOI18N
        btCortar.setToolTipText("cortar");
        btCortar.setText(" ");
        btCortar.setFocusable(false);
        btCortar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btCortar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btCortar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCortarActionPerformed(evt);
            }
        });
        jToolBar1.add(btCortar);

        btColar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/edit-paste-3 (2).png"))); // NOI18N
        btColar.setToolTipText("Colar");
        btColar.setText(" ");
        btColar.setFocusable(false);
        btColar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btColar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btColar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btColarActionPerformed(evt);
            }
        });
        jToolBar1.add(btColar);

        btCompilar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/go-next-8 (2).png"))); // NOI18N
        btCompilar.setToolTipText("Compilar");
        btCompilar.setText(" ");
        btCompilar.setFocusable(false);
        btCompilar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btCompilar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btCompilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCompilarActionPerformed(evt);
            }
        });
        jToolBar1.add(btCompilar);

        btExecutar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/media-seek-forward-3 (2).png"))); // NOI18N
        btExecutar.setToolTipText("Simulação rápida");
        btExecutar.setText(" ");
        btExecutar.setFocusable(false);
        btExecutar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btExecutar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btExecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExecutarActionPerformed(evt);
            }
        });
        jToolBar1.add(btExecutar);

        btEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/run-build-3 (2).png"))); // NOI18N
        btEntrada.setToolTipText("Inroduzir a palavra");
        btEntrada.setText(" ");
        btEntrada.setFocusable(false);
        btEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btEntrada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEntradaActionPerformed(evt);
            }
        });
        jToolBar1.add(btEntrada);

        btExecutar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/go-next-9 (2).png"))); // NOI18N
        btExecutar1.setToolTipText("Simular passo à passo");
        btExecutar1.setText(" ");
        btExecutar1.setFocusable(false);
        btExecutar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btExecutar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btExecutar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExecutar1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btExecutar1);

        btRepetir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/edit-undo (2).png"))); // NOI18N
        btRepetir.setToolTipText("Repetir a simulaçao");
        btRepetir.setText(" ");
        btRepetir.setFocusable(false);
        btRepetir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btRepetir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btRepetir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRepetirActionPerformed(evt);
            }
        });
        jToolBar1.add(btRepetir);

        btInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/help-about-3 (2).png"))); // NOI18N
        btInfo.setText(" ");
        btInfo.setToolTipText("Info");
        btInfo.setFocusable(false);
        btInfo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btInfo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btInfoActionPerformed(evt);
            }
        });
        jToolBar1.add(btInfo);

        jSplitPane3.setDividerLocation(480);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setToolTipText("");
        jSplitPane1.setOneTouchExpandable(true);

        jScrollPane1.setViewportView(jEditorPane1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setMargin(new java.awt.Insets(2, 15, 2, 2));
        System.out.println(this.jTextArea1.getLineCount());
        jTextArea1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jTextArea1CaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextArea1InputMethodTextChanged(evt);
            }
        });
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea1KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTextArea1);

        jSplitPane1.setLeftComponent(jScrollPane2);

        jScrollPane3.setViewportView(panelFita);

        jSplitPane1.setRightComponent(jScrollPane3);

        jSplitPane3.setTopComponent(jSplitPane1);

        consolaMensagem.setEditable(false);
        consolaMensagem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        consolaMensagem.setToolTipText("");
        jScrollPane5.setViewportView(consolaMensagem);

        jSplitPane3.setRightComponent(jScrollPane5);

        jMenu1.setText("Arquivo");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/document-new.png"))); // NOI18N
        jMenuItem3.setText("Novo");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/document-open-4_1.png"))); // NOI18N
        jMenuItem1.setText("Abrir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/document-export.png"))); // NOI18N
        jMenuItem2.setText("Guardar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/document-print.png"))); // NOI18N
        jMenuItem4.setText("Imprimir");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/dialog-cancel.png"))); // NOI18N
        jMenuItem5.setText("Sair");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Editar");

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/edit-copy-7.png"))); // NOI18N
        jMenuItem6.setText("Copiar");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/edit-paste-3.png"))); // NOI18N
        jMenuItem7.setText("Colar");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/edit-cut-7.png"))); // NOI18N
        jMenuItem8.setText("Cortar");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Simular");
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/go-next-8.png"))); // NOI18N
        jMenuItem9.setText("Compilar");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/media-seek-forward-3.png"))); // NOI18N
        jMenuItem10.setText("Executar");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem10);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/go-next-9.png"))); // NOI18N
        jMenuItem11.setText("Executar passo a passo");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem11);

        jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/edit-undo.png"))); // NOI18N
        jMenuItem12.setText("Repetir execução");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem12);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Ajuda");

        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/help-2.png"))); // NOI18N
        jMenuItem13.setText("Sobre os programadores");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem13);

        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/help-about-3.png"))); // NOI18N
        jMenuItem14.setText("Sobre o App");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem14);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextArea1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyPressed
        // TODO add your handling code here:
        this.btGuardar.setEnabled(true);

    }//GEN-LAST:event_jTextArea1KeyPressed

    private void btCompilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCompilarActionPerformed
        // TODO add your handling code here:

        //if(this.analizadorSintetico(this.jTextArea1.getText())){ 
        if (this.analizadorDeSintax(this.jTextArea1.getText())) {
            compilar(this.jTextArea1.getText());
            abilitarMenuSimular();
        }

    }//GEN-LAST:event_btCompilarActionPerformed


    private void btExecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExecutarActionPerformed
        // TODO add your handling code here: 

        executar();

    }//GEN-LAST:event_btExecutarActionPerformed

    private void btEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEntradaActionPerformed
        // TODO add your handling code here:
        this.panelFita.removeAll();
        this.panelFita.setBackground(Color.DARK_GRAY);
        this.maquinaTuring.adicionaItemNaFita();
        this.maquinaTuring.mostrarFita(panelFita);
        this.panelFita.updateUI();
        this.btExecutar.setEnabled(true);
        this.btExecutar1.setEnabled(true);
    }//GEN-LAST:event_btEntradaActionPerformed

    private void btRepetirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRepetirActionPerformed
        // TODO add your handling code here:
        this.panelFita.removeAll();

        try {
            this.panelFita.setBackground(Color.DARK_GRAY);
            this.panelFita.setBackground(Color.DARK_GRAY);
            this.maquinaTuring.mostrarFita(panelFita);
            this.panelFita.updateUI();
        } catch (NullPointerException ex) {

        }
    }//GEN-LAST:event_btRepetirActionPerformed

    private void btGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGuardarActionPerformed
        // TODO add your handling code here:
        this.guardarFicheiroMT();
    }//GEN-LAST:event_btGuardarActionPerformed

    private void btAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAbrirActionPerformed
        // TODO add your handling code here:

        this.abrirFicheiroMT();
    }//GEN-LAST:event_btAbrirActionPerformed

    private void btCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCopiarActionPerformed
        // TODO add your handling code here:
        this.jTextArea1.copy();
    }//GEN-LAST:event_btCopiarActionPerformed

    private void btCortarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCortarActionPerformed
        // TODO add your handling code here:
        this.jTextArea1.cut();
    }//GEN-LAST:event_btCortarActionPerformed

    private void btColarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btColarActionPerformed
        // TODO add your handling code here:
        this.jTextArea1.paste();
    }//GEN-LAST:event_btColarActionPerformed

    private void jTextArea1CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextArea1CaretPositionChanged
        // TODO add your handling code here:
        this.btGuardar.setEnabled(true);
    }//GEN-LAST:event_jTextArea1CaretPositionChanged

    private void jTextArea1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextArea1InputMethodTextChanged
        // TODO add your handling code here:
        this.btGuardar.setEnabled(true);
    }//GEN-LAST:event_jTextArea1InputMethodTextChanged

    private void btExecutar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExecutar1ActionPerformed
        // TODO add your handling code here:
        execucaoPorPassos();
    }//GEN-LAST:event_btExecutar1ActionPerformed

    private void btInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btInfoActionPerformed
        // TODO add your handling code here:
        sobreProgramadores();
    }//GEN-LAST:event_btInfoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        this.abrirFicheiroMT();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        this.guardarFicheiroMT();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:

        if (this.analizadorDeSintax(this.jTextArea1.getText())) {
            compilar(this.jTextArea1.getText());
            abilitarMenuSimular();
        }


    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.novoDocumento();
    
        this.consolaMensagem.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        this.novoDocumento();
               
        this.consolaMensagem.setText("");
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        this.jTextArea1.copy();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        this.jTextArea1.paste();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        this.jTextArea1.cut();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // TODO add your handling code here:
        execucaoPorPassos();

    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jMenu3ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:

        try {
            
            this.panelFita.setBackground(Color.DARK_GRAY);

            this.panelFita.updateUI();
        } catch (NullPointerException ex) {

        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // TODO add your handling code here:
        sobreProgramadores();
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        // TODO add your handling code here:
        sobreApp();
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        executar();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        imprimirCodigo();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    /**
     * @param args the command line argeption
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
            java.util.logging.Logger.getLogger(Start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
 /*Splash.getInstance().openSplash();
         try {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
         if ("Nimbus".equals(info.getName())) {
         javax.swing.UIManager.setLookAndFeel(info.getClassName());
         break;
         }
         }
         } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
         java.util.logging.Logger.getLogger(AplicacaoMT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
         }*/
        //</editor-fold>
        // Thread.sleep(3000);
        //</editor-fold>
 /*Splash.getInstance().openSplash();
         try {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
         if ("Nimbus".equals(info.getName())) {
         javax.swing.UIManager.setLookAndFeel(info.getClassName());
         break;
         }
         }
         } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
         java.util.logging.Logger.getLogger(AplicacaoMT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
         }*/
        //</editor-fold>
        // Thread.sleep(3000);
        //</editor-fold>
 /*Splash.getInstance().openSplash();
         try {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
         if ("Nimbus".equals(info.getName())) {
         javax.swing.UIManager.setLookAndFeel(info.getClassName());
         break;
         }
         }
         } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
         java.util.logging.Logger.getLogger(AplicacaoMT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
         }*/
        //</editor-fold>
        // Thread.sleep(3000);
        //</editor-fold>
 /*Splash.getInstance().openSplash();
         try {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
         if ("Nimbus".equals(info.getName())) {
         javax.swing.UIManager.setLookAndFeel(info.getClassName());
         break;
         }
         }
         } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
         java.util.logging.Logger.getLogger(AplicacaoMT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
         }*/
        //</editor-fold>
        // Thread.sleep(3000);

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Start().setVisible(true);

            }
        });

       // Splash.getInstance().dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAbrir;
    private javax.swing.JButton btColar;
    private javax.swing.JButton btCompilar;
    private javax.swing.JButton btCopiar;
    private javax.swing.JButton btCortar;
    private javax.swing.JButton btEntrada;
    private javax.swing.JButton btExecutar;
    private javax.swing.JButton btExecutar1;
    private javax.swing.JButton btGuardar;
    private javax.swing.JButton btInfo;
    private javax.swing.JButton btRepetir;
    private javax.swing.JTextPane consolaMensagem;
    private javax.swing.JButton jButton1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel panelFita;
    // End of variables declaration//GEN-END:variables
}
