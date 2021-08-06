package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The Search class, represents the recipe Search View, and connects UI with
 * back end and implements EventHandler to get action events from JavaFX and
 * Initializable to switch scenes and update certain models.
 * 
 * @author Reed Olm - avr414 - UTSA CS 3443 - CookingCompanion 2021
 */
public class SearchController implements Initializable, EventHandler<ActionEvent>
{
	// The storing all matching recipes, all recipes, and pass.csv data
	ArrayList<Recipe> filteredRecipes = new ArrayList<Recipe>();
	ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	String[] passData;

	// Sets ids for JavaFX
	@FXML
	public Button exitButton, viewButton, editButton, deleteButton, confirmButton;

	@FXML
	public Text searchType, searchTarget, errorText;

	@FXML
	public TableView<Recipe> trimmedRecipeList;

	@FXML
	public TableColumn<Recipe, String> colName, colCals;

	/**
	 * Overrides handle event to get button action events from JavaFX, branching
	 * if-statements detect each specific button buttons include: Exit, View, Edit,
	 * and Delete, and Confirm
	 * 
	 * @param event ActionEvent to get ActionEvent (ActionEvent)
	 */
	@FXML
	@Override
	public void handle(ActionEvent event)
	{
		// Happens when exit button is clicked
		if (event.getSource() == exitButton)
		{
			// starts the process of swapping scenes to the Search scene
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
				e.printStackTrace();
			}
		}

