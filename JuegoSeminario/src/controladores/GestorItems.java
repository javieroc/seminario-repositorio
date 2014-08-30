package controladores;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import Entidades.Item;

public class GestorItems {
	
	//Variables
	private Random random;
	private int tiempoProxItem;
	private int deltaContador = 0;
	private ArrayList<Item> listaItems;
	private boolean[][] casillasLibres;
	private Image mapaImagenes;
	
	//Constructor
	public GestorItems(boolean[][] casillasLibres) throws SlickException{
		listaItems = new ArrayList<Item>();
		mapaImagenes = new Image("res/img/mapaimagenes.png");
		random = new Random();
		tiempoProxItem = 20000 + random.nextInt(20000);
		this.casillasLibres = casillasLibres;
	}
	
	public void dibujar(){
		for(int i = 0; i < listaItems.size();i++){
			listaItems.get(i).dibujar();
		}
	}
	
	public void actualizar(int delta, GestorColisiones gestorColisiones) throws SlickException{
		for(int i = 0;i < listaItems.size();i++){
			if(listaItems.get(i).isActivo()){
				listaItems.get(i).actualizar(delta);
			}else{
				gestorColisiones.borrarObjeto(listaItems.get(i));
				listaItems.remove(i);
			}
		}
		generarItemRandom(delta, gestorColisiones);
	}
	
	private void generarItemRandom(int delta, GestorColisiones gestorColisiones) throws SlickException{
		deltaContador += delta;
		if(deltaContador > tiempoProxItem){
			//Genero el tipo de item.
			int tipoItem = random.nextInt(7);
			
			//Genero la posicion.
			int i,j;
			do{
				i = random.nextInt(casillasLibres.length);
				j = random.nextInt(casillasLibres[0].length);	
			}while(!casillasLibres[j][i]);
			float posItemX = i * 32;
			float posItemY = j * 32;
			
			//El valor 32 es el ancho y alto de la imagen.
 			Image imgItem = mapaImagenes.getSubImage(tipoItem * 32, 32, 32, 32);
			Item item = new Item(imgItem,tipoItem, new Vector2f(posItemX, posItemY));
			gestorColisiones.registrarObjeto(item);
			listaItems.add(item);
			deltaContador = 0;
		}
		tiempoProxItem = 5000 + random.nextInt(10000);
	}//fin método
	
	public Image getImageItem(int tipoItem){
		switch(tipoItem){
		case 0:
			//Manzana
			return mapaImagenes.getSubImage(0, 32, 32, 32);
			
		case 1:
			//Banana
			return mapaImagenes.getSubImage(1*32, 32, 32, 32);
		
		case 2:
			//Uvas
			return mapaImagenes.getSubImage(2*32, 32, 32, 32);
		
		case 3:
			//Limón
			return mapaImagenes.getSubImage(3*32, 32, 32, 32);
		
		case 4:
			//Naranja
			return mapaImagenes.getSubImage(4*32, 32, 32, 32);
		
		case 5:
			//Pera
			return mapaImagenes.getSubImage(5*32, 32, 32, 32);
			
		case 6:
			//Frutilla
			return mapaImagenes.getSubImage(6*32, 32, 32, 32);
		
		case 7:
			//Corazón
			return mapaImagenes.getSubImage(3*32, 0, 32, 32);
			
		default:
			return null;
		}
	}
	
	
}
