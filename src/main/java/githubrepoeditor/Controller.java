package githubrepoeditor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

//import advancedBeeMaker.App;

public class Controller {
	
	@FXML
    private MenuItem MenuItem_NewRepo;

    @FXML
    private BorderPane Screen_Editor;
    
    @FXML
    private BorderPane Screen_Editor2;

    @FXML
    private TreeView<File> localDirectoryView;

    @FXML
    private TextArea localDirectoryView_TextFileEditor;
    
    @FXML
    private MenuItem MenuItem_Push;
    
    @FXML
    private MenuItem MenuItem_OpenLocalRepo;
    
    @FXML
    private MenuBar editorMenuBar;

    @FXML
    private TreeView<File> directoryView2;
    
    @FXML
    private TextField commitTitleInput;
    
    @FXML
    private TextField repoNameInput;
    
    @FXML
    private ComboBox<String> CoBox_BranchChooser;
    
    //Login Screen
    @FXML
    private BorderPane Screen_Login;

    @FXML
    private PasswordField accessorInput;
    
    @FXML
    private Label accessorInputLabel;
    
    @FXML
    private Button loginButton;

    @FXML
    private Label loginErrorsLabel;

    @FXML
    private CheckBox rememberMeToggle;

    @FXML
    private CheckBox useSSHKeyToggle;

    @FXML
    private TextField usernameInput;
    
    
    private RepositoryEditor editor;
    
    @FXML
    void createNewRepo(ActionEvent event) {
    	//repo.setCredentials("gradualdsd", "genuinely idk my password");
    	editor.create();
    	this.loadRepo(new File(editor.getCurrentRepositoryPath()));
    }
    
    private void loadRepo(File repo) {
    	ObservableList<String> branchnames = FXCollections.observableArrayList();
    	//branchnames.add("main"); // FIXME: TEMP UNTIL WE CAN FIGURE OUT WHY WE DON'T GET THIS!!!
    	branchnames.addAll(editor.getBranchNames("local"));
    	System.out.println(branchnames.getLast());
    	CoBox_BranchChooser.setItems(branchnames);
    	CoBox_BranchChooser.setValue(editor.getCurrentRepositoryBranchName()); // force load on the main branch
    	repoNameInput.setText(editor.getName());
    	this.updateDirectoryViewers(repo);

    }
    
    private void updateDirectoryViewers(File repo) {
    	localDirectoryView.setRoot(getFileTreeFromDirectory(repo));
    	directoryView2.setRoot(getFileTreeFromDirectory(repo));
    }
    
    private TreeItem<File> getFileTreeFromDirectory(File f) {
        TreeItem<File> item = new TreeItem<>();
        item.setValue(f);
        if (f.isDirectory()) {
            for (File child : f.listFiles()) {
                item.getChildren().add(getFileTreeFromDirectory(child));
            }
        }
        return item;
    }
    
    /**
     * Recursively finds all nodes whose ID starts with the given prefix.
     *
     * @param parent The root node to search from.
     * @param prefix The prefix to match.
     * @return A list of matching nodes.
     */
    public static List<Node> findNodesWithIdPrefix(Parent parent, String prefix) {
        List<Node> result = new ArrayList<>();
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node.getId() != null && node.getId().startsWith(prefix)) {
                result.add(node);
            }
            if (node instanceof Parent) {
                result.addAll(findNodesWithIdPrefix((Parent) node, prefix));
            }
            //Git.init().
        }
        return result;
    }
    
    @FXML
    void push(ActionEvent event) {
    	
    	editor.push("Editor Test Commit");
    }
    
    @FXML
    void openlocalrepo(ActionEvent event) {
    	
    	editor.AskToOpen();
    	this.loadRepo(editor.getCurrentRepository().getDirectory());
    }
    
    private void openScreen(Pane screen) {
    	List<Node> screens = findNodesWithIdPrefix(MainApp.scene.getRoot(), "Screen_");
    	System.out.println(screens);
    	screens.remove(screen);
    	
    	for (Node scrn : screens) {
    		scrn.setDisable(true);
    		scrn.setVisible(false);
    	}
    	screen.setDisable(false);
    	screen.setVisible(true);
    	System.out.println(screen.isVisible());
    	System.out.println(screen.isDisabled());
    	System.out.println(screen.isDisable());
    	this.onOpenScreen(screen);
    }
    
    private void onOpenScreen(Pane screen) {
    	switch (screen.getId().substring("Screen_".length())) {
    	case "Editor2":
    		editorMenuBar.setDisable(false);
    		editorMenuBar.setVisible(true);
    		break;
    		
    	case "Login":
    		editorMenuBar.setDisable(true);
    		editorMenuBar.setVisible(false);
    		
    		loginErrorsLabel.setText("");
    		loginButton.setOnAction(event -> {
    			String username = usernameInput.getText();
    			String accessor = accessorInput.getText();
    			
    			if (username.isBlank() || accessor.isBlank()) {
    				loginErrorsLabel.setText("Username or Accessor is blank");
    			}
    			
    			editor = new RepositoryEditor(); // temp, also make it so that you have to set these parameters in the editor
    			
    			//TODO: Check if the textbox inputs are valid
    			
    			if (useSSHKeyToggle.isSelected()) {
    				//TODO: Later Add SSH Key Functionality
    			} else {
    				editor.setCredentials(username, accessor);
    			}
    			
    			this.openScreen(Screen_Editor2);
    			
    		});
    		
    		useSSHKeyToggle.setOnAction(event -> {
    			if (useSSHKeyToggle.isSelected()) {
    				accessorInputLabel.setText("SSH Key:");
    			} else {
    				accessorInputLabel.setText("Personal Access Token:");
    			}
    		});
    		
    		break;
    	default:
    		System.out.println("Yo you fucked up stupid idiot. You tried to load " + screen.getId() + " and it didn't work FUCKER!");
    	}
    	
    }
    
    private void onAppOpened() {
    	this.openScreen(Screen_Login);
    }
    
    private void onCreate() {
    	
    	//AppProperties.debugPrint(((HBox) scene.lookup("#FXID_Test")).getChildren().getText());
    }
    
    public void initialize() {
    	onCreate();
    	
    	Platform.runLater(() -> {
    		onAppOpened();
    	});
    	
    	
    	
    	//scene.
    	//AppProperties.debugPrint("Welcome back to Java, Gradualdsd.");
    	//AppProperties.debugPrint(hBox_TEMP_TextField.getId());
    	
    	
    	//IO.println(getOptionId("FXID_Test"));
    }

}

