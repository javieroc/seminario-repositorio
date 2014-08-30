package Entidades;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import controladores.GestorBalas;
import controladores.GestorColisiones;
import controladores.IColisionable;

public abstract class Tanque implements IColisionable{
	
	//Variables
	protected int id;//para identificar el tipo de entidad.
	
	protected Animation animacion;//Gestiona la animación del tanque.
	
	protected float velocidadMovimiento;
	
	protected float velocidadRotacion;
	
	protected SpriteSheet spriteTanque;//Guarda el tileset del tanque.
	
	protected float ang = 0;//Ángulo donde apunta el tanque.
	
	protected Vector2f posicion, direccion;//Vectores para las coordenadas de posicion y direccion
	
	protected Mapa mapa;//Mapa para guiarse.
	
	protected boolean[][] casillasLibres;//Casillas por donde se puede circular.
	
	protected Rectangle areaColision;
	
	protected GestorBalas gestorBalas;
	
	protected GestorColisiones gestorColisiones;
	
	protected boolean activo = true;
	
	//Método Constructor
	public Tanque(Mapa mapa, String ruta, Vector2f posicionInicial, 
			GestorBalas gestorBalas, GestorColisiones gestorColisiones) throws SlickException{
		
		this.mapa = mapa;
		this.gestorBalas = gestorBalas;
		this.gestorColisiones = gestorColisiones;
		casillasLibres = mapa.getCasillasLibres();
		this.posicion = posicionInicial;
		direccion = new Vector2f((float) Math.sin(Math.toRadians(ang)), (float) -Math.cos(Math.toRadians(ang)));
		spriteTanque = new SpriteSheet(ruta,32,32);
		animacion = new Animation(spriteTanque ,150);
		animacion.setAutoUpdate(false);
		areaColision = new Rectangle(posicionInicial.getX()-16, posicionInicial.getY()-16, 28, 28);
	}
	
	/**
	 * Metodos getters y setters
	 */
	public float getVelocidadMovimiento() {
		return velocidadMovimiento;
	}


	public void setVelocidadMovimiento(float velocidadMovimiento) {
		this.velocidadMovimiento = velocidadMovimiento;
	}


	public Vector2f getPosicion() {
		return posicion;
	}


	public void setPosicion(Vector2f posicion) {
		this.posicion = posicion;
	}


	public Vector2f getDireccion() {
		return direccion;
	}


	public void setDireccion(Vector2f direccion) {
		this.direccion = direccion;
	}


	public Mapa getMapa() {
		return mapa;
	}


	public void setMapa(Mapa mapa) {
		this.mapa = mapa;
	}

	
	public int getID() {
		return id;
	}


	public void setID(int id) {
		this.id = id;
	}
	//Fin métodos getters y setters
	
	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
	 * Metodo que dibuja el tanque en pantalla
	 */
	public void dibujar(Graphics g){
			int cx = (int)posicion.getX();
			int cy = (int)posicion.getY();
			g.rotate(cx, cy, ang);
			animacion.draw(cx-16, cy-16);
			g.rotate(cx, cy, -ang);
	}
	
	
	public void disparar() throws SlickException{
		Bala nuevaBala = new BalaNormal(mapa, new Vector2f(posicion.x,posicion.y), new Vector2f(direccion.x,direccion.y));
		nuevaBala.setID(id);//Necesario para las colisiones.
		gestorBalas.agregar(nuevaBala);
		gestorColisiones.registrarObjeto(nuevaBala);
	}
	
	//Métodos implementados de la interfaz IColisionable, 
	//falta implementar 1 método que se implementa en las clases hijas
	@Override
	public void sincronizarArea() {
		areaColision.setX(posicion.getX()-16);
		areaColision.setY(posicion.getY()-16);
		
	}
	
	@Override
	public Shape getAreaColision() {
		return areaColision;
	}
	
}//Fin clase Tanque.
