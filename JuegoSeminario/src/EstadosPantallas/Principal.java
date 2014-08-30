package EstadosPantallas;



import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Principal extends StateBasedGame{
	
	//Constantes
	public static final int ESTADOMENUPRINCIPAL = 0;
	public static final int ESTADOPLAYJUEGO = 1;
	public static final int ESTADOGAMEOVER = 2;
	
	
	
	public Principal() {
		super("Juego Seminario");
		this.addState(new EstadoMenu(ESTADOMENUPRINCIPAL));
		this.addState(new EstadoPlayGame(ESTADOPLAYJUEGO));
		this.addState(new EstadoGameOver(ESTADOGAMEOVER));
		this.enterState(ESTADOMENUPRINCIPAL);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {

		this.getState(ESTADOMENUPRINCIPAL).init(container, this);
		this.getState(ESTADOPLAYJUEGO).init(container, this);
		this.getState(ESTADOGAMEOVER).init(container, this);
	}
	
	public static void main(String [] args) throws SlickException{
		AppGameContainer app = new AppGameContainer(new Principal(), 672, 672, false);
		app.setShowFPS(true);
		app.setVSync(true);
		app.setMaximumLogicUpdateInterval(15);
		app.setMinimumLogicUpdateInterval(10);
		app.start();
	}
	
}
