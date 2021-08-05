package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The Home_BrowseController class, represents the Home View after data is
 * loaded, and connects UI with back end and implements EventHandler to get
 * action events from JavaFX and Initializable to switch scenes and update
 * certain models.
 * 
 * @author Reed Olm - avr414 - UTSA CS 3443 - CookingCompanion 2021
 */
public class Home_BrowseController implements EventHandler<ActionEvent>, Initializable
{
	// Load recipes from file, place them into this ArrayList
	ObservableList<String> list = FXCollections.observableArrayList();
	ArrayList<Recipe> recipes = new ArrayList<Recipe>();

	// Sets ids for JavaFX
	@FXML
	TableView<Recipe> browseTable;
	
	@FXML
	TableColumn<Recipe, String> nameCol, cpsCol;

	@FXML
	Button createRecipeButton, searchButton, editRecipeButton, viewRecipeButton;

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
				appStage.show();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// edit is clicked
		else if (event.getSource() == editRecipeButton)
		{
			if (browseTable.getSelectionModel().getSelectedItem() != null)
			{
				// Write your data to the pass csv
				writePass(browseTable.getSelectionModel().getSelectedItem().getName());
//				if (browseTable.getSelectionModel().getSelectedItem().getName() == "null")
//				{
//					//When the hell can this happen
//					System.out.println("Swag");
//				}

				// goes to the Search Screen
				Stage appStage;
				Parent root;
				appStage = (Stage) createRecipeButton.getScene().getWindow();
				try
				{
					root = FXMLLoader.load(getClass().getResource("Editor.fxml"));
					Scene scene = new Scene(root);
					appStage.setScene(scene);
					appStage.setTitle("Cooking Companion - Editor");
					appStage.show();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
			{
				errorText.setText("Error: Please choose a recipe.");
			}

		}

		// view is clicked
		else if (event.getSource() == viewRecipeButton)
		{
			if (browseTable.getSelectionModel().getSelectedItem() != null)
			{
				// Write your data to the pass csv
				writePass(browseTable.getSelectionModel().getSelectedItem().getName());

				// goes to the View Screen
				Stage appStage;
				Parent root;
				appStage = (Stage) viewRecipeButton.getScene().getWindow();
				try
				{
					root = FXMLLoader.load(getClass().getResource("View.fxml"));
					Scene scene = new Scene(root);
					appStage.setScene(scene);
					appStage.setTitle("Cooking Companion - View Recipe");
					appStage.show();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			} else
			{
				errorText.setText("Error: Please choose a recipe.");
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
		nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		cpsCol.setCellValueFactory(cellData -> cellData.getValue().calsProperty());
		browseTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		// Update listview
		try
		{
			readData();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadData();
	}

	void initData(String test)
	{
		tagSearch.setText(test);
	}

	// This reads the saved recipe information into a recipe arrayList
	private void readData() throws IOException
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

			String name = data[0];
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
			recipes.add(recipe);
		}
		csvReader.close();
	}

	// This puts the recipe arraylist into the listview
	private void loadData()
	{
		ObservableList<Recipe> list = browseTable.getItems();
		for(int i = 0; i < recipes.size(); i++)
		{
			list.add(recipes.get(i));
		}
		browseTable.setItems(list);
	}

	// This writes data to pass.csv
	private void writePass(String selectedItem)
	{
		try
		{
			FileWriter writer = new FileWriter("src/application/data/pass.csv", false);
			writer.append(selectedItem);
			writer.append("\n");
			writer.flush();
			writer.close();

		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}
}
