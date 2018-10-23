package Game;
import java.awt.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Tela extends JPanel implements Runnable, KeyListener {
	
	
	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;
	//Render
	private Graphics2D g2d;
	private BufferedImage image;
	
	//Loop do jogo
	private Thread thread;
	private boolean running; 
	private long targetTime;
	
	//Lógica do Jogo
	private final int SIZE = 10;
	private Entidade head,comida;
	private ArrayList<Entidade> cobra;
	private int placar;
	private boolean gameover;
	
	
	
	//Movimentação
	
	private int dx, dy;
	
	
	//Teclas
	private boolean cima,baixo,direita,esquerda,start;
	
	public Tela() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT) );
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	
	@Override
	public void addNotify() {
		// TODO Auto-generated method stub
		super.addNotify();
		thread = new Thread(this);
		thread.start();
	}
	private void setFPS(int fps) {
		targetTime = 1000 / fps;
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if(k==KeyEvent.VK_UP) cima = true;
		if(k==KeyEvent.VK_DOWN) baixo = true;
		if(k==KeyEvent.VK_RIGHT) direita = true;
		if(k==KeyEvent.VK_LEFT) esquerda = true;
		if(k==KeyEvent.VK_SPACE) cima = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		if(k==KeyEvent.VK_UP) cima = false;
		if(k==KeyEvent.VK_DOWN) baixo = false;
		if(k==KeyEvent.VK_RIGHT) direita = false;
		if(k==KeyEvent.VK_LEFT) esquerda = false;
		if(k==KeyEvent.VK_SPACE) cima = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		if(running) return;
		init();
		long startTime;
		long elapsed;
		long wait;
	
		while(running) {
			startTime = System.nanoTime();
			
			update();
			requestRender();
			
			elapsed = System.nanoTime() - startTime;
			wait = targetTime - elapsed / 1000000;
			if(wait > 0) {
				try {
					Thread.sleep(wait);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

		}
	}
	private void init() {
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		running = true;
		setUplevel();	
		
	}
	
	private void setUplevel() {
		cobra = new ArrayList<Entidade>();
		head = new Entidade(SIZE);
		head.setPosition(WIDTH / 2, HEIGHT / 2);
		cobra.add(head);
		//Cobrinha surge
		for (int i =1; i<3 ; i++) {
			Entidade e = new Entidade(SIZE);
			e.setPosition(head.getX()  + (i* SIZE), head.getY());
			cobra.add(e);
			
		}
		comida = new Entidade(SIZE);
		setComida();
		placar = 0;
		
		gameover = false;
		dx = dy = 0;
		setFPS(10);
	}
	

	public void setComida() {
		int x = (int)(Math.random()*(WIDTH-SIZE));
		int y = (int)(Math.random()*(HEIGHT-SIZE));
		//Para que a comida fique centralizada dentro do quadrado
		x= x - (x%SIZE);
		y= y - (y%SIZE);
		
		comida.setPosition(x, y);
	}
	private void requestRender() {
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
	}
	//Movimentação na tela da Cobrinha
	private void update() {
		if(gameover) {
			if(start) {
				setUplevel();				
			}
			return;
		}
		if(cima && dy == 0) {
			dy = -SIZE;
			dx=0;
		}
		if(baixo && dy == 0) {
			dy = SIZE;
			dx=0;
		}
		if(direita && dx == 0 && dy != 0) {
			dy = 0;
			dx=SIZE;
		}
		if(esquerda && dx == 0) {
			dy = 0;
			dx=-SIZE;
		}
		if (dx !=0 || dy != 0) {
			for(int i = cobra.size()- 1; i > 0; i--) {
				cobra.get(i).setPosition(cobra.get(i-1).getX(), cobra.get(i-1).getY());
			}
			head.movimento(dx, dy);
			
			
		}
		
		//Gameover na colisão
		for(Entidade e : cobra) {
			if(e.isCollision(head) || head.getX()<0 || head.getX()>WIDTH || head.getY()<0 || head.getY()>HEIGHT)  {
				gameover = true;
				break;
			} 						
		}
		
			
		
		
		//Comer e aumentar o placar
		if(comida.isCollision(head)) {
			placar++;
			setComida();
		//Fazer com que a cobra cresça sempre que comer	
			Entidade e = new Entidade(SIZE);
			e.setPosition(-100,-100);
			cobra.add(e);
		}
		
		//Para caso não queira a colisão com as paredes
		//if(head.getX()<0)head.setX(WIDTH);
		//if(head.getY()<0)head.setY(HEIGHT);
		//if(head.getX()>WIDTH)head.setX(0);
		//if(head.getY()>HEIGHT)head.setY(0);

	}
	public void render(Graphics2D g2d) { 
		g2d.clearRect(0, 0, WIDTH, HEIGHT);
		g2d.setColor(Color.GREEN);
		for(Entidade e: cobra) {
			e.render(g2d);
		}
		
		g2d.setColor(Color.RED);
		comida.render(g2d);
		
		if(gameover) {
			g2d.drawString("Perdeu Humano", 150, 200);
		}

		g2d.setColor(Color.WHITE);
		g2d.drawString("Comeu: " + placar, 10, 10);
		if(dx == 0 && dy==0) {
			g2d.drawString("Preparado Humano", 150, 200);
		}
	}



}
