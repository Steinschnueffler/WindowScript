package linus.wml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;

import static linus.wml.MLCommand.cmd;

public class MLCursor extends MLFile{

	private static final String
		COMMAND_IMAGE = "image",
		COMMAND_XHOT = "xhot",
		COMMAND_YHOT = "yhot";
	
	//Cursor Impl 
		
	private Image image = null;
	private double xhot = 0;
	private double yhot = 0;
	
	public MLCursor(Path path) throws MLException {
		super(path);
		commands.addAll(Arrays.asList(
			cmd(COMMAND_IMAGE, this::setImage),
			cmd(COMMAND_XHOT, this::setXHot),
			cmd(COMMAND_YHOT, this::setYHot)
		));
	}

	public void setImage(String image) {
		try {
			this.image = new Image(Files.newInputStream(getPath(image)));
		} catch (IOException e) {
			System.err.println("Couldn't read file: " + image);
		}
	}
	
	public void setXHot(double xhot) {
		this.xhot = xhot;
	}
	
	public void setXHot(String xhot) {
		try {
			setXHot(getDouble(xhot));
		} catch (MLException e) {
			warning("Invalid arg for xhot: " + xhot);
		}
	}
	
	public void setYHot(double yhot) {
		this.yhot = yhot;
	}
	
	public void setYHot(String yhot) {
		try {
			setYHot(getDouble(yhot));
		} catch (MLException e) {
			warning("Invalid arg for yhot: " + yhot);
		}
	}
	
	public Cursor toJavaFXCursor() {
		if(image != null)
			return new ImageCursor(image, xhot, yhot);
		else
			return Cursor.DEFAULT;
	}
	
	public static Cursor getCursor(Path path) throws MLException{
		MLCursor cursor = new MLCursor(path);
		cursor.run();
		return cursor.toJavaFXCursor();
	}
	
	public static Cursor getCursor(String path) throws MLException{
		return getCursor(Paths.get(path));
	}
}
