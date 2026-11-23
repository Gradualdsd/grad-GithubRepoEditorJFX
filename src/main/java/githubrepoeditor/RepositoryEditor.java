package githubrepoeditor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RepositoryEditor {
	
	private CredentialsProvider credentials;
	private String[] credentials_unsecure = new String[3]; //idk how else to get these soooooooooooo
	private String path;
	private String name;
	private String branch;
	private Repository repository;
	private Git git;
	private Dictionary<String, Object> repoData = new Hashtable<>();
	private List<Ref> branches;
	
		
	public RepositoryEditor() { //throws GitAPIException {
		//String[] splitted = path.split("/");
		
		this.path = System.getProperty("user.dir") + "/localrepos";
		//this.name = name; //splitted[splitted.length-1];
		//this.branch = branch;
		//this.repoData.put("name", name);
		
		/*
		try {
			// load git
			this.git = Git.open(new File(this.getCurrentRepositoryPath()));
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Could not find initial local repository at:\n" + this.getCurrentRepositoryPath());
		}
		*/
		
		//wanted to automatically create a new repo on object creation, but is likely not viable
	}
    // to use basic auth
	public void setCredentials(String user, String pw) {
		this.credentials = new UsernamePasswordCredentialsProvider(user, pw);
		this.credentials_unsecure[0] = user;
		this.credentials_unsecure[1] = pw;
		//Git.init().
	}
	
	public Repository getCurrentRepository() {
		return this.repository;
	}
	
	public String getCurrentRepositoryPath() {
		return this.path + "/" + this.name;
	}
	
	public String getCurrentRepositoryBranchName() {
		return this.branch;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setBranch(String branch) {
		this.branch = branch;
	}
	
	public ObservableList<String> getBranchNames(String mode) {
		ObservableList<String> result = FXCollections.observableArrayList();
		/*if (this.branches.size() <= 0) {
			this.git.branchCreate().setName("main").call();
		}*/
			
		for (Ref brnch : this.getBranches(mode)) {
			String[] splitted = brnch.getName().split("/");
			result.add(splitted[splitted.length-1]);
		}
		return result;
	}
	
	public List<Ref> getBranches(String mode) {
		mode = (mode == null || mode.isBlank()) ? "local" : mode;
		ListMode lmode;
		switch (mode.toLowerCase()) {
		case "local":
			lmode = null;
			break;
			
		case "remote":
			lmode = ListMode.REMOTE;
			break;
			
		case "all":
		default:
			lmode = ListMode.ALL;
		}
		
		try {
			//List<Ref> branches = this.git.branchList().setListMode(ListMode.ALL).call();
			List<Ref> branches = this.git.branchList().setListMode(lmode).call();
			//this.repoData.put("branches", branches);
			return branches;
		} catch (GitAPIException e) {
			//e.printStackTrace();
			System.out.println("Could not get the branches for repository:\n" + this.getCurrentRepositoryPath());
			//return;
		}
		
		return null;
	}
	
	/*public boolean DiffersFromRemote() {
		this.git.diff();
	}*/
	
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
		
		this.branches = this.getBranches("local");
		this.branch = this.getBranchNames("local").getFirst();
		/*
		try {
			String[] splitted = this.git.getRepository().getBranch().split("/");
			this.branch = (splitted[splitted.length]);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not get Default Branch for local repository: " + this.getCurrentRepositoryPath());
		}
		*/
		
		//System.out.println(this.branches);
		
		/*
		Config cfg = this.repository.getConfig();
		String username = cfg.getString("user", null, "name");
		String email = cfg.getString("user", null, "email");
		System.out.println(username);
		System.out.println(email);
		*/
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
	
	public void AskToOpen() {
		File directory = AppProperties.askForSingleFolderAndReturn(this.path, "Select Repository", null);
		this.open(directory);
	}
	
	public void open(File repoDirectory) {
		try {
			// load git
			this.git = Git.open(repoDirectory);
			this.repository = this.git.getRepository();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Could not find local repository at:\n" + repoDirectory.getPath());
			return;
		}
		String[] splitted = this.git.getRepository().getDirectory().getAbsolutePath().split("\\\\");
		
		this.name = splitted[splitted.length-2];
		System.out.println(splitted[0]);
		
		//this.repoData.put("name", name);
		this.load();
	}
	
	public void push(String commitTitle) {
		
		/*
		Config cfg = this.repository.getConfig();
		String username = cfg.getString("user", null, "name");
		String email = cfg.getString("user", null, "email");
		*/
		//System.out.println(cfg);
		
		try {
			System.out.println("Attempting to push repository...");
	        // Local repo path (can be a new repo or existing)
	        String uriStr = "https://github.com/" + this.credentials_unsecure[0] + "/" + this.name + ".git";
	        
	        git.checkout()
	        .setName(this.branch)
	        .call();

	        // Add the file(s) to staging
	        git.add().addFilepattern(".").call();
	        

	        // Commit changes
	        git.commit().setMessage(commitTitle).call();

	        // Add GitHub remote
	        try {
	        	git.remoteAdd()
	            .setName("origin")
	            .setUri(new URIish(uriStr))
	            .call();
	        } catch (URISyntaxException e) {
	        	e.printStackTrace();
	        	System.out.println("Invalid URI (" + uriStr + ")");
	        }
	        
	        // Push to remote
	        git.push().setRemote("origin")
	                .setCredentialsProvider(this.credentials)
	                .setRefSpecs(new RefSpec("refs/heads/" + this.branch + ":refs/heads/" + this.branch))
	                .call();

	        System.out.println("File pushed to GitHub!");
		        
		
		} catch (GitAPIException e) {
			e.printStackTrace();
			System.out.println("Could not push repository");
		}
		
		/*try {
			
			
			URIish uri = new URIish("https://github.com/" + username + "/" + this.name + ".git");
			//git.remoteAdd().setUri(uri).getRepository();
			
			// to pull changes from remote
		    //this.git.pull().setCredentialsProvider(this.credentials).call();
		    this.git.remoteAdd().setUri(uri).call();
		    //this.git.remoteSetUrl().setRemoteUri(uri).call();
			
			// to stage all files in a directory
		    this.git.add().setUpdate(true).addFilepattern(this.name).call();

		    // to create a commit with the staged files
		    this.git.commit().setMessage("Editor Commit Test").call();

		    // to push changes to remote
		    this.git.push().call();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		
		
	}
	
	private void syncLocalToRemote() {
		try {
			this.git.pull().setCredentialsProvider(this.credentials).setRemote("origin").call();
		} catch (WrongRepositoryStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RefNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RefNotAdvertisedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
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
