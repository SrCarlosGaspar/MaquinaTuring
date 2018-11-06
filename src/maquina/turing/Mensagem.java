

package maquina.turing; 
import java.awt.Color;
import javax.swing.JTextPane;


//Class para tratamento de todas mensagens
public class Mensagem {
    private final String  msg[] = { "Erro no codigo ou area de codigo vazio!..."," Compilado! PJ montado com sucesso!...","Moveu para esquerda...","Moveu para direita..."
                                    ,"Palavra aceite!...","Palavra rejeitada!..." }; 
   private  String msga;
    
    public Mensagem(){
        
         msga="Resultado:";
        
    }
 //Metodo que exibe as mensagens na area de mensagens   
    public void mostrarMsg( JTextPane jpane  ){
         jpane.setText( this.msga);
         if(this.msga.contains(msg[0])|| this.msga.contains("Erro"))
             jpane.setForeground(Color.red);
         else
             jpane.setForeground(Color.blue);
    }
 //Metodo que envia o cod msg a ser apresentada   
    public void setMsg(int cod){
         if(cod>=0 && cod<this.msg.length)
           this.msga = "Resultado: "+this.msg[cod];
    }
 //Metodo que envia msg a ser apresentada   
    public void setMsg(String msg){
        this.msga+= " \n  Estado actual-> "+msg;
    }
    
}
