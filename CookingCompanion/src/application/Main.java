package application;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * The Main class creates the UI, as well as the storage data files
 * the program will use, then will switch scenes.
 * 
 * @author Reed Olm - avr414 - UTSA CS 3443 - CookingCompanion 2021
 */
public class Main extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			//Create home scene
			Pane home = (Pane) FXMLLoader.load(getClass().getResource("Home.fxml"));
			Scene homeScene = new Scene(home, 1080, 810);
			homeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			
			//Create Data files if it doesn't exist
			try
			{
				File fileA = new File("src/application/data/recipes.csv");
				File fileB = new File("src/application/data/pass.csv");
				if(fileA.createNewFile())
				{}
				else
				{}
				if(fileB.createNewFile())
				{}
				else
				{}

			} catch(IOException e)
			{
				System.out.println("Error with file creation.");
				e.printStackTrace();
			}
			//Set up primary stage to open home, as well as load the stages
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
