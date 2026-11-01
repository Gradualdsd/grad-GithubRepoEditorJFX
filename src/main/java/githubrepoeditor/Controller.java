package githubrepoeditor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.jgit.api.Git;

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
    private TreeView<File> localDirectoryView;

    @FXML
    private TextArea localDirectoryView_TextFileEditor;
    
    
    private Scene scene;
    private Stage stage;
    
    @FXML
    void createNewRepo(ActionEvent event) {
    	RepositoryEditor repo = new RepositoryEditor("test_localrepo_path/repo","editortestbranch");
    	//repo.setCredentials("gradualdsd", "genuinely idk my password");
    	repo.create();
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
    
    private void openScreen(Pane screen) {
    	List<Node> screens = findNodesWithIdPrefix(scene.getRoot(), "Screen_");
    	screens.remove(screen);
    	
    	for (Node scrn : screens) {
    		scrn.setDisable(true);
    		scrn.setVisible(false);
    	}
    	screen.setDisable(false);
    	screen.setVisible(true);
    	onOpenScreen(screen);
    }
    
    private void onOpenScreen(Pane screen) {
    	switch (screen.getId().substring("Screen_".length())) {
    	case "Editor":
    		break;
    	default:
    		System.out.println("Yo you fucked up stupid idiot. You tried to load " + screen.getId() + " and it didn't work FUCKER!");
    	}
    	
    }
    
    private void onAppOpened() {
    	
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

