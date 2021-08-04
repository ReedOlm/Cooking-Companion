package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The View class, represents the recipe Viewer View, and connects UI with 
 * back end and implements EventHandler to get action events from JavaFX 
 * and Initializable to switch scenes and update certain models.
 * 
 * @author Reed Olm - avr414 - UTSA CS 3443 - CookingCompanion 2021
 */
public class ViewController implements Initializable, EventHandler<ActionEvent>
{
	//The recipe that we are displaying is declared here
	Recipe recipe;

	// Sets ids for JavaFX
	@FXML
	public Button exitButton;

	@FXML
	public Text name, calories, servings;

	@FXML
	public ListView<String> tagList, instructions;

	@FXML
	public TableView<Ingredient> ingredientList;

	@FXML
	public TableColumn<Ingredient, String> colName, colAmount, colUnit;
	
	/**
	 * Overrides handle event to get action events from JavaFX exits
	 * the view screen when clicked.
	 * 
	 * @param event ActionEvent to get ActionEvent (ActionEvent)
	 */
	@Override // Happens when button is clicked
	public void handle(ActionEvent event)
	{
		if (event.getSource() == exitButton)
		{
			Stage appStage;
			Parent root;
			appStage = (Stage) exitButton.getScene().getWindow();
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		try
		{
			Recipe recipe = readRecipe(readPass());
			recipe.getCalories();
			name.setText(recipe.getName());
			calories.setText(String.valueOf((recipe.getCalories() / recipe.getServings())));
			servings.setText(String.valueOf(recipe.getServings()));
			
			ObservableList<Ingredient> list = ingredientList.getItems();
			for(int i = 0; i < recipe.getIngredients().size(); i++)
			{
				list.add(recipe.getIngredients().get(i));
			}
			ObservableList<String> tags = tagList.getItems();
			for(int i = 0; i < recipe.getTags().size(); i++)
			{
				tags.add(recipe.getTags().get(i));
			}
			ObservableList<String> prepList = instructions.getItems();
			for(int i = 0; i < recipe.getPrep().size(); i++)
			{
				prepList.add(recipe.getPrep().get(i));
			}
			
			tagList.setItems(tags);
			instructions.setItems(prepList);
			ingredientList.setItems(list);
			colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			colAmount.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
			colUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
			
		} catch (IOException e)
		{
			recipe = null;
			e.printStackTrace();
		}
	}

	private String readPass() throws IOException
	{
		String name = null;
		String row;
		BufferedReader csvReader = new BufferedReader(new FileReader("src/application/data/pass.csv"));
		while ((row = csvReader.readLine()) != null)
		{
			String[] data = row.split(",");
			name = data[0];
			csvReader.close();
			return name;
		}

		csvReader.close();
		return name;
	}

	private Recipe readRecipe(String name) throws IOException
	{
		String row;
		BufferedReader csvReader = new BufferedReader(new FileReader("src/application/data/recipes.csv"));
		while ((row = csvReader.readLine()) != null)
		{
			String[] data = row.split(",");
			int i = 0;
			int j = 0;
			ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
			ArrayList<String> tags = new ArrayList<String>();
			ArrayList<String> prep = new ArrayList<String>();

			if (data[0].equalsIgnoreCase(name))
			{
				int calories = Integer.parseInt(data[1]);
				int servings = Integer.parseInt(data[2]);
				int numTags = Integer.parseInt(data[3]);

				for (i = 0; i < numTags; i++)
				{
					tags.add(data[4 + i]);
				}

				int numSteps = Integer.parseInt(data[4 + i]);
				for (j = 0; j <= numSteps; j++)
				{
					prep.add(data[4 + i]);
					i++;
				}
				prep.remove(0);

				int numIngredients = Integer.parseInt(data[4 + i]);
				for (j = 0; j < numIngredients; j++)
				{
					String iName = data[5 + i];
					double iAmount = Double.parseDouble(data[6 + i]);
					EUnitType iUnit = EUnitType.fromString(data[7 + i]);
					int iCalories = Integer.parseInt(data[8 + i]);
					i = i + 4;

					Ingredient ingredient = new Ingredient(iName, iAmount, iUnit, iCalories);
					ingredients.add(ingredient);
				}

				Recipe recipe = new Recipe(name, servings, calories, ingredients, tags, prep);
				csvReader.close();
				return recipe;
			}
		}
		csvReader.close();
		return null;
	}

}
