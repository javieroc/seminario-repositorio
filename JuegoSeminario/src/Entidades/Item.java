package Entidades;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import controladores.IColisionable;

public class Item implements IColisionable{

	//Variables
	private int id;//Es el mismo para todos los items
	private Image imgItem;
	private Vector2f posicion;
	private int tiempoEnPantalla = 30000;//Cuanto tiempo permane en pantalla.
	private int tipoItem;//Para distinguir el tipo de item. Ej: 1->Manzana, 2->Banana
	private Rectangle areaColision;
	private boolean activo = true;
	
	//Constructor
	public Item(Image imgItem, int tipoItem, Vector2f posicion){
		this.imgItem = imgItem;
		this.tipoItem = tipoItem;
		this.posicion = posicion;
		this.id = 2;
		int ancho = imgItem.getWidth();
		int alto = imgItem.getHeight();
		areaColision = new Rectangle(posicion.x, posicion.y, ancho, alto);
	}

	public void dibujar(){
		imgItem.draw(posicion.x, posicion.y);
	}
	
	public void actualizar(int delta){
		tiempoEnPantalla -= delta;
		if(tiempoEnPantalla < 0){
			activo = false;
		}
	}
	
	@Override
	public Shape getAreaColision() {
		return areaColision;
	}

	@Override
	public void alColisionar(IColisionable colision) {
		if(!(colision instanceof Bala)){
			activo = false;
		}
	}

	@Override
	public void sincronizarArea() {
		
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void setID(int id) {
		this.id = id;
	}
	
	public int getTipoItem() {
		return tipoItem;
	}

	public void setTipoItem(int tipoItem) {
		this.tipoItem = tipoItem;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}
