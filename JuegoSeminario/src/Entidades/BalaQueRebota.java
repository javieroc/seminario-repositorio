package Entidades;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import controladores.IColisionable;

public class BalaQueRebota extends Bala{
	
	//Variables
	private Image imgPelota;

	//Constructor
	public BalaQueRebota(Mapa mapa, Vector2f posicion, Vector2f direccion) throws SlickException {
		super(mapa, posicion, direccion);
		imgPelota = new Image("res/img/pelota.png");
	}
	
	@Override
	public void dibujar(Graphics g) {
		imgPelota.draw(posicion.x-5, posicion.y-5);
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
				if(!casillasLibres[i][(int)(posicion.x/32)]){
					if(!casillasLibres[(int)(posicion.y/32)][j]){
						direccion.x = direccion.x * -1;
						direccion.y = direccion.y * -1;
						//sonido.play();		
					}else{
						direccion.y = direccion.y * -1;
						//sonido.play();
					}
				}else{
					direccion.x = direccion.x * -1;
					//sonido.play();
				}
			}else{
				posicion.set(proxPosX, proxPosY);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
	}//fin método
	
	@Override
	public void alColisionar(IColisionable colision) {
		// TODO Auto-generated method stub
		
	}

}
