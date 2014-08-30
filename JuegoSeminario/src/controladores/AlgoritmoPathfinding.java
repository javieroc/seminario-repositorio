package controladores;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class AlgoritmoPathfinding {
	
	//Variables de instancia
	private boolean[][] mapa;//representación simplificada del mapa
	
	/*
	 * La lista abierta contiene los nodos candidatos,
	 * La lista cerrada contiene los nodos ya seleccionados y que nunca más se pondrá a prueba
	 * su permanencia.
	 * La lista cerrada no es el camino mínimo pero lo contiene.
	 */
	private ArrayList<Vertice> listaAbierta, listaCerrada;
	
	private Vertice VerticeOrigen, VerticeDestino, VerticeActual;
	
	private Stack<Vertice> caminoMinimo;
	
	
	//Método Constructor
	public AlgoritmoPathfinding(boolean[][] mapa, Vertice VerticeOrigen, Vertice VerticeDestino){
		caminoMinimo = new Stack<Vertice>();
		listaAbierta = new ArrayList<Vertice>();
		listaCerrada = new ArrayList<Vertice>();
		this.mapa = mapa;
		this.VerticeOrigen = VerticeOrigen;
		this.VerticeDestino = VerticeDestino;
		
	}
	
	
	public Stack<Vertice> getCaminoMinimo(){
		return caminoMinimo;
	}
	
	
	public Vertice algoritmo(){
		
		VerticeActual = null;
		listaAbierta.add(VerticeOrigen);
		
		do{
			VerticeActual = obtenerVerticeConMenorF(listaAbierta);
			listaCerrada.add(VerticeActual);
					
			//Preguntamos basicamente si el Vertice actual es el Vertice destino.
			if(VerticeActual.i == VerticeDestino.i && VerticeActual.j == VerticeDestino.j){
				
				return VerticeActual;
			
			}else{
				
				ArrayList<Vertice> adyacentes = obtenerAdyacentes(VerticeActual);
				
				for(int i = 0;i < adyacentes.size();i++){
					
					if(!loContiene(listaAbierta, adyacentes.get(i)) && !loContiene(listaCerrada, adyacentes.get(i))){
			
						setEcuaciones(VerticeActual, adyacentes.get(i));
						adyacentes.get(i).setPadre(VerticeActual);
						listaAbierta.add(adyacentes.get(i));
					
					}else if(loContiene(listaAbierta, adyacentes.get(i))){
						
						if(calculaG(VerticeActual, adyacentes.get(i)) < adyacentes.get(i).G){
							setEcuaciones(VerticeActual, adyacentes.get(i));
							adyacentes.get(i).setPadre(VerticeActual);
						}//fin if
						
					}//fin if
					
				}//fin for
				
				
			}//fin if-else
			
		}while(listaAbierta.size() != 0);
		
		return null;
	}//fin encontrarRutaMinima
	
	
	/*
	 * Devuelve true si la lista pasada como parametro contiene al Vertice que se pasa como parametro.
	 * Devuelve false en caso contrario
	 */
	public boolean loContiene(ArrayList<Vertice> lista, Vertice Vertice){
		boolean resultado = false;
		int i = 0;
		while(i < lista.size() && !resultado){
			Vertice VerticeLista = lista.get(i);
			if(VerticeLista.i == Vertice.i && VerticeLista.j == Vertice.j){
				resultado = true;
			}else{
				i++;
			}
		}
		return resultado;
	}
	
	
	/*
	 * Obtiene los Vertices adyacentes a un Vertice actual pasado como parametro.
	 * Los agrupa en un ArrayList.
	 */
	public ArrayList<Vertice> obtenerAdyacentes(Vertice actual){
		ArrayList<Vertice> adyacentes = new ArrayList<Vertice>();
		Vertice adyacente;
		int x = actual.i;
		int y = actual.j;
		
		for(int i = -1;i < 2;i++){
			for(int j = -1;j < 2;j++){
				
				//Obviamente ignoramos el cuadro actual.
				if(i == 0 && j == 0){
					continue;
				}
				
				//Uno de los cuadros diagonales.
				if(i != 0 && j != 0 && mapa[x+i][y+j]){
					if(mapa[x][y + j] && mapa[x + i][y]){
						adyacente = new Vertice(x+i, y+j);
						adyacentes.add(adyacente);
					}
				}else if(mapa[x+i][y+j]){	
					adyacente = new Vertice(x+i, y+j);
					adyacentes.add(adyacente);
				}
				
			}
		}
		return adyacentes;
	}//fin metodo obtenerAdyacentes
	
	
	/*
	 * Obtiene el Vertice con menor valor de F de la lista que se le pasa por parametro.
	 * F es un valor de un atributo del Vertice.
	 */
	public Vertice obtenerVerticeConMenorF(ArrayList<Vertice> lista){
		Vertice resultado = null;
		if(!lista.isEmpty()){
			Collections.sort(lista);
			resultado = lista.get(0);
			lista.remove(0);
		}
		return resultado;
	}
	

	public void setEcuaciones(Vertice actual, Vertice adyacente){
		int G, H;
		if((actual.i - adyacente.i == 0) || (actual.j - adyacente.j == 0)){
			G = 10;
		}else{
			G = 14;
		}
		H = (Math.abs(VerticeDestino.i - adyacente.i) + Math.abs(VerticeDestino.j - adyacente.j)) * 10;
		adyacente.G = actual.G + G;
		adyacente.H = H;
		adyacente.F = adyacente.G + H;
	}
	
	
	public int calculaG(Vertice actual, Vertice adyacente){
		int G;
		if((actual.i - adyacente.i == 0) || (actual.j - adyacente.j == 0)){
			G = 10;
		}else{
			G = 14;
		}
		return adyacente.G + G;
	}
	
	
	public void obtenerCaminoMinimo(Vertice origen, Vertice destino){
		if(destino.i == origen.i && destino.j == origen.j){
			caminoMinimo.push(destino);
		}else{
			caminoMinimo.push(destino);
			obtenerCaminoMinimo(origen, destino.getPadre());
		}	
	}
	
	
	/*
	 * Return true -> se ejecuto correctamente
	 * Return false -> no se encontro el camino minimo.
	 */
	public boolean ejecutarAlgoritmo(){
		Vertice destino = algoritmo();
		if(destino == null){
			return false;
		}else{
			obtenerCaminoMinimo(VerticeOrigen, destino);
			return true;
		}
	}
	
	
	/*
	 * Muestra el camino mínimo por consola. Sirve para probar el algoritmo
	 */
	public void mostrarPila(){
		while(!caminoMinimo.isEmpty()){
			Vertice Vertice = caminoMinimo.pop();
			System.out.println("("+Vertice.i+","+Vertice.j+")");
		}
	}
	
		
	/*
	 * Muestra por consola los vertice de la lista pasada por parámetro.
	 */
	public void mostrarListaVertice(ArrayList<Vertice> lista){
		for(int i = 0;i < lista.size();i++){
			System.out.println("("+lista.get(i).i+","+lista.get(i).j+")");
		}
	}

}
