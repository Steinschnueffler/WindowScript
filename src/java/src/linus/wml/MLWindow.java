package linus.wml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static linus.wml.MLCommand.cmd;

public class MLWindow extends MLFile{
	
	private static final String
		COMMAND_TITLE = "title",
		COMMAND_WIDTH = "width",
		COMMAND_HEIGHT = "height",
		COMMAND_XPOS = "xpos",
		COMMAND_YPOS = "ypos",
		COMMAND_MODE = "mode",
		COMMAND_POS = "pos",
		COMMAND_RESIZABLE = "resizable",
		//COMMAND_MINIMIZABLE = "minimizable", //Not part of stage
		//COMMAND_MAXIMIZABLE = "maximizable", //Not part of stage
		COMMAND_MINWIDTH = "minwidth",
		COMMAND_MAXWIDTH = "maxwidth",
		COMMAND_MINHEIGHT = "minheight",
		COMMAND_MAXHEIGHT = "maxheight",
		COMMAND_OPACITY = "opacity",
		COMMAND_ICON = "icon",
		COMMAND_CONTENT = "content",
		COMMAND_STYLE = "style",
		COMMAND_CHILD = "child",
		//COMMAND_BACKGROUND = "background", //should be implemented in HTML
		COMMAND_CURSOR = "cursor",
		COMMAND_MODALITY = "modality";

	private static enum Modes { MINIMIZED, NORMAL, FULLSCREEN, MAXIMIZED }
	private static enum Pos { LEFT_UP, LEFT, LEFT_DOWN, CENTRE_UP, CENTRE, CENTRE_DOWN, RIGHT_UP, RIGHT, RIGHT_DOWN }
	private static enum Styles { DECORATED, TRANSPARENT, UNDECORATED, UNIFIED, UTILITY}
	private static enum Modalitys { APPLICATION, WINDOW, NONE }
	private static enum Cursors { 
			DEFAULT, CLOSED_HAND, CROSSHAIR, DISSAPEAR, TEXT, HAND, WAIT, MOVE, NONE, OPEN_HAND,
			H_RESIZE, V_RESIZE, N_RESIZE, E_RESIZE, S_RESIZE, W_RESIZE, NE_RESIZE, SE_RESIZE, SW_RESIZE, NW_RESIZE
		}
	
	private static final Screen SCREEN = Screen.getPrimary();
	
	//Window Impl
	
	public final Stage stage;
	
	public final List<MLWindow> childs = new ArrayList<>();
	
	private WebView view = new WebView();
	private WebEngine engine = view.getEngine();
	
	private StackPane root = new StackPane(view);
	private Scene scene = new Scene(root);
		
	public final boolean isChild;
	private boolean settedTitle = false;
	private boolean hasRead = false;
	
	public MLWindow(Path path, Stage stage, boolean isChild) throws MLException {
		super(path);
		this.stage = stage;
		this.stage.setScene(scene);
		this.isChild = isChild;
		commands.addAll(Arrays.asList(
			cmd(COMMAND_CHILD, this::setChild),
			cmd(COMMAND_CONTENT, this::setContent),
			cmd(COMMAND_CURSOR, this::setCursor),
			cmd(COMMAND_HEIGHT, this::setHeight),
			cmd(COMMAND_ICON, this::setIcon),
			cmd(COMMAND_MAXHEIGHT, this::setMaxHeight),
			cmd(COMMAND_MAXWIDTH, this::setMaxWidth),
			cmd(COMMAND_MINHEIGHT, this::setMinHeight),
			cmd(COMMAND_MINWIDTH, this::setMinWidth),
			cmd(COMMAND_MODALITY, this::setModality),
			cmd(COMMAND_MODE, this::setMode),
			cmd(COMMAND_OPACITY, this::setOpacity),
			cmd(COMMAND_POS, this::setPos),
			cmd(COMMAND_RESIZABLE, this::setResizable),
			cmd(COMMAND_STYLE, this::setStyle),
			cmd(COMMAND_TITLE, this::setTitle),
			cmd(COMMAND_WIDTH, this::setWidth),
			cmd(COMMAND_XPOS, this::setXPos),
			cmd(COMMAND_YPOS, this::setYPos)
		));
	}
	
	public MLWindow(String path, Stage stage, boolean isChild) throws MLException {
		this(Paths.get(path), stage, isChild);
	}
	
	public void setTitle(String title) {
		stage.setTitle(title);
		settedTitle = true;
	}
	
	public void setWidth(double width) {
		stage.setWidth(width);
	}
	
	public void setWidth(String width) {
		try {
			setWidth(getDouble(width));
		} catch (MLException e) {
			warning("Invalid argument for width: " + width);
		}
	}
	
