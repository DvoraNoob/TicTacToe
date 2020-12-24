
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable,KeyListener,MouseListener{

	
	public static final int WIDTH = 300, HEIGHT = 330;
	public int PLAYER = 1,OPONENTE = 2,CURRENT;
	public int select;
	
	public BufferedImage PLAYER_SPRITE,OPONENTE_SPRITE;
	public int[][] TABULEIRO = new int[3][3];
	
	public boolean player2 = false;
	
	public int mx,my;
	public boolean pressed = false;
	
	public boolean menu = true;
	public boolean selected = false;
	public boolean gameover = false;
	
	public Random random;
	
	public Game() {
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.addKeyListener(this);
		this.addMouseListener(this);
		try {
			PLAYER_SPRITE = ImageIO.read(getClass().getResource("/player.png"));
			OPONENTE_SPRITE = ImageIO.read(getClass().getResource("/oponente.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		random = new Random();
	}
	
	public void tick() {
		if(!menu) {
			select++;
			if(select == 1) {
				if(random.nextInt(100) < 50) {
					CURRENT = PLAYER;
					System.out.println("1");
				}else {
					CURRENT = OPONENTE;
					System.out.println("2");
				}
			}
			
			if(player2 == false) {
				if(CURRENT == PLAYER) {
					if(pressed) {
						pressed = false;
						mx/=100;
						my/=100;
						if(TABULEIRO[mx][my] == 0) {
							TABULEIRO[mx][my] = PLAYER;
							CURRENT = OPONENTE;
						}
					}
				}else if(CURRENT == OPONENTE){
					for(int xx = 0; xx < TABULEIRO.length; xx++) {
						for(int yy = 0; yy < TABULEIRO.length; yy++) {
							if(TABULEIRO[xx][yy] == 0) {
								Node bestMove = getBestMove(xx,yy,0,OPONENTE);
								TABULEIRO[bestMove.x][bestMove.y] = OPONENTE;
								CURRENT = PLAYER;
								return;
							}
						}
					}
	
				}
			}else {
				if(CURRENT == PLAYER) {
					if(pressed) {
						pressed = false;
						mx/=100;
						my/=100;
						if(TABULEIRO[mx][my] == 0) {
							TABULEIRO[mx][my] = PLAYER;
							CURRENT = OPONENTE;
						}
					}
				}else if(CURRENT == OPONENTE){
					if(pressed) {
						pressed = false;
						mx/=100;
						my/=100;
						if(TABULEIRO[mx][my] == 0) {
							TABULEIRO[mx][my] = OPONENTE;
							CURRENT = PLAYER;
						}
					}
				}
			}
			
			if(checkVictory() == PLAYER) {
				gameover = true;		
			}else if(checkVictory() == OPONENTE) {
				gameover = true;		
			}else if(checkVictory() == 0) {
				gameover = true;		
			}
		}
	}

	
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH,HEIGHT);
		
		for(int xx = 0; xx < TABULEIRO.length; xx++) {
			for(int yy = 0; yy < TABULEIRO.length; yy++) {
				g.setColor(Color.black);
				g.drawRect(xx*100, yy*100, 100,100);
				if(TABULEIRO[xx][yy] == PLAYER) {
					g.drawImage(PLAYER_SPRITE,xx*100, yy*100,null);
				}else if(TABULEIRO[xx][yy] == OPONENTE) {
					g.drawImage(OPONENTE_SPRITE,xx*100, yy*100,null);
				}
			}
		}
		
		if(menu == true) {
			g2.setColor(new Color(0,0,0,230));
			g2.fillRect(0, 0, WIDTH,HEIGHT);
			
			if(player2) {
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString("PvP", 150-20, 50);
			}else {
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString("PvE", 150-20, 50);
			}
		}
		
		if(!player2 && menu == false && gameover == false) {
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString("Sua Vez", 150-40, 325);	
		}else if(player2 && menu == false && gameover == false){
			if(CURRENT == PLAYER) {
				g.setColor(Color.black);
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString("Vez do Player 1", 150-70, 325);
			}else {
				g.setColor(Color.black);
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString("Vez do Player 2", 150-70, 325);
			}
		}
		
		if(gameover) {
			g2.setColor(new Color(0,0,0,230));
			g2.fillRect(0, 0, WIDTH,HEIGHT);
		}
		
		if(checkVictory() == PLAYER) {
			if(!player2) {
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString("Player venceu!", 150-70, 150);
			}else {
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString("Player 1 venceu!", 150-70, 150);
			}
		}else if(checkVictory() == OPONENTE) {
			if(!player2) {
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString("Oponente venceu!", 150-70, 150);
			}else {
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString("Player 2 venceu!", 150-70, 150);
			}
		}else if(checkVictory() == 0) {
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString("Deu Empate!", 150-60, 150);		
		}
		
		g.dispose();
		bs.show();
	}

	
	
	public static void main(String args[]) {
		Game game = new Game();
		JFrame frame = new JFrame("Jogo da Velha");
		frame.add(game);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		new Thread(game).start();
	}
	
	@Override
	public void run() {
		
		while(true) {
			tick();
			render();
			requestFocus();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void resetTabuleiro() {
		menu = true;
		gameover = false;
		for(int xx = 0; xx < TABULEIRO.length; xx++) {
			for(int yy = 0; yy < TABULEIRO.length; yy++) {
				TABULEIRO[xx][yy] = 0;
			}
		}
	}
	
	public int checkVictory() {
		//PLAYER
		
		//H
		if(TABULEIRO[0][0] == PLAYER && TABULEIRO[1][0] == PLAYER && TABULEIRO [2][0] == PLAYER) {
			return PLAYER;
		}
		if(TABULEIRO[0][1] == PLAYER && TABULEIRO[1][1] == PLAYER && TABULEIRO [2][1] == PLAYER) {
			return PLAYER;
		}
		if(TABULEIRO[0][2] == PLAYER && TABULEIRO[1][2] == PLAYER && TABULEIRO [2][2] == PLAYER) {
			return PLAYER;
		}
		
		//v
		if(TABULEIRO[0][0] == PLAYER && TABULEIRO[0][1] == PLAYER && TABULEIRO [0][2] == PLAYER) {
			return PLAYER;
		}
		if(TABULEIRO[1][0] == PLAYER && TABULEIRO[1][1] == PLAYER && TABULEIRO [1][2] == PLAYER) {
			return PLAYER;
		}
		if(TABULEIRO[2][0] == PLAYER && TABULEIRO[2][1] == PLAYER && TABULEIRO [2][2] == PLAYER) {
			return PLAYER;
		}
		
		//D
		if(TABULEIRO[0][0] == PLAYER && TABULEIRO[1][1] == PLAYER && TABULEIRO [2][2] == PLAYER) {
			return PLAYER;
		}
		if(TABULEIRO[2][0] == PLAYER && TABULEIRO[1][1] == PLAYER && TABULEIRO [0][2] == PLAYER) {
			return PLAYER;
		}
		
		//OPONENTE
		
		//H
		if(TABULEIRO[0][0] == OPONENTE && TABULEIRO[1][0] == OPONENTE && TABULEIRO [2][0] == OPONENTE) {
			return OPONENTE;
		}
		if(TABULEIRO[0][1] == OPONENTE && TABULEIRO[1][1] == OPONENTE && TABULEIRO [2][1] == OPONENTE) {
			return OPONENTE;
		}
		if(TABULEIRO[0][2] == OPONENTE && TABULEIRO[1][2] == OPONENTE && TABULEIRO [2][2] == OPONENTE) {
			return OPONENTE;
		}
		
		//v
		if(TABULEIRO[0][0] == OPONENTE && TABULEIRO[0][1] == OPONENTE && TABULEIRO [0][2] == OPONENTE) {
			return OPONENTE;
		}
		if(TABULEIRO[1][0] == OPONENTE && TABULEIRO[1][1] == OPONENTE && TABULEIRO [1][2] == OPONENTE) {
			return OPONENTE;
		}
		if(TABULEIRO[2][0] == OPONENTE && TABULEIRO[2][1] == OPONENTE && TABULEIRO [2][2] == OPONENTE) {
			return OPONENTE;
		}
		
		//D
		if(TABULEIRO[0][0] == OPONENTE && TABULEIRO[1][1] == OPONENTE && TABULEIRO [2][2] == OPONENTE) {
			return OPONENTE;
		}
		if(TABULEIRO[2][0] == OPONENTE && TABULEIRO[1][1] == OPONENTE && TABULEIRO [0][2] == OPONENTE) {
			return OPONENTE;
		}
		
		int curScore = 0;
		for(int xx = 0; xx < TABULEIRO.length;xx++) {
			for(int yy = 0; yy < TABULEIRO.length;yy++) {
				if(TABULEIRO[xx][yy] != 0) {
					curScore++;
				}
			}
		}
		
		if(curScore == TABULEIRO.length * TABULEIRO[0].length) {
			return 0;
		}
		//Velha
		return -10;
	}
	
	public Node getBestMove(int x,int y, int depth, int turno){
		if(checkVictory() == PLAYER) {  
			return new Node(x,y,depth-10,depth);
		}else if(checkVictory() == OPONENTE) {
			return new Node(x,y,10-depth,depth);
		}else if(checkVictory() == 0) {
			return new Node(x,y,0,depth);
		}
		
		List<Node> nodes = new ArrayList<Node>();
		for(int xx = 0; xx < TABULEIRO.length;xx++) {
			for(int yy = 0; yy < TABULEIRO.length;yy++) {
				if(TABULEIRO[xx][yy] == 0) {
					Node node;
					if(turno == PLAYER) {
						TABULEIRO[xx][yy] = PLAYER;
						node = getBestMove(xx,yy,depth+1,OPONENTE);
						TABULEIRO[xx][yy] = 0;
					}else {
						TABULEIRO[xx][yy] = OPONENTE;
						node = getBestMove(xx,yy,depth-1,PLAYER);
						TABULEIRO[xx][yy] = 0;
					}
					nodes.add(node);
				}
			}
		}
		
		Node finalNode = nodes.get(0);
		for(int i = 0; i < nodes.size();i++) {
			Node n = nodes.get(i);
			if(turno == PLAYER) {
				if(n.score > finalNode.score) {
					finalNode = n;
				}
			}else {
				if(n.score < finalNode.score) {
					finalNode = n;
				}
			}
		}
		
		return finalNode;
			
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!menu) {
			pressed = true;
			mx = e.getX();
			my = e.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(menu) {
			if(e.getKeyCode() == KeyEvent.VK_1 || player2 == false) {
				player2 = false;
				selected = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_2) {
				player2 = true;
				selected = true;
			}
		}
		
		if(selected) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				menu = false;
			}
		}

		if(gameover) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				resetTabuleiro();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
}
