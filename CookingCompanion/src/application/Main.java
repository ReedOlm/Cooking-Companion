package application;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * The Main class contains the main method to run the program and creates the
 * UI, as well as the storage data files the program will use, then will switch
 * scenes.
 * 
 * @author Reed Olm - avr414 - UTSA CS 3443 - CookingCompanion 2021
 */
public class Main extends Application
{
	/**
	 * function to start JavaFX, creates the stage, and sets the first scene, the
	 * home scene. If the required data storage files do not exist, this will create
	 * them for us then swap to the first scene
	 * 
	 */
	@Override
	public void start(Stage primaryStage)
	{
		// Create Data files if it doesn't exist
		try
		{
			// Create directory if it doesn't exist
			new File("src/application/data").mkdirs();
			// Create files if it doesn't exist
			File fileA = new File("src/application/data/recipes.csv");
			File fileB = new File("src/application/data/pass.csv");
			// Could add new user message maybe? These only happen if new files are made.
			if (fileA.createNewFile())
			{
			} else
			{
			}
			if (fileB.createNewFile())
			{
			} else
			{
			}

		} catch (IOException e)
		{
			System.out.println("Error with file creation.");
			e.printStackTrace();
		}
		try
		{
			// Create home scene
			Pane home = (Pane) FXMLLoader.load(getClass().getResource("Home_Browse.fxml"));
			Scene homeScene = new Scene(home, 1080, 810);
			homeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// Set up primary stage to open home, as well as load the stages
			primaryStage.setScene(homeScene);
			primaryStage.setTitle("Cooking Companion - Home");
			primaryStage.show();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
