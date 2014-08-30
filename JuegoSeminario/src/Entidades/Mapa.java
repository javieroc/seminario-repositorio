package Entidades;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Mapa {
	
	//Variables
	private TiledMap mapa;
	
	private boolean casillasLibres[][];
	
	private int[][] resistenciaCasillas; 
	
	private int anchoEnTiles;
	
	private int altoEnTiles;
	
	//Constructor
	public Mapa(String ruta) throws SlickException{
		mapa = new TiledMap(ruta);//Instanciamos un objeto del a clase TiledMap
		anchoEnTiles = mapa.getWidth();
		altoEnTiles = mapa.getHeight();
		casillasLibres = new boolean[altoEnTiles][anchoEnTiles];
		resistenciaCasillas = new int[altoEnTiles][anchoEnTiles];
		llenarCasillasLibres();
	}//fin constructor
	
	
	/*
	 * Cargar la matriz booleana de casillas libres
	 */
	private void llenarCasillasLibres(){
		int capaID = mapa.getLayerIndex("collision");
		for(int i = 0;i < altoEnTiles;i++){
			for(int j = 0;j < anchoEnTiles;j++){
				int tileID = mapa.getTileId(j, i, capaID);
				if(tileID != 0){
					casillasLibres[i][j] = false;
					resistenciaCasillas[i][j] = 2;
				}else{
					casillasLibres[i][j] = true;
				}
			}
		}
	}//fin método
	
	
	/*
	 * Dibujar la capa especificada por parámetro
	 */
	public void dibujarCapa(int numCapa){
		int idCapa;
		switch(numCapa){
		case 1:
			idCapa = mapa.getLayerIndex("background");
			mapa.render(0, 0, idCapa);
			break;
		case 2:
			idCapa = mapa.getLayerIndex("collision");
			mapa.render(0,0,idCapa);
			break;
		case 3:
			idCapa = mapa.getLayerIndex("object");
			mapa.render(0, 0, idCapa);
			break;
		}
	}//fin método
	
	
	//Getters
	public boolean[][] getCasillasLibres(){
		return casillasLibres;
	}//fin método
	

	public void borrarCuadrito(int columna, int fila){
		if(mapa.getTileId(columna, fila, mapa.getLayerIndex("collision")) != 16){	
			resistenciaCasillas[fila][columna]--;
			if(resistenciaCasillas[fila][columna] == 0){
				mapa.setTileId(columna, fila, mapa.getLayerIndex("collision"), 0);
				casillasLibres[fila][columna] = true;
			}else{
				switch(mapa.getTileId(columna, fila, mapa.getLayerIndex("collision"))){
				case 15:
					mapa.setTileId(columna, fila, mapa.getLayerIndex("collision"), 13);
					break;
				case 21:
					mapa.setTileId(columna, fila, mapa.getLayerIndex("collision"), 22);
					break;
				case 23:
					mapa.setTileId(columna, fila, mapa.getLayerIndex("collision"), 24);
					break;
				case 28:
					mapa.setTileId(columna, fila, mapa.getLayerIndex("collision"), 18);
					break;
				
				}
			}
		}
	}//fin método
	
	//Dibuja todas las capas de una sola vez.
	public void dibujarMapa(){
		mapa.render(0, 0);
	}//fin método
	
	public int getHeightMapa(){
		return mapa.getHeight();
	}
	
	public int getWidthMapa(){
		return mapa.getWidth();
	}
	
	public int getSizeTileMapa(){
		return mapa.getTileHeight();
	}
	
}
