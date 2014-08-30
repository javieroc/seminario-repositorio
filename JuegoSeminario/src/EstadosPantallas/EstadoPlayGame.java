package EstadosPantallas;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import controladores.GestorBalas;
import controladores.GestorColisiones;
import controladores.GestorItems;
import controladores.InfoNiveles;

import Entidades.Mapa;
import Entidades.TanqueEnemigo;
import Entidades.TanquePlayer;

public class EstadoPlayGame extends BasicGameState{
	
	private static final int TiempoCrearEnemigo = 10000;
	
	//Variables
	private int cronoCrearEnemigo = 0;
	
	private int nivel = 0;
	
	private int id;//Para identificar el estado.
	
	private Mapa mapa;//Clase gestora del mapa.
	
	private TanquePlayer tanquePlayer;
	
	private ArrayList<TanqueEnemigo> listaTanques;
	
	private TanqueEnemigo tanqueEnemigo;
	
	private GestorBalas gestorBalas;
	
	private GestorItems gestorItems;
	
	private Input input;
	
	private GestorColisiones gestorColisiones;
	
	private Music musica;
	
	private boolean pausa = false, playExplosion = false;
	
	private int[][] objetivos;
	
	private Sound soundExplosion;
	
	//Constructor
	public EstadoPlayGame(int id){
		this.id = id;
		
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		input = container.getInput();
		listaTanques = new ArrayList<TanqueEnemigo>();
		mapa = new Mapa(InfoNiveles.rutas[nivel]);
		gestorBalas = new GestorBalas();
		gestorItems = new GestorItems(mapa.getCasillasLibres());
		gestorColisiones = new GestorColisiones();
		musica = new Music(InfoNiveles.rutasMusica[nivel]);
		soundExplosion = new Sound("res/music/explosion.wav");
		objetivos = InfoNiveles.objetivos[nivel];
		musica.loop();
		Vector2f posicionJugador = new Vector2f(8 * 32 + 16, 19 * 32 + 16);
		tanquePlayer = new TanquePlayer(mapa, "res/img/spriteTanqueAzul.png", posicionJugador,gestorBalas,gestorColisiones, objetivos);
		gestorColisiones.registrarObjeto(tanquePlayer);
		crearEnemigo();
	}
	
	private void crearEnemigo() throws SlickException{
		Random r = new Random();
		int indice = r.nextInt(3);//genera 0,1,2
		float posicionX = InfoNiveles.puntosNacimientosEnemigos[indice][0] * 32 + 16;
		float posicionY = InfoNiveles.puntosNacimientosEnemigos[indice][1] * 32 + 16;
		Vector2f destino = tanquePlayer.getPosicion();
		tanqueEnemigo = new TanqueEnemigo(mapa,"res/img/spriteTanqueRojo.png", new Vector2f(posicionX, posicionY), gestorBalas, destino, gestorColisiones);
		listaTanques.add(tanqueEnemigo);
		gestorColisiones.registrarObjeto(tanqueEnemigo);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		mapa.dibujarCapa(1);
		mapa.dibujarCapa(2);
		tanquePlayer.dibujar(g);
		for(int i = 0;i < listaTanques.size();i++){
			listaTanques.get(i).dibujar(g);
		}
		//tanqueEnemigo.dibujar(g);
		gestorBalas.dibujar(g);
		gestorItems.dibujar();
		mapa.dibujarCapa(3);
		dibujarHUD(g);
		//g.drawString("size colisionables:"+gestorColisiones.getSizeColisionables(), 200, 200);
		//g.drawString("size objetos enemigos:"+gestorColisiones.getSizeObjetosEnemigos(), 200, 220);
		if(pausa){
	        Color trans = new Color(0f,0f,0f,0.5f);
	        g.setColor(trans);
	        g.fillRect(0,0, container.getWidth(), container.getHeight());
	        g.setColor(Color.white);
	        g.drawString("Presionar enter: volver juego", 200, 250);
		}
	}
	
	private void dibujarHUD(Graphics g){
		Color trans = new Color(1f,1f,1f,0.5f);
        g.setColor(trans);
		g.fillRect(32, 3, 608, 25);
		g.setColor(Color.black);
		//Dibujo el corazoncito
		gestorItems.getImageItem(7).draw(585, 5);
		g.drawString("X"+tanquePlayer.getVidas(), 612, 8);
		
		//DIbujo los objetivos
		g.drawString("OBJETIVOS:", 42, 8);
		for(int i = 0;i < objetivos.length;i++){
			gestorItems.getImageItem(objetivos[i][0]).draw(60 + 80 * (i+1), 1);
			g.drawString("X"+objetivos[i][1], 90 + 80 * (i+1), 8);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		if(!pausa){
			if(input.isKeyPressed(Input.KEY_ENTER)){
				pausa = true;
			}
			
			//Actualizo los tanques.
			if(tanquePlayer.isActivo()){
				tanquePlayer.actualizar(delta, input);
			}else{
				playExplosion = true;
				tanquePlayer.reiniciarParametros();
			}
			for(int i = 0;i < listaTanques.size();i++){
				if(listaTanques.get(i).isActivo()){
					listaTanques.get(i).actualizar(delta, tanquePlayer.getPosicion());
				}else{
					gestorColisiones.borrarObjeto(listaTanques.get(i));
					listaTanques.remove(i);
				}
			}
			
			//Compruebo colisiones
			gestorColisiones.comprobarColisiones();
			
			//Actualizo las balas y los items
			gestorBalas.actualizar(delta, gestorColisiones);
			gestorItems.actualizar(delta, gestorColisiones);
			
			//Crear Enemigos.
			cronoCrearEnemigo += delta;
			if(cronoCrearEnemigo >= TiempoCrearEnemigo && 
					listaTanques.size() < InfoNiveles.limitesEnemigos[nivel]){	
				crearEnemigo();
				cronoCrearEnemigo = 0;
			}
			
			//Lógica del juego
			//Si se completaron los objetivos y seguimos vivos, avanzamos de nivel
			if(tanquePlayer.isObjetivosCompletados() && tanquePlayer.getVidas() > 0){
				//Avanzamos de nivel
				if(nivel <= 3){
					nivel++;
					musica.stop();
					game.enterState(Principal.ESTADOPLAYJUEGO);
				}else{
					nivel = 0;
					musica.stop();
					game.enterState(Principal.ESTADOMENUPRINCIPAL);
				}
			}else if(tanquePlayer.getVidas() <= 0){
				//Perdimos todas las vidas
				nivel = 0;
				musica.stop();
				game.enterState(Principal.ESTADOGAMEOVER);
			}
			
			if(playExplosion){
				soundExplosion.play();
				playExplosion = false;
			}
			
		}else{
			if(input.isKeyPressed(Input.KEY_ENTER)){
				pausa = false;
			}
		}
	}

	@Override
	public int getID() {
		return id;
	}

}
