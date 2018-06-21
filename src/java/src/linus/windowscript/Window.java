package linus.windowscript;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Window {
	
	private static boolean execute(String line, String command, Consumer<String> action) {
		if(line.startsWith(command)) {
			action.accept(line.substring(command.length()).trim());
			return true;
		}
		return false;
	}
	
	private static double getDouble(String arg) throws WindowScriptException {
		try {
			return Double.parseDouble(arg);
		}catch(NumberFormatException e) {
			throw new WindowScriptException(e);
		}
	}
	
	private static boolean getBoolean(String arg) throws WindowScriptException {
		if(arg.equalsIgnoreCase("true"))
			return true;
		else if(arg.equalsIgnoreCase("false"))
			return false;
		else
			throw new WindowScriptException("Invalid arg for boolean: " + arg);
	}
	
	private static Path getPath(String arg, Path root) {
		Path path = Paths.get(arg).normalize();
		if(!path.isAbsolute())
			path = root.getParent().resolve(path);
		return path;
	}
	
	private static final String
		COMMAND_TITLE = "title",
		COMMAND_WIDTH = "width",
		COMMAND_HEIGHT = "height",
		COMMAND_XPOS = "xpos",
		COMMAND_YPOS = "ypos",
		COMMAND_MODE = "mode",
		COMMAND_POS = "pos",
		COMMAND_RESIZABLE = "resizable",
		//COMMAND_MINIMIZABLE = "minimizable",
		//COMMAND_MAXIMIZABLE = "maximizable",
		COMMAND_MINWIDTH = "minwidth",
		COMMAND_MAXWIDTH = "maxwidth",
		COMMAND_MINHEIGHT = "minheight",
		COMMAND_MAXHEIGHT = "maxheight",
		COMMAND_OPACITY = "opacity",
		COMMAND_ICON = "icon",
		COMMAND_CONTENT = "content",
		COMMAND_STYLE = "style",
		COMMAND_CHILD = "child",
		COMMAND_BACKGROUND = "background",
		COMMAND_CURSOR = "cursor",
		COMMAND_CURSORIMG = "curorimg",
		COMMAND_CURSORX = "cursorx",
		COMMAND_CURSORY = "cursory",
		COMMAND_MODALITY = "modality";

	private static enum Mode { MINIMIZED, NORMAL, FULLSCREEN, MAXIMIZED }
	private static enum Pos { LEFT_UP, LEFT, LEFT_DOWN, CENTRE_UP, CENTRE, CENTRE_DOWN, RIGHT_UP, RIGHT, RIGHT_DOWN }
	private static enum Style { DECORATED, TRANSPARENT, UNDECORATED, UNIFIED, UTILITY}
	private static enum Modality { APPLICATION, WINDOW, NONE }
	
	private static final Screen SCREEN = Screen.getPrimary();
	
	//Window Impl
	
	public final Path path;
	public final Stage stage;
	
	public final List<Window> childs = new ArrayList<>();
	
	private boolean hasRead = false;
	
	private WebView view = new WebView();
	private WebEngine engine = view.getEngine();
	
	private StackPane root = new StackPane(view);
	private Scene scene = new Scene(root);
		
	public final boolean isChild;
	
	public Window(Path path, Stage stage, boolean isChild) throws WindowScriptException {
		path = path.normalize();
		if(!Files.exists(path))
			throw new WindowScriptException("File doesn't exists: " + path);
		this.path = path;
		this.stage = stage;
		this.stage.setScene(scene);
		this.isChild = isChild;
	}
	
	public Window(String path, Stage stage, boolean isChild) throws WindowScriptException {
		this(Paths.get(path), stage, isChild);
	}
	
	public void setTitle(String title) {
		stage.setTitle(title);
	}
	
	public void setWidth(double width) {
		stage.setWidth(width);
	}
	
	public void setWidth(String width) {
		try {
			setWidth(getDouble(width));
		} catch (WindowScriptException e) {
			System.err.println("Invalid argument for width: " + width);
		}
	}
	
	public void setHeight(double height) {
		stage.setHeight(height);
	}
	
	public void setHeight(String height) {
		try {
			setHeight(getDouble(height));
		} catch (WindowScriptException e) {
			System.err.println("Invalid argument for height: " + height);
		}
	}
	
	public void setXPos(double x) {
		stage.setX(x);
	}
	
	public void setXPos(String x) {
		try {
			setXPos(getDouble(x));
		} catch (WindowScriptException e) {
			System.err.println("Invalid arg for xpos: " + x);
		}
	}
	
	public void setYPos(double y) {
		stage.setY(y);
	}
	
	public void setYPos(String y) {
		try {
			setYPos(getDouble(y));
		} catch (WindowScriptException e) {
			System.err.println("Invalid arg for ypos: " + y);
		}
	}
	
	public void setPos(double x, double y) {
		setXPos(x);
		setYPos(y);
	}
	
	public void setSize(double width, double height) {
		setWidth(width);
		setHeight(height);
	}
	
	public void setBounds(double x, double y, double width, double height) {
		setPos(x, y);
		setSize(width, height);
	}
	
	public void setMode(String mode) {
		if(mode.equalsIgnoreCase(Mode.FULLSCREEN.name()))
			stage.setFullScreen(true);
		else if(mode.equalsIgnoreCase(Mode.MAXIMIZED.name()))
			stage.setMaximized(true);
		else if(mode.equalsIgnoreCase(Mode.MINIMIZED.name()))
			stage.setIconified(true);
		else if(mode.equalsIgnoreCase(Mode.NORMAL.name())) {
			stage.setFullScreen(false);
			stage.setFullScreen(false);
			stage.setIconified(false);
		}else
			System.err.println("Invalid arg for mode: " + mode);
	}
	
	public void setPos(String pos) {
		double screenWidth = SCREEN.getBounds().getWidth();
		double screenHeight = SCREEN.getBounds().getHeight();
		
		if(pos.equalsIgnoreCase(Pos.LEFT_UP.name()))
			setPos(0, 0);
		else if(pos.equalsIgnoreCase(Pos.LEFT.name()))
			setPos(0, (screenHeight/ 2) - (stage.getHeight() / 2));
		else if(pos.equalsIgnoreCase(Pos.LEFT_DOWN.name()))
			setPos(0, screenHeight - stage.getHeight());
		else if(pos.equalsIgnoreCase(Pos.CENTRE_UP.name()))
			setPos((screenWidth / 2) - (stage.getWidth() / 2), 0);
		else if(pos.equalsIgnoreCase(Pos.CENTRE.name()))
			setPos((screenWidth / 2) - (stage.getWidth() / 2), (screenHeight / 2) - (stage.getHeight() / 2));
		else if(pos.equalsIgnoreCase(Pos.CENTRE_DOWN.name()))
			setPos((screenWidth / 2) - (stage.getWidth() / 2), screenHeight - stage.getHeight());
		else if(pos.equalsIgnoreCase(Pos.RIGHT_UP.name()))
			setPos(screenWidth - stage.getWidth(), 0);
		else if(pos.equalsIgnoreCase(Pos.RIGHT.name()))
			setPos(screenWidth - stage.getWidth(), (screenHeight / 2) - (stage.getHeight() / 2));
		else if(pos.equalsIgnoreCase(Pos.RIGHT_DOWN.name()))
			setPos(screenWidth - stage.getWidth(), screenHeight - stage.getHeight());
		else
			System.err.println("Invalid arg for pos: " + pos);
	}
	
	public void setResizable(boolean resizable) {
		stage.setResizable(resizable);
	}
	
	public void setResizable(String resizable) {
		try {
			setResizable(getBoolean(resizable));
		} catch (WindowScriptException e) {
			System.err.println("Invalid arg for resizable: " + resizable);
		}
	}
	
	public void setMinWidth(double minWidth) {
		stage.setMinWidth(minWidth);
	}
	
	public void setMinWidth(String minWidth) {
		try {
			setMinWidth(getDouble(minWidth));
		} catch (WindowScriptException e) {
			System.err.println("Invalid arg for minwidth: " + minWidth);
		}
	}
	
	public void setMaxWidth(double maxWidth) {
		stage.setMaxWidth(maxWidth);
	}
	
	public void setMaxWidth(String maxWidth) {
		try {
			setMaxWidth(getDouble(maxWidth));
		} catch (WindowScriptException e) {
			System.err.println("Invalid arg for maxwidth: " + maxWidth);
		}
	}
	
	public void setMinHeight(double minHeight) {
		stage.setMinHeight(minHeight);
	}
	
	public void setMinHeight(String minHeight) {
		try {
			setMinHeight(getDouble(minHeight));
		} catch (WindowScriptException e) {
			System.err.println("Invalid arg for minheight: " + minHeight);
		}
	}
	
	public void setMaxHeight(double maxHeight) {
		stage.setMaxHeight(maxHeight);
	}
	
	public void setMaxHeight(String maxHeight) {
		try {
			setMaxHeight(getDouble(maxHeight));
		} catch (WindowScriptException e) {
			System.err.println("Invalid arg for maxheight: " + maxHeight);
		}
	}
	
	public void setOpacity(double opacity) {
		stage.setOpacity(opacity);
	}
	
	public void setOpacity(String opacity) {
		try {
			setOpacity(getDouble(opacity));
		} catch (WindowScriptException e) {
			System.err.println("Invalid arg for opacity: " + opacity);
		}
	}
	
	public void setIcon(String icon) {
		Path file = getPath(icon, path);
		try {
			Image img = new Image(Files.newInputStream(file));
			stage.getIcons().add(img);
		} catch (IOException e) {
			System.err.println("Icon not found: " + icon);
		}
	}
	
	public void setContent(String arg) {
		Path file = getPath(arg, path);
		String content;
		try {
			content = new String(Files.readAllBytes(file));
		} catch (IOException e) {
			System.err.println("Could't read file: " + file);
			return;
		}
		engine.loadContent(content, "text/html");
	}
	
	public void setStyle(String style) {
		if(style.equalsIgnoreCase(Style.DECORATED.name()))
			stage.initStyle(StageStyle.DECORATED);
		else if(style.equalsIgnoreCase(Style.TRANSPARENT.name()))
			stage.initStyle(StageStyle.TRANSPARENT);
		else if(style.equalsIgnoreCase(Style.UNDECORATED.name()))
			stage.initStyle(StageStyle.UNDECORATED);
		else if(style.equalsIgnoreCase(Style.UNIFIED.name()))
			stage.initStyle(StageStyle.UNIFIED);
		else if(style.equalsIgnoreCase(Style.UTILITY.name()))
			stage.initStyle(StageStyle.UTILITY);
		else
			System.err.println("Invalid arg for style: " + style);
	}
	
	public boolean hasIcon() {
		return stage.getIcons().size() > 0;
	}
	
	public void setChild(String arg) {
		Path file = getPath(arg, path);
		
		Stage childStage = new Stage();
		childStage.initOwner(this.stage);
		childStage.getIcons().setAll(stage.getIcons());
		childStage.initStyle(stage.getStyle());
		
		try {
			Window window = new Window(file, childStage, true);
			childs.add(window);
		} catch (WindowScriptException e) {
			System.err.println("Error at creating window: " + e.getLocalizedMessage());
		}
	}
	
	public void setCursor(String cursor) {
	}
	
	public void setModality(String modality) {
		if(!isChild) {
			System.err.println("Should't set modality on main window");
			return;
		}
		
		if(modality.equalsIgnoreCase(Modality.APPLICATION.name()))
			stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
		else if(modality.equalsIgnoreCase(Modality.WINDOW.name()))
			stage.initModality(javafx.stage.Modality.WINDOW_MODAL);
		else if(modality.equalsIgnoreCase(Modality.NONE.name()))
			stage.initModality(javafx.stage.Modality.NONE);
		else
			System.err.println("Invalid arg for modality: " + modality);
	}
	
	private void read() throws WindowScriptException {
		try {
			Files.lines(path).forEach(s -> {
				if(
					!execute(s, COMMAND_TITLE, this::setTitle) &&
					!execute(s, COMMAND_WIDTH, this::setWidth) &&
					!execute(s, COMMAND_HEIGHT, this::setHeight) &&
					!execute(s, COMMAND_XPOS, this::setXPos) &&
					!execute(s, COMMAND_YPOS, this::setYPos) &&
					!execute(s, COMMAND_MODE, this::setMode) &&
					!execute(s, COMMAND_POS, this::setPos) &&
					!execute(s, COMMAND_RESIZABLE, this::setResizable) &&
					!execute(s, COMMAND_MINWIDTH, this::setMinWidth) &&
					!execute(s, COMMAND_MAXWIDTH, this::setMaxWidth) &&
					!execute(s, COMMAND_MINHEIGHT, this::setMinHeight) &&
					!execute(s, COMMAND_MAXHEIGHT, this::setMaxHeight) &&
					!execute(s, COMMAND_OPACITY, this::setOpacity) &&
					!execute(s, COMMAND_ICON, this::setIcon) &&
					!execute(s, COMMAND_CONTENT, this::setContent) &&
					!execute(s, COMMAND_STYLE, this::setStyle) &&
					!execute(s, COMMAND_CHILD, this::setChild) &&
					!execute(s, COMMAND_MODALITY, this::setModality)
				) {
					System.err.println("Unknown command: " + s);
				}
			});
		} catch (IOException e) {
			throw new WindowScriptException("Couldn't read path: " + path, e);
		}
		
		stage.setOnShowing(event -> {
			childs.forEach(s -> {
				try {
					s.show();
				} catch (WindowScriptException e) {
					System.err.println("Error at showing child window: " + e.getLocalizedMessage());
				}
			});
		});
		
		hasRead = true;	
	}
	
	public void show() throws WindowScriptException {
		if(!hasRead)
			read();
		stage.show();
	}
	
}