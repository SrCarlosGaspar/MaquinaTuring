
package maquina.turing;

/**
 *
 * @author Carlos Gaspar
 */
public class Aresta {
     Estado inicio;
        String lido;
        String escrito;
        String direccao;
        Estado proxEstado;

        Aresta(Estado estadoInicial, String simboloLeitura, String simboloEscrita, String direccao, Estado proxEstado) {
            this.inicio = estadoInicial;
            this.lido = simboloLeitura;
            this.escrito = simboloEscrita;
            this.proxEstado = proxEstado;
            this.direccao = direccao;
        }
}
