package controladores;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import Entidades.Bala;

public class GestorBalas {
	
	//Variables
	private ArrayList<Bala> listaBalas;
	
	private SpriteSheet spriteExplosion;
	
	private Animation animacionExplosion;
	
	private Vector2f posExplosion;
	
	private boolean dibujarExplosion = false;
	
	
	//Método constructor
	public GestorBalas() throws SlickException{
		listaBalas = new ArrayList<Bala>();
		spriteExplosion = new SpriteSheet("res/img/explosion.png",32,32);
		animacionExplosion = new Animation(spriteExplosion, 100);
		animacionExplosion.setLooping(false);
	}
	
	public void agregar(Bala bala){
		listaBalas.add(bala);
	}
	
	public void dibujar(Graphics g){
		for(int i = 0;i < listaBalas.size();i++){
			listaBalas.get(i).dibujar(g);
		}
		if(dibujarExplosion){
			animacionExplosion.draw(posExplosion.x, posExplosion.y);
			if(animacionExplosion.isStopped()){
				dibujarExplosion = false;
			}
		}
	}
	
	public void actualizar(int delta, GestorColisiones gestorColisiones){
		for(int i = 0;i < listaBalas.size();i++){
			if(listaBalas.get(i).isActiva()){
				listaBalas.get(i).actualizar(delta);
			}else{
				animacionExplosion.restart();
				posExplosion = new Vector2f(listaBalas.get(i).getX() - 10, listaBalas.get(i).getY() - 10);
				dibujarExplosion = true;
				gestorColisiones.borrarObjeto(listaBalas.get(i));
				listaBalas.remove(i);
			}
		}
	}
	
	public int getSize(){
		return listaBalas.size();
	}
	
	
}//Fin clase Gestor Balas



