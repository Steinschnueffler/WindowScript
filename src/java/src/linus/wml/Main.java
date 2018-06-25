package linus.wml;

import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage stage) {
		List<String> args = getParameters().getRaw();
		if(args.isEmpty())
			throw new MLError("Program must be started with a window script");
		try {
			MLWindow window = new MLWindow(args.get(0), stage, false);
			window.show();
		} catch (MLException e) {
			throw new MLError(e);
		}
	}
	
	public static void main(String[] args) {
		launch(Main.class, args);
	}
}
