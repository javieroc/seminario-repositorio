package EstadosPantallas;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class EstadoGameOver extends BasicGameState{
	
	private int id;
	private Image imgGameOver;
	
	public EstadoGameOver(int id){
		this.id = id;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		imgGameOver = new Image("res/img/gameover.png");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		imgGameOver.draw(50, 300);
		g.setColor(Color.white);
		g.drawString("presione enter para volver al menú", 200, 500);
		g.drawString("presione esc para salir", 200, 530);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();
		if(input.isKeyDown(Input.KEY_ENTER)){
			game.enterState(Principal.ESTADOMENUPRINCIPAL);
		}else if(input.isKeyDown(Input.KEY_ESCAPE)){
			System.exit(0);
		}
	}

	@Override
	public int getID() {
		return id;
	}

}
