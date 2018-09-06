package org.johnny.sql;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application{
	private Stage primaryStage;
	private VBox rootLayout;
    public static void main( String[] args ){
    	launch(args);
    }

	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		initRootLayout();
	}

	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("/view/AppLayout.fxml"));
			this.rootLayout = loader.load();
			Scene scene = new Scene(rootLayout,800,600);
			this.primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/png/sql_16x16.png")));
			this.primaryStage.setScene(scene);
			this.primaryStage.setResizable(false);
			this.primaryStage.setTitle("SQL Coder");
			this.primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
