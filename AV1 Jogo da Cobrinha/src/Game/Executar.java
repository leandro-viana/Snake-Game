package Game;
import java.awt.*;

import javax.swing.JFrame;

public class Executar {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Jogo da Cobrinha");
		frame.setContentPane(new Tela());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();


		frame.setPreferredSize(new Dimension(Tela.WIDTH,Tela.HEIGHT));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		

	}

}
