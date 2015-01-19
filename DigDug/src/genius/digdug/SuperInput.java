package genius.digdug;

import org.newdawn.slick.Input;

public class SuperInput {
	private final Input input;
	
	public SuperInput(final Input input) {
		this.input = input;
	}
	
	/**
	 * @return key W or UP
	 */
	public boolean up() {
		return this.input.isKeyDown(Input.KEY_W) || this.input.isKeyDown(Input.KEY_UP);
	}
	
	/**
	 * @return key S or DOWN
	 */
	public boolean down() {
		return this.input.isKeyDown(Input.KEY_S) || this.input.isKeyDown(Input.KEY_DOWN);
	}
	
	/**
	 * @return key A or LEFT
	 */
	public boolean left() {
		return this.input.isKeyDown(Input.KEY_A) || this.input.isKeyDown(Input.KEY_LEFT);
	}
	
	/**
	 * @return key D or RIGHT
	 */
	public boolean right() {
		return this.input.isKeyDown(Input.KEY_D) || this.input.isKeyDown(Input.KEY_RIGHT);
	}
	
	/**
	 * @return key RCRTL or SPACE
	 */
	public boolean action() {
		return this.input.isKeyDown(Input.KEY_SPACE) || this.input.isKeyDown(Input.KEY_RCONTROL);
	}
	
	/**
	 * @return left mouse button
	 */
	public boolean bleft() {
		return this.input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
	}
	
	/**
	 * @return right mouse button
	 */
	public boolean bright() {
		return this.input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON);
	}
	
	/**
	 * @return middle mouse button
	 */
	public boolean bmiddle() {
		return this.input.isMouseButtonDown(Input.MOUSE_MIDDLE_BUTTON);
	}
	
	/**
	 * @return any mouse button
	 */
	public boolean mouse() {
		return this.bleft() || this.bright() || this.bmiddle();
	}
}
