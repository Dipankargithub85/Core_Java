package graph;

import java.io.File;

import javax.swing.filechooser.*;

public class FileTypeFilter extends FileFilter {
	private final String extention;
	private final String description;
	public FileTypeFilter(String extention,String description){
		this.extention = extention;
		this.description = description;
		
			
	}
	@Override
	public boolean accept(File file) {
		// TODO Auto-generated method stub
		if(file.isDirectory()){
			return true;
		}
		return file.getName().endsWith(extention);
	}
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return description + String.format(" (*%s)",extention);
		
	}

	
}
