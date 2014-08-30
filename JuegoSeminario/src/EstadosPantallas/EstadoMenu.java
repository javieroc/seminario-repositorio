package EstadosPantallas;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class EstadoMenu extends BasicGameState{
	
	private static int menuX = 220;
	private static int menuY = 320;
	
	private int id;
	private Image imgFondo;
	private Image imgBotonJugar, imgBotonSalir;
	private Image imgSelector, imgMusicOn, imgMusicOff;
	private int seleccion;
	private int opcionSeleccionada;
	
	private float jugarScale = 1;
	private float salirScale = 1;	
	private float scaleStep = 0.0001f;
	
	private Music musica;
	private boolean playMusic = true;
	
	public EstadoMenu(int id){
		this.id = id;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		//Cargo la imagen de fondo
		imgFondo = new Image("res/img/fondomenu.jpg");
		
		//Cargo imagen del selector
		Image mapaImagenes = new Image("res/img/mapaimagenes.png");
		imgSelector = mapaImagenes.getSubImage(64, 0, 32, 32);
		
		//Cargo imagenes para la musica.
		imgMusicOn = mapaImagenes.getSubImage(0, 0, 32, 32);
		imgMusicOff = mapaImagenes.getSubImage(32, 0, 32, 32);
		
		//Cargo las imagenes de los botones.
		Image imgBotones = new Image("res/img/botonesmenu.png");
		imgBotonJugar = imgBotones.getSubImage(0, 0, 250, 90);
		imgBotonSalir = imgBotones.getSubImage(0, 180, 250, 90);
		
		//Cargo la musica
		musica = new Music("res/music/trance-menu.wav");
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		musica.play();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		//Dibujo el fondo.
		imgFondo.draw(0,0);
		//Dibujo los botones, un poco hardcodeado :)
		imgBotonJugar.draw(menuX, menuY, jugarScale);
		imgBotonSalir.draw(menuX,menuY+90,salirScale);
		
		//Dibujo el selector
		int desplazamiento = 30;
		if(seleccion == 1){
			imgSelector.draw(menuX - 50, menuY + desplazamiento);
		}else if(seleccion == 2){
			imgSelector.draw(menuX - 50, menuY + 90 + desplazamiento);
		}
		
		//Dibujo los botones para la musica
		if(playMusic){
			imgMusicOn.draw(600, 20);
		}else{
			imgMusicOff.draw(600, 20);
		}
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		//Para el boton jugar
		if(seleccion == 1){
			if(opcionSeleccionada == 1){
				opcionSeleccionada = -1;
				musica.stop();
				game.enterState(1);
			}else{
				if(jugarScale < 1.05f){
					jugarScale += scaleStep * delta;
				}
			}	
		}else{
			if(jugarScale > 1.0f){
				jugarScale -= scaleStep * delta;
			}
		}
		
		//Para el boton salir
		if(seleccion == 2){
			if(opcionSeleccionada == 2){
				opcionSeleccionada = -1;
				System.exit(0);
			}else{
				if(salirScale < 1.05f){
					salirScale += scaleStep * delta;
				}
			}
		}else{
			if(salirScale > 1.0f){
				salirScale -= scaleStep * delta;
			}
		}
		
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		super.mouseClicked(button, x, y, clickCount);
		opcionSeleccionada = seleccion;
		if(x > 600 && x < 632 && y > 20 && y < 52){
			if(playMusic){
				playMusic = false;
				musica.stop();
			}else{
				playMusic = true;
				musica.play();
			}
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		super.mouseMoved(oldx, oldy, newx, newy);
		if(newx > menuX && newx < menuX + 250) {
			//Sobre botón jugar
			if(newy > menuY && newy < menuY + 90) {
				seleccion = 1;
			//Sobre el botón salir	
			}else if(newy > menuY + 90 && newy < menuY+180) {
				seleccion = 2;
			}else{
				seleccion = -1;
			}
		}else{
			seleccion = -1;
		}
	}
	
	@Override
	public int getID() {
		return id;
	}

}
