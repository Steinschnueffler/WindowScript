package linus.windowscript;

import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage stage) {
		List<String> args = getParameters().getRaw();
		if(args.isEmpty())
			throw new WindowScriptError("Program must be started with a window script");
		try {
			Window window = new Window(args.get(0), stage);
			window.show();
		} catch (WindowScriptException e) {
			throw new WindowScriptError(e);
		}
	}
	
	public static void main(String[] args) {
		launch(Main.class, args);
	}
}
