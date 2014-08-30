package Entidades;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import controladores.IColisionable;

public class BalaNormal extends Bala{
	
	//Variables
	private Image imgBala;//Imagen de la bala.
	
	//Método constructor
	public BalaNormal(Mapa mapa, Vector2f posicion, Vector2f direccion) throws SlickException {
		super(mapa, posicion, direccion);
		imgBala = new Image("res/img/bala.png");
		areaColision = new Rectangle(posicion.x, posicion.y, imgBala.getWidth(), imgBala.getHeight());
	}
	
	@Override
	public void dibujar(Graphics g) {
		imgBala.draw(posicion.x-5, posicion.y-5);
	}
	
	@Override
	public void actualizar(int delta) {
		intentarMover(delta);
		sincronizarArea();
	}
	
	/*
	 * Intenta mover la bala a una nueva posición, si no lo logra la desactiva.
	 */
	private void intentarMover(int delta){
		float proxPosX = posicion.x + direccion.x * delta * velocidadBala;
		float proxPosY = posicion.y + direccion.y * delta * velocidadBala;
		
		int j = (int)(proxPosX / 32);
		int i = (int)(proxPosY / 32);
		
		try{
			if(!casillasLibres[i][j]){
				
				activa = false;
				mapa.borrarCuadrito(j, i);
				
			}else{
			
				posicion.set(proxPosX, proxPosY);
				
			}
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
	}

	@Override
	public void alColisionar(IColisionable colision) {
		if(!(colision instanceof Item)){
			activa = false;
		}
	}





	

}
