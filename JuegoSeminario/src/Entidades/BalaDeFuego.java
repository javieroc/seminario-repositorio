package Entidades;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import controladores.IColisionable;

public class BalaDeFuego extends Bala{
	
	//Variables
	private SpriteSheet spriteBala;
	private Animation animacionBala;

	public BalaDeFuego(Mapa mapa, Vector2f posicion, Vector2f direccion) throws SlickException {
		super(mapa, posicion, direccion);
		spriteBala = new SpriteSheet("res/img/bolaDeFuego.png", 16, 16);
		animacionBala = new Animation(spriteBala, 100);
		animacionBala.setAutoUpdate(false);
	}

	@Override
	public void dibujar(Graphics g) {
		animacionBala.draw(posicion.x-5, posicion.y-5);
	}

	@Override
	public void actualizar(int delta) {
		tratarMover(delta);
	}
	
	private void tratarMover(int delta){
		float proxPosX = posicion.x + direccion.x * delta * velocidadBala;
		float proxPosY = posicion.y + direccion.y * delta * velocidadBala;
		
		int j = (int)(proxPosX / 32);
		int i = (int)(proxPosY / 32);
		
		try{
			if(!casillasLibres[i][j]){
				
				activa = false;
				
			}else{
				animacionBala.update(delta);
				posicion.set(proxPosX, proxPosY);
				
			}
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
	}//fin método
	
	@Override
	public void alColisionar(IColisionable colision) {
		
	}

}
