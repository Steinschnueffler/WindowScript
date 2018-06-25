package linus.wml;

import java.util.function.Consumer;

public class MLCommand {

	public final String command;
	public final Consumer<String> action;
	
	public MLCommand(String command, Consumer<String> action) {
		this.command = command;
		this.action = action;
	}
	
	public boolean execute(String line) {
		line = line.trim();
		if(line.startsWith(command)) {
			action.accept(line.substring(command.length()).trim());
			return true;
		}
		return false;
	}
	
	public static MLCommand cmd(String command, Consumer<String> action) {
		return new MLCommand(command, action);
	}

}
