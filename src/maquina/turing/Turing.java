package maquina.turing;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Turing {

    /**
     * @return the InicioDaFita
     */
    public int getInicioDaFita() {
        return InicioDaFita;
    }

    /**
     * @param InicioDaFita the InicioDaFita to set
     */
    public void setInicioDaFita(int InicioDaFita) {
        this.InicioDaFita = InicioDaFita;
    }

    /**
     * @return the simboloLeitura
     */
    public String getSimboloLido() {
        return simboloLeitura;
    }

    /**
     * @param simboloLido the simboloLeitura to set
     */
    public void setSimboloLido(String simboloLido) {
        this.simboloLeitura = simboloLido;
    }

    /**
     * @return the simboloEscrita
     */
    public String getSimboloEscrito() {
        return simboloEscrita;
    }

    /**
     * @param simboloEscrito the simboloEscrita to set
     */
    public void setSimboloEscrito(String simboloEscrito) {
        this.simboloEscrita = simboloEscrito;
    }

    /**
     * @return the palavraDeEntrada
     */
    public String getPalavra() {
        return palavraDeEntrada;
    }

    /**
     * @param palavra the palavraDeEntrada to set
     */
    public void setPalavra(String palavra) {
        this.palavraDeEntrada = palavra;
    }

 


//Maquina de Turing
    List<Estado> estados;
    List<Aresta> arestas;
    ArrayList<String> valorEntrada;
    ArrayList<String> valorFita;
    ArrayList<JButton> fita;
    private int InicioDaFita;
    private String simboloLeitura;
    private String simboloEscrita;
    Mensagem mensagem;
    Estado estadoInicial;
    Estado estadoActual;
    private String palavraDeEntrada;


    public Turing() {
        estados = new ArrayList<Estado>();
        arestas = new ArrayList<Aresta>();
        valorEntrada = new ArrayList<String>();
        valorFita = new ArrayList<String>();
        fita = new ArrayList<JButton>();
        mensagem = new Mensagem();
        this.InicioDaFita = 5;
    }

    //Metodo que inicializa a fita
    public void inicializarFita() {
        /*Nota: A inicialização da fita deverá receber por parâmetro a palavar a ser testada
         gerando assim de forma automática um tamanho da fita maior em relação ao tamanho da palavra(potencialmente maior q w)
         */
        for (int i = 0; i <= 10; i++) {
            fita.add(new JButton("B"));
        }
        this.setInicioDaFita(5);

    }

//Função que Adicina estados na lista de estados, retorna true se o nome passado em 
    //parâmetro ainda não existe e false, caso contrario.
    boolean addEstado(String nome) {
        int n = this.estados.size();
        for (int i = 0; i < n; i++) {
            if ((this.estados.get(i).getNomeEstado().trim().equals(nome.trim()))) {
                return false;
            }
        }
        this.estados.add(new Estado(nome));

        return true;
    }
//Função que adiciona uma aresta na lista de arestas e retorna a mesma.<q1,1,1,D,q1>

    Aresta addAresta(Estado origem, String simboloLido, String gravacao, String movimentar, Estado destino) {
        Aresta a = new Aresta(origem, simboloLido, gravacao, movimentar, destino);
        origem.addAdj(a);
        arestas.add(a);
        return a;
    }

    //Função que Retona um estado na lista de estados
    Estado retornaEstado(String nomeEstado) {
        Estado e = new Estado(nomeEstado);
        for (Estado estado : this.estados) {
            if (estado.getNomeEstado().equals(nomeEstado)) {
                return estado;
            }
        }
        return e;
    }

    //Função que faz a leitura de um simbolo na fita, na celula apontada pela cabeça <q1,1,X,D,q2> 

    String leituraNafita(int cabeca) {
        return this.fita.get(cabeca).getText();
    }

//Função que faz a gravação na fita, na celula apontada pela cabeça    
    boolean gravarNafita(String simbolo) {
        if (this.getInicioDaFita() > 0) {
            this.fita.get(this.getInicioDaFita()).setText(simbolo);
            return true;
        }
        return false;
    }

    //Metodo auxiliar que pinta a celula estadoActual, apontada pela    

    public void mudaCor(int cabeca, Color cor) {
        this.fita.get(cabeca).setBackground(cor);
    }

    //Executa o deslocamento da cabeça da fita
    public boolean deslocamento(String mov) {
        if (this.getInicioDaFita() == 1 && mov.equals("E")) {
            this.mudaCor(this.getInicioDaFita(), Color.LIGHT_GRAY);//Pintar a cor padrão na celula anterior
            this.fita.add(1, new JButton("B"));//Adiciona mais um botão na posição 1 simulando fita infinita a esquerda
            this.mudaCor(this.getInicioDaFita(), Color.BLUE);//Pintar azul na celula estadoActual
            this.mensagem.setMsg(2);//Inserir o codigo da mensagem que será mostrada na área de mensagens
            return true;
        } else if (this.getInicioDaFita() == this.fita.size() - 2 && mov.equals("D")) {
            this.fita.add(new JButton("B"));// Adicionar mais mais um botão no fim da lista  
            this.mudaCor(this.getInicioDaFita(), Color.LIGHT_GRAY);//Pintar a cor padrão na celula anterior
            this.setInicioDaFita(this.getInicioDaFita() + 1);
            this.mudaCor(this.getInicioDaFita(), Color.BLUE);
            this.mensagem.setMsg(3);
            return true;
        } else if (mov.equals("E")) {
            this.mudaCor(this.getInicioDaFita(), Color.LIGHT_GRAY);
            this.setInicioDaFita(this.getInicioDaFita() - 1);
            this.mudaCor(this.getInicioDaFita(), Color.BLUE);
            this.mensagem.setMsg(2);
            return true;
        } else if (mov.equals("D")) {
            this.mudaCor(this.getInicioDaFita(), Color.LIGHT_GRAY);
            this.setInicioDaFita(this.getInicioDaFita() + 1);
            this.mudaCor(this.getInicioDaFita(), Color.BLUE);
            this.mensagem.setMsg(3);
            return true;
        }

        return false;
    }
    
    public void mostrarFita(JPanel panel) {
        int i = 0;
        while (i < this.fita.size()) {
            panel.add(this.fita.get(i));
            i++;
        }
    }
  
// limpa código. de <q0,1,B,D,q1> para q0 1 B D q1
    public boolean limpaCodigo(String cod) {
        String[] instrucao = cod.trim().split(">");
        for (int i = 0; 0 < instrucao.length; i++) {
            instrucao[i].replace("<", "").trim();
        }
        String[] item = instrucao[0].split(",");

        return false;
    }

//Função que verifica a aceitação ou não de uma palavraDeEntrada    
    public boolean verificaPalavra(Estado e, JPanel panel) {
        try {
            if (e.adj.isEmpty()) {
                this.mudaCor(this.getInicioDaFita(), Color.green);
                panel.setBackground(Color.GREEN.darker());
                this.mensagem.setMsg(4);//palavra aceite
                return true;
            } else {
                this.mudaCor(this.getInicioDaFita(), Color.red);
                panel.setBackground(Color.PINK);
                this.mensagem.setMsg(5);//palavra negada

            }
        } catch (NullPointerException ex) {
            this.mensagem.setMsg("Nenhum estado foi informado");
        }
        return false;
    }
    
    public void limparFita() {
        for (int i = 0; i < this.fita.size(); i++) {
            this.fita.get(i).setText("B");
            this.mudaCor(i, Color.LIGHT_GRAY);
        }

        this.fita.clear();  //Remover todos botoes na fita

        this.inicializarFita();//Inicializa a fita 
        try {
            this.estadoActual = this.estados.get(0);
        } catch (IndexOutOfBoundsException ex) {

        }
        this.setInicioDaFita(5);
    }


//será chamada na compilalão
    public boolean montarMaquinaTuring(String cod) {

        //Remove os estados existentens para criar outros
        if (!this.estados.isEmpty()) {
            for (int i = this.estados.size() - 1; i >= 0; i--) {
                this.estados.remove(i);
            }
        }

        if (cod.isEmpty()) {
            this.mensagem.setMsg(0);
            return false;
        }
        //pega a instrução do programa
        String[] instrucoes = cod.replaceAll(" ", "").trim().split(">");
        String[] estadoInit = instrucoes[0].trim().split(",");
        this.estadoInicial = new Estado(estadoInit[0].replace("<", ""));

        this.estados.add(estadoInicial);
        this.estadoActual = this.estadoInicial;

        //cria lista de estados  
        for (String instrucao : instrucoes) {
            String[] elem = instrucao.replaceAll(" ", "").split(",");
            try {
                boolean bol = this.addEstado(elem[0].replace("<", "").trim());

                this.addEstado(elem[4].trim());
                System.out.println(bol);
            } catch (ArrayIndexOutOfBoundsException ex) {
                //  Logger.getLogger(Turing.class.getName()).log(Level.SEVERE, null, ex);
                this.mensagem.setMsg("Oops! alguma coisa errada não está certa!...");
                return false;
            }
        }
       
        //Ligar as arestas entre os estados
        for (String especif1 : instrucoes) {
            String[] elem = especif1.split(",");
            this.addAresta(this.retornaEstado(elem[0].replace("<", "").trim()), elem[1], elem[2], elem[3], this.retornaEstado(elem[4].trim()));
        }

        this.mensagem.setMsg(1);

        return true;
    }

    //Função resnposável pela execução da PJ, será chamada no tempo de execução. 
    //Conterá a função de inicialização da fita e a função de transição 
    public void espera(int num) {
        try {
            Thread.sleep(num * 10);
        } catch (InterruptedException ex) {
            Logger.getLogger(Turing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public boolean adicionaItemNaFita() {

        this.limparFita();
        try {
            this.setPalavra(JOptionPane.showInputDialog(null, "Digita a palavra", JOptionPane.INFORMATION_MESSAGE).toString().trim());

        } catch (NullPointerException es) {
            this.mensagem.setMsg("Ups!...");
            return false;
        }
        if (getPalavra().isEmpty()) {
            return false;
        }
        String[] vpalavra = getPalavra().split("");
        vpalavra[0] = getPalavra().substring(0, 1).trim();
        System.out.println(vpalavra[0]);

        if (this.fita.size() - 5 < getPalavra().length()) {
            for (int i = 0; i <= getPalavra().length() - 2; i++) {
                this.fita.add(new JButton("B"));
            }
        }
        this.setInicioDaFita(5);
        int j = this.getInicioDaFita();
        for (int i = 1; i <= getPalavra().length(); i++) {
            this.fita.get(j).setText(vpalavra[i]);
            j++;
        }

        this.mudaCor(getInicioDaFita(), Color.red);
        ////
        return false;
    }


//Função de transição entre estados, será chamada no momento de execução apois a inicialização da fica com uma palavraDeEntrada
    Estado funcTransicao(Estado actual, JPanel panel) { //Nota: o simbolo lido vem da fita

        try {
            for (Aresta adj : actual.adj) {

                if (adj.lido.equals(this.leituraNafita(this.getInicioDaFita()))) {
                    if (adj.direccao.equals("D") || adj.direccao.equals("E")) {

                        this.gravarNafita(adj.escrito); //Gravar na fita
                        this.deslocamento(adj.direccao);//movimentar na fita
                        this.mensagem.setMsg(adj.proxEstado.getNomeEstado());

                        return adj.proxEstado;
                    }
                }
            }
            this.verificaPalavra(actual, panel);

            this.mensagem.setMsg(actual.getNomeEstado());

        } catch (NullPointerException ex) {
            this.mensagem.setMsg("Sem estados!..." + ex);
        }

        return actual;
    }

}

