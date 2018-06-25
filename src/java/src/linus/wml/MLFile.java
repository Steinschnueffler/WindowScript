package linus.wml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class MLFile{
	
	protected static double getDouble(String arg) throws MLException {
		try {
			return Double.parseDouble(arg);
		}catch(NumberFormatException e) {
			throw new MLException(e);
		}
	}
	
	protected static boolean getBoolean(String arg) throws MLException {
		if(arg.equalsIgnoreCase("true"))
			return true;
		else if(arg.equalsIgnoreCase("false"))
			return false;
		else
			throw new MLException("Invalid arg for boolean: " + arg);
	}
	
	public final Path path;
	
	protected List<MLCommand> commands = new ArrayList<>();
	
	public MLFile(Path path) throws MLException {
		if(!Files.exists(path))
			throw new MLException("File doesn't exists: " + path);
		this.path = path;
	}
	
	protected Path getPath(String arg) {
		Path path = Paths.get(arg).normalize();
		if(!path.isAbsolute())
			path = this.path.getParent().resolve(path);
		return path;
	}

	public void run() throws MLException{
		try {
			Files.lines(path).forEach(s -> {
				for(MLCommand command : commands)
					if(command.execute(s))
						break;
			});
		} catch (IOException e) {
			throw new MLException(e);
		}
	}
	
	protected void warning(String msg) {
		System.err.println("warning[class=" + getClass().getSimpleName() + ", path=" + path + "]: " + msg);
	}
}
