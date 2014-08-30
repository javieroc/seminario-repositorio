package controladores;


public class Vertice implements Comparable{
	
	//Variables de instancia
	public int i;//fila
	
	public int j;//columna
	
	public int G, H, F;//valor de los pesos.
	
	private Vertice padre;
	
	//Método constructor
	public Vertice(int i, int j){
		this.i = i;
		this.j = j;
		G = 0;
		H = 0;
		F = 0;
		padre = null;
	}
	
	
	public void setPadre(Vertice padre){
		this.padre = padre;
	}
	
	public Vertice getPadre(){
		return padre;
	}
	
	public void setF(int F){
		this.F = F;	
	}
	
	
	public void setG(int G){
		this.G= G;	
	}
	
	
	public void setH(int H){
		this.H = H;	
	}
	
	
	@Override
	public int compareTo(Object ob) {
		Vertice vertice = (Vertice) ob;
		if(this.F < vertice.F){
			return -1;
		}else if (this.F == vertice.F){
			return 0;
		}else{
			return 1;
		}
	}
	
	
}//fin clase Nodo
