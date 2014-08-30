package controladores;

import org.newdawn.slick.geom.Shape;

public interface IColisionable {
	
	public Shape getAreaColision();
	
	public void alColisionar(IColisionable colision);
		
	public void sincronizarArea();
	
	public int getID();
	
	public void setID(int id);
}