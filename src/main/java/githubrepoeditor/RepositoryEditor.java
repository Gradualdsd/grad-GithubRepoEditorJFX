package githubrepoeditor;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RepositoryEditor {
	
	private CredentialsProvider credentials;
	private String path;
	private String name;
	private String branch;
	private Repository repository;
	private Git git;
	private Dictionary<String, Object> repoData = new Hashtable<>();
	private List<Ref> branches;
	
		
	public RepositoryEditor(String name, String branch) { //throws GitAPIException {
		//String[] splitted = path.split("/");
		
		this.path = System.getProperty("user.dir") + "/localrepos";
		this.name = name; //splitted[splitted.length-1];
		this.branch = branch;
		this.repoData.put("name", name);
		
		try {
			// load git
			this.git = Git.open(new File(this.getCurrentRepositoryPath()));
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Could not find initial local repository at:\n" + this.getCurrentRepositoryPath());
		}
		
		//wanted to automatically create a new repo on object creation, but is likely not viable
	}
    // to use basic auth
	public void setCredentials(String user, String pw) {
		this.credentials = new UsernamePasswordCredentialsProvider(user, pw);
		//Git.init().
	}
	
	public Repository getCurrentRepository() {
		return this.repository;
	}
	
	public String getCurrentRepositoryPath() {
		return this.path + "/" + this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setBranch(String branch) {
		this.branch = branch;
	}
	
	public ObservableList<String> getBranchNames() {
		ObservableList<String> result = FXCollections.observableArrayList();
		/*if (this.branches.size() <= 0) {
			this.git.branchCreate().setName("main").call();
		}*/
			
		for (Ref brnch : this.branches) {
			result.add(brnch.toString());
		}
		return result;
	}
	
	public List<Ref> getBranches() {
		//return (List<Ref>) this.repoData.get("branches");
		
		return this.branches;
	}
	
	public void create() {
		
		/*if (this.credentials == null) {
			//Exception err = new Exception("Repo creation requires credentials (use setCredentials())");
			//throw err;
			System.out.println("EXCEPTION: " + "Failed to create repository; " + "Repo creation requires credentials (use setCredentials())");
		}*/
		try {
			File localrepo = new File(this.path + "/" + this.name + "/.git");
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
		if (this.git == null) { // for if we initialized without a valid git
			try {
				// load git
				this.git = Git.open(new File(this.getCurrentRepositoryPath()));
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("Could not load local repository at:\n" + this.getCurrentRepositoryPath());
				return;
			}
		}
		
		try {
			List<Ref> branches = this.git.branchList().setListMode(ListMode.ALL).call();
			//this.repoData.put("branches", branches);
			this.branches = branches;
		} catch (GitAPIException e) {
			//e.printStackTrace();
			System.out.println("Could not get the branches for local repository:\n" + this.getCurrentRepositoryPath());
			//return;
		}
		
		Config cfg = this.repository.getConfig();
		String username = cfg.getString("user", null, "name");
		String email = cfg.getString("user", null, "email");
		System.out.println(username);
		System.out.println(email);
		this.repository.close(); //temp
		
		// load an existing local repository
		/*try {
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
		}*/
		
		
	}
	
	public void push() {
		
		Config cfg = this.repository.getConfig();
		String username = cfg.getString("user", null, "name");
		String email = cfg.getString("user", null, "email");
		
		try {
			
			
			URIish uri = new URIish("https://github.com/" + username + "/" + this.name + ".git");
			//git.remoteAdd().setUri(uri).getRepository();
			
			// to pull changes from remote
		    //git.pull().setCredentialsProvider(this.credentials).call();
			
			// to stage all files in a directory
		    this.git.add().setUpdate(true).addFilepattern(this.name).call();

		    // to create a commit with the staged files
		    this.git.commit().setMessage("Editor Commit Test").call();

		    // to push changes to remote
		    this.git.push().call();
		} catch (Exception e) {
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
