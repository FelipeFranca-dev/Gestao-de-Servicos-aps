package teste;

import java.awt.EventQueue;

import controller.Controller;
import dao.dummy.DaoDummy;
import view.janelas.ViewJanelas;

public class Teste {
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable(){
           public void run(){
       		new Controller(new DaoDummy(), new ViewJanelas());
           }
        });
	}
}
