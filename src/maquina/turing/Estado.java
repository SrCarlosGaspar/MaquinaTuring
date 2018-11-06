/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maquina.turing;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Carlos Gaspar
 */
public class Estado {
     private String nomeEstado;
        List<Aresta> adj;

        public Estado(String nomeEstado) {
            this.nomeEstado = nomeEstado;
            this.adj = new ArrayList<Aresta>();
        }

        void addAdj(Aresta a) {
            adj.add(a);

        }

        /**
         * @return the nomeEstado
         */
        public String getNomeEstado() {
            return nomeEstado;
        }

        /**
         * @param nomeEstado the nomeEstado to set
         */
        public void setNomeEstado(String nomeEstado) {
            this.nomeEstado = nomeEstado;
        }
}