		// Happens when view button is clicked
		else if (event.getSource() == viewButton)
		{
			if (trimmedRecipeList.getSelectionModel().getSelectedItem() != null)
			{
				// Write your data to the pass csv
				writePass(trimmedRecipeList.getSelectionModel().getSelectedItem().getName());

				// starts the process of swapping scenes to the View scene
				Stage appStage;
				Parent root;
				appStage = (Stage) viewButton.getScene().getWindow();
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

		// Happens when edit button is clicked
		else if (event.getSource() == editButton)
		{
			if (trimmedRecipeList.getSelectionModel().getSelectedItem() != null)
			{
				// Write data to pass csv
				writePass(trimmedRecipeList.getSelectionModel().getSelectedItem().getName());

				// starts the process of swapping scenes to the Edit scene
				Stage appStage;
				Parent root;
				appStage = (Stage) editButton.getScene().getWindow();
				try
				{
					root = FXMLLoader.load(getClass().getResource("Editor.fxml"));
					Scene scene = new Scene(root);
					appStage.setScene(scene);
					;
					appStage.setTitle("Cooking Companion - Edit Recipe");
					appStage.show();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		// Happens when confirm button is clicked
		else if (event.getSource() == confirmButton)
		{
			if (trimmedRecipeList.getSelectionModel().getSelectedItem() != null)
			{
				// Removes the deleted recipe from both the array list and the Table.
				// Calls save after removing from the arrayList, which saves it and makes it
				// permanent
				String name = trimmedRecipeList.getSelectionModel().getSelectedItem().getName();
				ObservableList<Recipe> list = trimmedRecipeList.getItems();
				list.remove(trimmedRecipeList.getSelectionModel().getSelectedItem());
				trimmedRecipeList.setItems(list);

				for (int i = 0; i < recipes.size(); i++)
				{
					if (recipes.get(i).getName().equals(name))
					{
						recipes.remove(i);
					}
				}
				try
				{
					saveData();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			// Disables the confirm button
			confirmButton.setDisable(true);
		}

		// Happens when delete button is clicked
		else if (event.getSource() == deleteButton)
		{
			// Activates the confirm button
			if (trimmedRecipeList.getSelectionModel().getSelectedItem() != null)
			{
				confirmButton.setDisable(false);
			}
		}
	}

	/**
	 * MouseEvent action event detector, that disables the confirm button if you
	 * select another recipe after pressing delete, but before confirming. This
	 * prevents users from accidentally deleting things they don't want without a
	 * confirmation
	 * 
	 * @param event MouseEvent to get MouseEvent (MouseEvent)
	 */
	@FXML
	private void confirmCancel(MouseEvent event)
	{
		confirmButton.setDisable(true);
	}

	/**
	 * Overrides initialize event to initialize previously declared FXML data and
	 * data containers
	 * 
	 * @param arg0 URL default initialize param (URL)
	 * @param arg1 ResourceBundle default initialize param (ResourceBundle)
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		try
		{
			// Reads from pass.csv and stores it
			passData = readPass();
			// Reads from recipes.csv and stores it in Recipes ArrayList
			readRecipes();
			// Fills filteredRecipes arrayList with only recipes that match the passData
			// data from readPass
			filterRecipes(passData[0], passData[1]);

			// Sets what information can be displayed in the Table view columns with a
			// lambda expression using wrappers in the Recipe.java file
			colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			colCals.setCellValueFactory(cellData -> cellData.getValue().calsProperty());
			// Sets selection model to only allow one recipe to be selected at a time
			trimmedRecipeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

			// Fills out table view with the recipes that matched the search target from the
			// ArrayList
			ObservableList<Recipe> list = trimmedRecipeList.getItems();
			for (int i = 0; i < filteredRecipes.size(); i++)
			{
				list.add(filteredRecipes.get(i));
			}
			trimmedRecipeList.setItems(list);

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Function that reads from pass.csv, and returns the target value we are
	 * searching for and the type of the search in an array of Strings
	 * 
	 * @return String[] the search value, and the type of search
	 */
	private String[] readPass() throws IOException
	{
		String row;
		BufferedReader csvReader = new BufferedReader(new FileReader("src/application/data/pass.csv"));
		// 4 ifs to determine the search type, and store the target
		while ((row = csvReader.readLine()) != null)
		{
			String[] data = row.split(",");
			String[] ret = { "", "" };

			if (data[0] != "")
			{
				ret[0] = data[0];
				ret[1] = "Name";
			} else if (data[1] != "")
			{
				ret[0] = data[1];
				ret[1] = "Ingredient";
			} else if (data[2] != "")
			{
				ret[0] = data[2];
				ret[1] = "Tag";
			} else if (data[3] != "")
			{
				ret[0] = data[3];
				ret[1] = "Calories";
			}
			csvReader.close();
			return ret;
		}

		csvReader.close();
		return null;
	}

	/**
	 * Function that reads from recipes.csv, and places the parsed information into
	 * the recipes ArrayList for the current view.
	 * 
	 */
	private void readRecipes() throws IOException
	{
		String row;
		BufferedReader csvReader = new BufferedReader(new FileReader("src/application/data/recipes.csv"));
		while ((row = csvReader.readLine()) != null)
		{
			// Reads csv and inputs data into Recipe objects following my file formatting.
			// Field 1: Name, Field 2: Total Calories, Field 3: Servings in the Recipe
			// Field 4: x Number of Tags, next x Fields are the tags.
			// Field 4+x: y Number of Ingredients, next y fields are the tags.
			// Field 4+x+y: z Number of Instructions, next z fields are the instructions.
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
			prep.remove(0); // Fixes an off-By-One error

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

			// Adds recipe to arraylist
			Recipe recipe = new Recipe(name, servings, calories, ingredients, tags, prep);
			recipes.add(recipe);
		}
		csvReader.close();
	}

	/**
	 * Searches based on search type given, Recipe name, ingredient name, tag, or a
	 * number of calories per serving that serves as a maximum number. Also sorts by
	 * Recipe name for first 3 search types, and sorts by calories per serving for
	 * the last search type.
	 * 
	 * @param target name, ingredient name, tag, or max calories (String)
	 * @param type   search type (String)
	 */
	private void filterRecipes(String target, String type)
	{
		// Displays what the search was at top of view
		searchTarget.setText(target);
		searchType.setText(type + " Search");

		// Name search
		if (type == "Name")
		{
			for (int i = 0; i < recipes.size(); i++)
			{
				if (recipes.get(i).getName().toLowerCase().contains(target.toLowerCase()))
				{
					filteredRecipes.add(recipes.get(i));
				}
			}
			// Sort list by name
			filteredRecipes.sort(Comparator.comparing(Recipe::getName));
		}

		// Ingredient search
		else if (type == "Ingredient")
		{
			for (int i = 0; i < recipes.size(); i++)
			{
				for (int j = 0; j < recipes.get(i).getIngredients().size(); j++)
				{
					if (recipes.get(i).getIngredients().get(j).getName().toLowerCase().contains(target.toLowerCase()))
					{
						filteredRecipes.add(recipes.get(i));
					}
				}
			}
			// Sort list by name
			filteredRecipes.sort(Comparator.comparing(Recipe::getName));
		}

		// Tag search
		else if (type == "Tag")
		{
			for (int i = 0; i < recipes.size(); i++)
			{
				for (int j = 0; j < recipes.get(i).getTags().size(); j++)
				{
					if (recipes.get(i).getTags().get(j).toLowerCase().contains(target.toLowerCase()))
					{
						filteredRecipes.add(recipes.get(i));
					}
				}
			}
			// Sort list by Tag
			filteredRecipes.sort(Comparator.comparing(Recipe::getName));
		}

		// Calories search
		else if (type == "Calories")
		{
			int maxCals = Integer.valueOf(target);
			for (int i = 0; i < recipes.size(); i++)
			{
				if (recipes.get(i).getCalories() / recipes.get(i).getServings() <= maxCals)
				{
					filteredRecipes.add(recipes.get(i));
				}
			}
			// Sort list by calories per serving
			filteredRecipes.sort(Comparator.comparing(Recipe::getCaloriesPerServing));
		}
	}

	/**
	 * Function that writes to pass.csv the name of the Recipe we are going to
	 * edit/view
	 * 
	 * @param selectedItem name of the Recipe we are writing the the file (String)
	 */
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

	/**
	 * Saves the data from the ArrayList into the recipes.csv file, used for
	 * deleting
	 * 
	 */
	private void saveData() throws IOException
	{
		FileWriter writer;
		try
		{
			writer = new FileWriter("src/application/data/recipes.csv", false);
			for (int x = 0; x < recipes.size(); x++)
			{
				Recipe cur = recipes.get(x);
				// write Recipe name
				writer.append(cur.getName());
				writer.append(",");
				// write Recipe Calories
				writer.append(String.valueOf(cur.getCalories()));
				writer.append(",");
				// write Recipe Servings
				writer.append(String.valueOf(cur.getServings()));
				writer.append(",");
				// write numTags and Tags
				writer.append(String.valueOf(cur.getTags().size()));
				writer.append(",");
				for (int i = 0; i < cur.getTags().size(); i++)
				{
					writer.append(cur.getTags().get(i));
					writer.append(",");
				}
				// write numSteps and Steps
				writer.append(String.valueOf(cur.getPrep().size()));
				writer.append(",");
				for (int i = 0; i < cur.getPrep().size(); i++)
				{
					writer.append(cur.getPrep().get(i));
					writer.append(",");
				}

				// write numIngredients and Ingredients
				writer.append(String.valueOf(cur.getIngredients().size()));
				writer.append(",");
				for (int i = 0; i < cur.getIngredients().size(); i++)
				{
					writer.append(cur.getIngredients().get(i).getName());
					writer.append(",");
					writer.append(String.valueOf(cur.getIngredients().get(i).getAmount()));
					writer.append(",");
					writer.append(cur.getIngredients().get(i).getUnit().getText());
					writer.append(",");
					writer.append(String.valueOf(cur.getIngredients().get(i).getCalories()));
					writer.append(",");
				}
				writer.append("\n");
			}

			writer.flush();
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
