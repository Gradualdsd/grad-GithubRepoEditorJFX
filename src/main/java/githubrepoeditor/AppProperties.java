package githubrepoeditor;

import java.io.File;

import javax.swing.JFileChooser;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppProperties {
	private static Stage stage;
    private static Scene scene;

    public static void setStage(Stage s) { stage = s; }
    public static Stage getStage() { return stage; }

    public static void setScene(Scene sc) { scene = sc; }
    public static Scene getScene() { return scene; }
    
    private static int debugprint_num = 0;
    public static void debugPrint(Object content) {
    	debugprint_num += 1;
    	IO.println(debugprint_num + " - " + content);
    }
    
    public static File askForSingleFolderAndReturn(String defaultPath, String acceptButtonText, String[] allowTypes) {
    	JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  // Only folders
        chooser.setCurrentDirectory(new File(defaultPath));
        int result = chooser.showDialog(null, acceptButtonText);

        if (result == JFileChooser.APPROVE_OPTION) {
        	File selectedFolder = chooser.getSelectedFile();
            System.out.println("Selected folder: " + selectedFolder.getAbsolutePath());
            return selectedFolder;
        }
        return null;
    }
    
    protected String getProjectFolder() {
    	String[] splitted = getClass().getResource("/").getPath().split("/");
    	return splitted[splitted.length-3];
    }
}
