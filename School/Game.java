package real;
import java.awt.Canvas;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.Random;

public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 7249936825495199895L;
	
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static final int WIDTH = (int) screenSize.getWidth();
	public static final int HEIGHT = (int) screenSize.getHeight();
	
	private Thread thread;
	private boolean running = false; 
	
	private Random r;
	private Handler handler;
	private Menu menu;
	private HUD hud;
	
	public enum STATE {
		Menu,
		Game,
		Help,
		Multisalen,
		Canteen,
		Library,
		ParkingLot
	};
	
	public STATE gameState = STATE.Menu;
	
	public Game() {
		handler = new Handler();
		menu = new Menu(this, handler);
		this.addMouseListener(menu);
		hud = new HUD(this,handler);
		this.addMouseListener(hud);
		
		new Window(WIDTH, HEIGHT, "School Manager Game", this);
		
		r = new Random();
		
	}
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
			}
			if(running)
				try {
					render();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	private void tick() {
			handler.tick();
			if(gameState == STATE.Game || gameState == STATE.Canteen || gameState == STATE.Library || gameState == STATE.ParkingLot || gameState == STATE.Multisalen)
			{
				hud.tick();
			}else if(gameState == STATE.Menu || gameState == STATE.Help) {
				menu.tick();
			}	
	}
	private void render() throws IOException {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		handler.render(g);
		
		if(gameState == STATE.Game || gameState == STATE.Canteen || gameState == STATE.Library || gameState == STATE.ParkingLot || gameState == STATE.Multisalen) {
			hud.render(g);
		}else if(gameState == STATE.Menu || gameState == STATE.Help) {
			menu.render(g);
		}
		g.dispose();
		bs.show();
	}
	public static int clamp(int var, int min, int max) {
		if(var>=max) {
			return var = max;
		}
		else if (var<=min) {
			return var = min;
		}
		else
			return var;
	}
	public static void main(String[] args) {
		new Game();
	}
}