package Entidades;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;

import controladores.GestorBalas;
import controladores.GestorColisiones;
import controladores.IColisionable;

public class TanquePlayer extends Tanque{
	
	//Variables
	private int vidas = 3;
	
	private int[][] objetivos;
	
	private Sound soundDisparo;
	
	private Vector2f posicionInicial;
	
	//constructor
	public TanquePlayer(Mapa mapa, String ruta, Vector2f posicionInicial, GestorBalas gestorBalas, 
			GestorColisiones gestorColisiones, int[][] objetivos) throws SlickException {
		super(mapa, ruta, posicionInicial,gestorBalas, gestorColisiones);
		velocidadMovimiento = 0.1f;
		velocidadRotacion = 0.2f;
		this.id = 1;
		this.objetivos = objetivos;
		this.posicionInicial = new Vector2f(posicionInicial.x, posicionInicial.y);
		soundDisparo = new Sound("res/music/shoot.wav");
	}
	
	private void actualizarDireccion(){
		this.direccion.set((float) Math.sin(Math.toRadians(ang)), (float) -Math.cos(Math.toRadians(ang)));
	}
	
	public void actualizar(int delta, Input entrada) throws SlickException{
		movimientoTanque(delta,entrada);
		sincronizarArea();
	}
	
	/*
	 * Controla el movimiento del tanque según las entradas por teclado o joystick.
	 */
	private void movimientoTanque(int delta, Input entrada) throws SlickException{
		float incrementoX;
		float incrementoY;
		if(entrada.isKeyDown(Input.KEY_UP) || entrada.isControllerUp(0)){
			incrementoX = direccion.x * delta * velocidadMovimiento;
			incrementoY = direccion.y * delta * velocidadMovimiento;
			if(intentarMover(incrementoX, incrementoY)){
				animacion.update(delta);
			}
		}
		if(entrada.isKeyDown(Input.KEY_DOWN) || entrada.isControllerDown(0)){
			incrementoX = -direccion.x * delta * velocidadMovimiento;
			incrementoY = -direccion.y * delta * velocidadMovimiento;
			if(intentarMover(incrementoX, incrementoY)){
				animacion.update(delta);
			}
		}
		if(entrada.isKeyDown(Input.KEY_LEFT) || entrada.isControllerLeft(0)){
			ang -= delta * velocidadRotacion;
			actualizarDireccion();
		}
		if(entrada.isKeyDown(Input.KEY_RIGHT) || entrada.isControllerRight(0)){
			ang += delta * velocidadRotacion;
			actualizarDireccion();
		}
		if(entrada.isKeyPressed(Input.KEY_SPACE)){
			disparar();
			soundDisparo.play();
		}
	}
	
	/*
	 * Devuelve verdadero si se pudo mover a la nueva posición, y falso en caso contrario.
	 */
	private boolean intentarMover(float x, float y){
		int posi = (int)((posicion.y + y) / 32);
		int posj = (int)((posicion.x + x) / 32);
		if(!casillasLibres[posi][posj]){
			if(!casillasLibres[posi][(int)(posicion.x/32)]){
				if(!casillasLibres[(int)(posicion.y/32)][posj]){
					return false;
				}else{
					posicion.x = posicion.x + x;
					return true;
				}
			}else{
				posicion.y = posicion.y + y;
				return true;
			}
		}else{
			posicion.x = posicion.x + x;
			posicion.y = posicion.y + y;
			return true;
		}
	}
	
	/*
	 * Acción que debe realizar el tanque cuando colisiona contra otro objeto.
	 */
	@Override
	public void alColisionar(IColisionable colision) {
		if(colision instanceof Item){
			for(int i = 0;i < objetivos.length;i++){
				if(objetivos[i][0] == ((Item) colision).getTipoItem() && objetivos[i][1] > 0){
					objetivos[i][1]--;
				}
			}
		}else{
			vidas--;
			activo = false;
		}
	}
	
	public void reiniciarParametros(){
		posicion.set(posicionInicial);
		ang = 0;
		actualizarDireccion();
		activo = true;
	}
	
	public int getVidas(){
		return vidas;
	}
	
	public boolean isObjetivosCompletados(){
		int cont = 0;
		for(int i = 0;i < objetivos.length;i++){
			cont += objetivos[i][1];
		}
		if(cont <= 0){
			return true;
		}else{
			return false;
		}
	}

}
