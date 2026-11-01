package githubrepoeditor;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;

public class RepositoryEditor {
	
	private CredentialsProvider credentials;
	private String path;
	private String name;
	private String branch;
	private Repository repository;
	
		
	public RepositoryEditor(String path, String branch) { //throws GitAPIException {
		String[] splitted = path.split("/");
		
		this.path = path;
		this.name = splitted[splitted.length-1];
		this.branch = branch;
		
		//wanted to automatically create a new repo on object creation, but is likely not viable
	}
    // to use basic auth
	public void setCredentials(String user, String pw) {
		this.credentials = new UsernamePasswordCredentialsProvider(user, pw);
		//Git.init().
	}
	
	public void create() {
		
		/*if (this.credentials == null) {
			//Exception err = new Exception("Repo creation requires credentials (use setCredentials())");
			//throw err;
			System.out.println("EXCEPTION: " + "Failed to create repository; " + "Repo creation requires credentials (use setCredentials())");
		}*/
		try {
			File localrepo = new File(System.getProperty("user.dir") + "/localrepos" + "/new_repo/.git");
			this.repository = FileRepositoryBuilder.create(localrepo);
			if (!localrepo.exists()) {
				
				this.repository.create();
				
			}
			//this.repository.
			// Create a new local repository
			
			this.load();
			
			// to init a new repository
		    //Git.init().setDirectory(new File("/opt/my-repo")).setInitialBranch("master").call();
			//Git.lsRemoteRepository().setRemote(branch).setCredentialsProvider(credentials)
			
			
			

			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
			
		
	}
	
	public void load() {
		
		
		// load an existing local repository
		try {
			Repository existingRepo = new FileRepositoryBuilder()
				    .setGitDir(new File("grad_test_my_repo/.git"))
				    .build();
			this.repository = existingRepo;
			// Config
			Config cfg = this.repository.getConfig();
			String username = cfg.getString("user", null, "name");
			
			System.out.println(username);
			this.repository.close(); //temp
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
    

    /*// to clone a repository
    Git.cloneRepository().setURI("https://github.com/my-repo.git").setCredentialsProvider(credentials).call();

    // to init a new repository
    //Git.init().setDirectory(new File("/opt/my-repo")).setInitialBranch("master").call();

    // to use a cloned repo
    var git = Git.open(new File("/opt/my-repo"));

    // to pull changes from remote
    git.pull().setCredentialsProvider(credentials).call();

    // to stage one file
    git.add().setUpdate(true).addFilepattern("dir/file.txt").call();

    // to stage all files in a directory
    git.add().setUpdate(true).addFilepattern("dir").call();

    // to create a commit with the staged files
    git.commit().setMessage("just adding some files.").call();

    // to push changes to remote
    git.push().call();*/

}
