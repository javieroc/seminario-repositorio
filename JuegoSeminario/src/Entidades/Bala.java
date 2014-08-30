package Entidades;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import controladores.IColisionable;

public abstract class Bala implements IColisionable{
	
	//Variables
	protected int id;//Para poder identificar el tipo de bala.
	
	protected float velocidadBala = 0.2f;//Velocidad para el desplazamiento de la bala.
	
	protected Vector2f posicion, direccion;//Posicion y direccion son dos variables que cambian constantemente.
	
	protected boolean[][] casillasLibres;//Para poder conocer si choca con algún muro.
	
	protected boolean activa = true;//Para saber si está destruida o no.
	
	protected Mapa mapa;
	
	protected Rectangle areaColision;
	
	
	//Constructor
	public Bala(Mapa mapa, Vector2f posicion, Vector2f direccion){
		this.posicion = posicion;
		this.direccion = direccion;
		this.mapa = mapa;
		this.casillasLibres = mapa.getCasillasLibres();
	}
	
	public abstract void dibujar(Graphics g);
	
	public abstract void actualizar(int delta);
	
	public boolean isActiva(){
		return activa;
	}
			
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public float getX(){
		return posicion.x;
	}
	
	public float getY(){
		return posicion.y;
	}
	
	@Override
	public void sincronizarArea() {
		areaColision.setX(posicion.x);
		areaColision.setY(posicion.y);
	}
	
	@Override
	public Shape getAreaColision() {
		return areaColision;
	}

}
