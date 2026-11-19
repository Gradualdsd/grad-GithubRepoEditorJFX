package githubrepoeditor;

import java.io.File;

public class RepoViewItem {
	private File file;
	
	public RepoViewItem(File file) {
		this.file = file;
	}
	
	@Override
	public String toString() {
		return this.file.getPath();
	}
}
