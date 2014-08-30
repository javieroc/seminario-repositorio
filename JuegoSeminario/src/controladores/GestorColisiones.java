package controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Entidades.Tanque;

public class GestorColisiones {
	
	//Variables
	/*
	 * Key = 1 -> lista con el tanque jugador y sus balas.
	 * key = 2..n -> listas con los tanques enemigos y sus respectivas balas.
	 */
	private Map<Integer, ArrayList<IColisionable>> colisionables;

	
	//Constructor
	public GestorColisiones(){
		colisionables = new HashMap<Integer, ArrayList<IColisionable>>();
		colisionables.put(1, new ArrayList<IColisionable>());
		colisionables.put(2, new ArrayList<IColisionable>());
	}
	
	public synchronized void registrarObjeto(IColisionable objetoColisionable){
		ArrayList<IColisionable> listaObjetos = colisionables.get(objetoColisionable.getID());
		listaObjetos.add(objetoColisionable);
	}
	
	public synchronized void borrarObjeto(IColisionable objetoColisionable){
		ArrayList<IColisionable> listaColisionables = colisionables.get(objetoColisionable.getID());
		if (listaColisionables != null) {
			//Si lo que se borra es un tanque se debe borrar todas sus balas.
			//if(objetoColisionable instanceof Tanque){
				//Remuevo toda la lista
				//colisionables.remove(objetoColisionable.getID());
			//}else{
				listaColisionables.remove(objetoColisionable); 		
			//}
		}
	}//Fin borarObjeto

	public synchronized void comprobarColisiones(){
		//El id 1 corresponde a la lista de objetos del jugador
		ArrayList<IColisionable> objetosJugador = colisionables.get(1);
		ArrayList<IColisionable> objetosEnemigos;
		for(int i = 0;i < objetosJugador.size();i++){
			
			Iterator iter = colisionables.keySet().iterator();
			while(iter.hasNext()){
				
				Integer key = (Integer) iter.next();
				if(key != 1){
					objetosEnemigos = colisionables.get(key);
					
					for(int j = 0; j < objetosEnemigos.size();j++){
						if(objetosJugador.get(i).getAreaColision().intersects(objetosEnemigos.get(j).getAreaColision())){
							objetosJugador.get(i).alColisionar(objetosEnemigos.get(j));
							objetosEnemigos.get(j).alColisionar(objetosJugador.get(i));
						}
					}
				}
					
			}
		}
	}//fin comprobarColisiones
	
	public int getSizeColisionables(){
		return colisionables.size();
	}
	
	public int getSizeObjetosEnemigos(){
		return colisionables.get(2).size();
	}
	
}
