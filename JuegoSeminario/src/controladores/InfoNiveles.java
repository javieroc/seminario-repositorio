package controladores;

public class InfoNiveles {
	
	/*
	 * La primera coordenada es el nivel, la segunda el id del item y la tercera
	 * la cantidad de ese item que se tendra que conseguir.
	 */
	public static final int[][][] objetivos = {
									{{0,1},{1,1},{2,1}},
									{{3,1},{4,1},{5,2}},
									{{6,2},{0,2},{1,2}},
									{{2,2},{3,2},{4,3}},
								};
	
	public static final String[] rutas = {"res/mapas/mapa1.tmx","res/mapas/mapa2.tmx","res/mapas/mapa3.tmx", "res/mapas/mapa4.tmx"};
	
	public static final String[] rutasMusica = {"res/music/BattleEscape.wav","res/music/awake.wav","res/music/FightingBack.wav","res/music/YouOnlyGetOneBeat.wav"};
	
	public static final int[] limitesEnemigos = {3,3,4,5};
	
	public static int[][] puntosNacimientosEnemigos = {{1,1},{10,1},{19,1}};
}