	public void setHeight(double height) {
		stage.setHeight(height);
	}
	
	public void setHeight(String height) {
		try {
			setHeight(getDouble(height));
		} catch (MLException e) {
			warning("Invalid argument for height: " + height);
		}
	}
	
	public void setXPos(double x) {
		stage.setX(x);
	}
	
	public void setXPos(String x) {
		try {
			setXPos(getDouble(x));
		} catch (MLException e) {
			warning("Invalid arg for xpos: " + x);
		}
	}
	
	public void setYPos(double y) {
		stage.setY(y);
	}
	
	public void setYPos(String y) {
		try {
			setYPos(getDouble(y));
		} catch (MLException e) {
			warning("Invalid arg for ypos: " + y);
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
		if(mode.equalsIgnoreCase(Modes.FULLSCREEN.name()))
			stage.setFullScreen(true);
		else if(mode.equalsIgnoreCase(Modes.MAXIMIZED.name()))
			stage.setMaximized(true);
		else if(mode.equalsIgnoreCase(Modes.MINIMIZED.name()))
			stage.setIconified(true);
		else if(mode.equalsIgnoreCase(Modes.NORMAL.name())) {
			stage.setMaximized(false);
			stage.setFullScreen(false);
			stage.setIconified(false);
		}else
			warning("Invalid arg for mode: " + mode);
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
			warning("Invalid arg for pos: " + pos);
	}
	
	public void setResizable(boolean resizable) {
		stage.setResizable(resizable);
	}
	
	public void setResizable(String resizable) {
		try {
			setResizable(getBoolean(resizable));
		} catch (MLException e) {
			warning("Invalid arg for resizable: " + resizable);
		}
	}
	
	public void setMinWidth(double minWidth) {
		stage.setMinWidth(minWidth);
	}
	
	public void setMinWidth(String minWidth) {
		try {
			setMinWidth(getDouble(minWidth));
		} catch (MLException e) {
			warning("Invalid arg for minwidth: " + minWidth);
		}
	}
	
	public void setMaxWidth(double maxWidth) {
		stage.setMaxWidth(maxWidth);
	}
	
	public void setMaxWidth(String maxWidth) {
		try {
			setMaxWidth(getDouble(maxWidth));
		} catch (MLException e) {
			warning("Invalid arg for maxwidth: " + maxWidth);
		}
	}
	
	public void setMinHeight(double minHeight) {
		stage.setMinHeight(minHeight);
	}
	
	public void setMinHeight(String minHeight) {
		try {
			setMinHeight(getDouble(minHeight));
		} catch (MLException e) {
			warning("Invalid arg for minheight: " + minHeight);
		}
	}
	
	public void setMaxHeight(double maxHeight) {
		stage.setMaxHeight(maxHeight);
	}
	
	public void setMaxHeight(String maxHeight) {
		try {
			setMaxHeight(getDouble(maxHeight));
		} catch (MLException e) {
			warning("Invalid arg for maxheight: " + maxHeight);
		}
	}
	
	public void setOpacity(double opacity) {
		stage.setOpacity(opacity);
	}
	
	public void setOpacity(String opacity) {
		try {
			setOpacity(getDouble(opacity));
		} catch (MLException e) {
			warning("Invalid arg for opacity: " + opacity);
		}
	}
	
	public void setIcon(String icon) {
		Path file = getPath(icon);
		try {
			Image img = new Image(Files.newInputStream(file));
			stage.getIcons().add(img);
		} catch (IOException e) {
			warning("Icon not found: " + icon);
		}
	}
	
	public void setContent(String arg) {
		Path file = getPath(arg);
		String content;
		try {
			content = new String(Files.readAllBytes(file));
		} catch (IOException e) {
			warning("Could't read file: " + file);
			return;
		}
		engine.loadContent(content, "text/html");
	}
	
	public void setStyle(String style) {
		if(style.equalsIgnoreCase(Styles.DECORATED.name()))
			stage.initStyle(StageStyle.DECORATED);
		else if(style.equalsIgnoreCase(Styles.TRANSPARENT.name()))
			stage.initStyle(StageStyle.TRANSPARENT);
		else if(style.equalsIgnoreCase(Styles.UNDECORATED.name()))
			stage.initStyle(StageStyle.UNDECORATED);
		else if(style.equalsIgnoreCase(Styles.UNIFIED.name()))
			stage.initStyle(StageStyle.UNIFIED);
		else if(style.equalsIgnoreCase(Styles.UTILITY.name()))
			stage.initStyle(StageStyle.UTILITY);
		else
			warning("Invalid arg for style: " + style);
	}
	
	public boolean hasIcon() {
		return stage.getIcons().size() > 0;
	}
	
	public void setChild(String arg) {
		Path file = getPath(arg);
		
		Stage childStage = new Stage();
		childStage.initOwner(this.stage);
		childStage.getIcons().setAll(stage.getIcons());
		childStage.initStyle(stage.getStyle());
		
		try {
			MLWindow window = new MLWindow(file, childStage, true);
			childs.add(window);
		} catch (MLException e) {
			warning("warning at creating window: " + e.getLocalizedMessage());
		}
	}
	
	public void setCursor(String cursor) {
		if(cursor.equalsIgnoreCase(Cursors.CLOSED_HAND.name()))
			view.setCursor(Cursor.CLOSED_HAND);
		else if(cursor.equalsIgnoreCase(Cursors.CROSSHAIR.name()))
			view.setCursor(Cursor.CROSSHAIR);
		else if(cursor.equalsIgnoreCase(Cursors.DEFAULT.name()))
			view.setCursor(Cursor.DEFAULT);
		else if(cursor.equalsIgnoreCase(Cursors.DISSAPEAR.name()))
			view.setCursor(Cursor.DISAPPEAR);
		else if(cursor.equalsIgnoreCase(Cursors.E_RESIZE.name()))
			view.setCursor(Cursor.E_RESIZE);
		else if(cursor.equalsIgnoreCase(Cursors.H_RESIZE.name()))
			view.setCursor(Cursor.H_RESIZE);
		else if(cursor.equalsIgnoreCase(Cursors.HAND.name()))
			view.setCursor(Cursor.HAND);
		else if(cursor.equalsIgnoreCase(Cursors.MOVE.name()))
			view.setCursor(Cursor.MOVE);
		else if(cursor.equalsIgnoreCase(Cursors.N_RESIZE.name()))
			view.setCursor(Cursor.N_RESIZE);
		else if(cursor.equalsIgnoreCase(Cursors.NE_RESIZE.name()))
			view.setCursor(Cursor.NE_RESIZE);
		else if(cursor.equalsIgnoreCase(Cursors.NONE.name()))
			view.setCursor(Cursor.NONE);
		else if(cursor.equalsIgnoreCase(Cursors.NW_RESIZE.name()))
			view.setCursor(Cursor.NW_RESIZE);
		else if(cursor.equalsIgnoreCase(Cursors.OPEN_HAND.name()))
			view.setCursor(Cursor.OPEN_HAND);
		else if(cursor.equalsIgnoreCase(Cursors.S_RESIZE.name()))
			view.setCursor(Cursor.S_RESIZE);
		else if(cursor.equalsIgnoreCase(Cursors.SE_RESIZE.name()))
			view.setCursor(Cursor.SE_RESIZE);
		else if(cursor.equalsIgnoreCase(Cursors.SW_RESIZE.name()))
			view.setCursor(Cursor.SW_RESIZE);
		else if(cursor.equalsIgnoreCase(Cursors.TEXT.name()))
			view.setCursor(Cursor.TEXT);
		else if(cursor.equalsIgnoreCase(Cursors.V_RESIZE.name()))
			view.setCursor(Cursor.V_RESIZE);
		else if(cursor.equalsIgnoreCase(Cursors.W_RESIZE.name()))
			view.setCursor(Cursor.W_RESIZE);
		else if(cursor.equalsIgnoreCase(Cursors.WAIT.name()))
			view.setCursor(Cursor.WAIT);
		else {
			try {
				view.setCursor(MLCursor.getCursor(cursor));
			} catch (MLException e) {
				warning("Couldn't load cursor: " + cursor);
			}
		}
	}
	
	public void setModality(String modality) {
		if(!isChild) {
			warning("Should't set modality on main window");
			return;
		}
		
		if(modality.equalsIgnoreCase(Modalitys.APPLICATION.name()))
			stage.initModality(Modality.APPLICATION_MODAL);
		else if(modality.equalsIgnoreCase(Modalitys.WINDOW.name()))
			stage.initModality(Modality.WINDOW_MODAL);
		else if(modality.equalsIgnoreCase(Modalitys.NONE.name()))
			stage.initModality(Modality.NONE);
		else
			warning("Invalid arg for modality: " + modality);
	}
	
	@Override
	public void run() throws MLException {
		super.run();
		
		if(!settedTitle)
			setTitle(path.getFileName().toString());
		
		stage.setOnShown(event -> {
			childs.forEach(s -> {
				try {
					s.show();
				} catch (MLException e) {
					warning("error at showing child window: " + e.getLocalizedMessage());
				}
			});
		});
		
		hasRead = true;	
	}
	
	public void show() throws MLException {
		if(!hasRead)
			run();
		stage.show();
	}
	
}