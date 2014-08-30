package Entidades;

import java.util.Stack;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import controladores.AlgoritmoPathfinding;
import controladores.GestorBalas;
import controladores.GestorColisiones;
import controladores.IColisionable;
import controladores.Vertice;

public class TanqueEnemigo extends Tanque{
	
	//Variables
	private final int TiempoParaDisparar = 3000;//El tanque disparara cada 3 segundos.
	
	private final int TiempoRecalcularDestino = 3000;
	
	private float angDestino, angAnterior;
	
	//El vector destino es el nodo destino al que debe dirigirse. Este nodo cambia cada cierta
	//cantidad de tiempo para poder perseguir al tanque enemigo.
	private Vector2f destino, siguientePunto;
	
	private Stack<Vertice> caminoMinimo;//Contiene los puntos de una ruta mínima calculada.
	
	/*
	 * DistanciaRecorrer, sera la distancia del pto actual en el que me encuetro,
	 * hasta el siguiente punto de la ruta mínima.
	 * DistanciaRecorrida, es la distancia que ya recorrimos en ese camino desde el pto 
	 * actual al siguiente pto.
	 */
	private float distanciaRecorrer = 0, distanciaRecorrida = 0;
	
	/*
	 * Ambas variables acumulan tiempo transcurrido desde que ocurren ciertos eventos,
	 * deltaContador cuenta el tiempo desde la última vez que se disparo y cronoRecalcular
	 * el tiempo que transcurre desde la última vez que se calculó una ruta mínima.
	 */
	private int cronoDisparar, cronoRecalcular;
	
	private boolean hayCamino = false;
	
	//Método constructor
	public TanqueEnemigo(Mapa mapa, String ruta, Vector2f posicionInicial,
			GestorBalas gestorBalas,Vector2f destino,GestorColisiones gestorColisiones) throws SlickException {
		super(mapa, ruta, posicionInicial, gestorBalas,gestorColisiones);
		this.destino = destino;
		velocidadMovimiento = 0.058f;
		velocidadRotacion = 0.2f;
		this.id = 2;
		obtenerCaminoMinimo();//Calculamos el camino minimo.
		obtenerSiguientePunto();//Obtenemos el siguiente punto de nuestro camino para llegar al destino.
		calcularDistanciaRecorrer();
	}

	private void obtenerCaminoMinimo(){
		int i = (int) (posicion.y / 32);
		int j = (int) (posicion.x / 32);
		Vertice VerticeOrigen = new Vertice(i, j);
		i = (int) (destino.y / 32);
		j = (int) (destino.x / 32);
		Vertice VerticeDestino = new Vertice(i, j);
		AlgoritmoPathfinding algoritmoAStar = new AlgoritmoPathfinding(casillasLibres, VerticeOrigen, VerticeDestino);
		if(algoritmoAStar.ejecutarAlgoritmo()){
			caminoMinimo = algoritmoAStar.getCaminoMinimo();
			hayCamino = true;
		}else{
			caminoMinimo = null;
			hayCamino = false;
		}
	}
	
	private void obtenerSiguientePunto(){
		if(!caminoMinimo.isEmpty()){
			Vertice siguienteNodo = caminoMinimo.pop();
			siguientePunto = new Vector2f(siguienteNodo.j * 32 + 16, siguienteNodo.i * 32 + 16);
		}
	}
	
	/*
	 * Actualiza la lógica del tanque.
	 */
	public void actualizar(int delta, Vector2f posPlayer) throws SlickException{
		cronoRecalcular += delta;//Avanzamos el reloj para recalcular.
		cronoDisparar += delta;//Avanzamos el reloj para disparar.
		//Preguntamos si es hora de recalcular el destino
		if(cronoRecalcular >= TiempoRecalcularDestino){
			//Preguntamos si llegamos a un punto del camino. No podemos recalcular si 
			//el tanque está a mitad de un trayecto entre dos puntos.
			if(distanciaRecorrida == 0){
				//calculamos el nuevo camino mínimo
				this.destino = posPlayer;//el destino es la posicion del tanque del jugador.
				caminoMinimo.clear();//Limpiamos la pila.
				obtenerCaminoMinimo();
				obtenerSiguientePunto();//Obtenemos el siguiente punto.
				calcularAnguloDestino();//Obtenemos el angulo para alinear el cañon.
				calcularDistanciaRecorrer();//Calculamos la distancia a recorrer hasta llegar al siguiente punto.
				cronoRecalcular = 0;
			}else{
				//Si estamos a mitad de camino seguimos avanzando.
				avanzar(delta);
			}
		}else{
			avanzar(delta);
			if(cronoDisparar > TiempoParaDisparar){
				disparar();
				cronoDisparar = 0;
			}
		}
		sincronizarArea();
	}//Fin método actualizar
	
	private void avanzar(int delta){
		//Preguntamos si No llegamos a destino, es decir si todavia debemos seguir recorriendo camino.
		if(posicion.x != destino.x | posicion.y != destino.y){
			//Preguntamos si llegamos a un check point, es decir a un punto en concreto.
			if(posicion.x == siguientePunto.x && posicion.y == siguientePunto.y){
				obtenerSiguientePunto();//Obtenemos el siguiente punto.
				calcularAnguloDestino();//Obtenemos el angulo para alinear el cañon.
				calcularDistanciaRecorrer();//Calculamos la distancia a recorrer hasta llegar al siguiente punto.
			}else if(ang == angDestino){
				//Si el cañon está alineado avanzamos.
				posicion.x += delta * direccion.x * velocidadMovimiento;
				posicion.y += delta * direccion.y * velocidadMovimiento;
				animacion.update(delta);
				distanciaRecorrida += delta * velocidadMovimiento;
				if(distanciaRecorrida > distanciaRecorrer){
					posicion.x = siguientePunto.x;
					posicion.y = siguientePunto.y;
					distanciaRecorrida = 0;
				}
			}else{
				girar(delta);//Giramos el cañon
				actualizarDireccion();
			}
		}
	}
	
	/*
	 * Calcula el ángulo destino al cual tenemos que llegar girando.
	 */
	private void calcularAnguloDestino(){
		angAnterior = ang;
		float dx = (siguientePunto.x - posicion.x);
		float dy = (siguientePunto.y - posicion.y);
		float radianes = (float) Math.atan2(dy, dx);
		float grados = (float) ((180 * radianes)/(Math.PI));
		grados = (grados < 0) ? (360 - (Math.abs(grados) % 360) ) % 360 : (grados % 360);
		angDestino = ((grados + 90) % 360 == 0) ? 360 : (grados + 90) % 360;
	}
	
	/*
	 * Calcula la distancia a recorrer desde un punto a otro.
	 */
	private void calcularDistanciaRecorrer(){
		float diferenciax = siguientePunto.x - posicion.x;
		float diferenciay = siguientePunto.y - posicion.y;
		distanciaRecorrer = (float) Math.sqrt(diferenciax*diferenciax+diferenciay*diferenciay);
	}
	
	/*
	 * Hace que el tanque gire.
	 */
	private void girar(int delta){
		ang = angDestino;
	}
	
	
	/*
	 * Actualiza la dirección en la que se tiene que mover el tanque de acuerdo al ángulo de giro.
	 */
	private void actualizarDireccion(){
		this.direccion.set((float) Math.sin(Math.toRadians(ang)), (float) -Math.cos(Math.toRadians(ang)));
	}
	
	
	@Override
	public void alColisionar(IColisionable colision) {
		activo = false;
		
	}


	
	

}
