package application;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The HomeController class, represents the Home View, and connects UI with back
 * end and implements EventHandler to get action events from JavaFX and
 * Initializable to switch scenes and update certain models.
 * 
 * @author Reed Olm - avr414 - UTSA CS 3443 - CookingCompanion 2021
 */
public class HomeController implements EventHandler<ActionEvent>, Initializable
{

	// Sets ids for JavaFX
	@FXML
	Button createRecipeButton, searchButton, browseRecipesButton;

	@FXML
	TextField nameSearch, ingredientSearch, tagSearch, caloriesSearch;

	@FXML
	Text errorText;

	/**
	 * Overrides handle event to get action events from JavaFX shuffles 3 cards
	 * after button is pressed
	 * 
	 * @param event ActionEvent to get ActionEvent (ActionEvent)
	 */
	@Override
	public void handle(ActionEvent event)
	{
		// Determines which button is pressed
		// Search is clicked
		if (event.getSource() == searchButton)
		{
			// if all 4 text fields are empty, produce error message
			String name = nameSearch.getText();
			String ingredient = ingredientSearch.getText();
			String tag = tagSearch.getText();
			int calorie = -2;
			try
			{
				if (caloriesSearch.getText() != "")
				{
					calorie = Integer.parseInt(caloriesSearch.getText());
				}
			} catch (NumberFormatException e)
			{
				errorText.setText("Error: Please enter a number when searching for calories.");
				calorie = -1;
			}

			if (name == "" && ingredient == "" && tag == "" && calorie == -2)
			{
				errorText.setText("Error: Please enter at least one search option, then try your search again.");
			} else if (calorie == -1)
			{
				// Do nothing
			} else

			{
				// Write your data to the pass csv
				try
				{
					FileWriter writer = new FileWriter("src/application/data/pass.csv", false);
					writer.append(nameSearch.getText());
					writer.append(",");
					writer.append(ingredientSearch.getText());
					writer.append(",");
					writer.append(tagSearch.getText());
					writer.append(",");
					writer.append(caloriesSearch.getText());
					writer.append("\n");
					writer.flush();
					writer.close();

				} catch (IOException e1)
				{
					e1.printStackTrace();
				}

				// goes to the Search Screen
				Stage appStage;
				Parent root;
				appStage = (Stage) createRecipeButton.getScene().getWindow();
				try
				{
					root = FXMLLoader.load(getClass().getResource("Search.fxml"));
					Scene scene = new Scene(root);
					appStage.setScene(scene);
					appStage.setTitle("Cooking Companion - Search");
					appStage.show();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// create is clicked
		else if (event.getSource() == createRecipeButton)
		{
			// Opens the Blank recipe editor
			Stage appStage;
			Parent root;
			appStage = (Stage) createRecipeButton.getScene().getWindow();
			try
			{
				root = FXMLLoader.load(getClass().getResource("Create.fxml"));
				Scene scene = new Scene(root);
				appStage.setScene(scene);
				appStage.setTitle("Cooking Companion - New Recipe Creator");
				appStage.show();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// browse is clicked
		else if (event.getSource() == browseRecipesButton)
		{
			// Opens the Home screen with browser filled
			Stage appStage;
			Parent root;
			appStage = (Stage) createRecipeButton.getScene().getWindow();
			try
			{
				root = FXMLLoader.load(getClass().getResource("Home_Browse.fxml"));
				Scene scene = new Scene(root);
				appStage.setScene(scene);
				appStage.setTitle("Cooking Companion - Home");
				appStage.show();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void clearTextFields(MouseEvent event)
	{
		nameSearch.clear();
		ingredientSearch.clear();
		tagSearch.clear();
		caloriesSearch.clear();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		// TODO Auto-generated method stub

	}
}
