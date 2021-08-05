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
	 * Overrides handle event to get action events from JavaFX exits the view screen
	 * when clicked.
	 * 
	 * @param event ActionEvent to get ActionEvent (ActionEvent)
	 */
	@Override
	public void handle(ActionEvent event)
	{
		// Happens when exit button is clicked
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

				// goes to the View Screen
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

				// Goes to edit screen
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

			confirmButton.setDisable(true);
		}

		// Happens when delete button is clicked
		else if (event.getSource() == deleteButton)
		{
			if (trimmedRecipeList.getSelectionModel().getSelectedItem() != null)
			{
				confirmButton.setDisable(false);
			}
		}
	}

	@FXML
	private void confirmCancel(MouseEvent event)
	{
		confirmButton.setDisable(true);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		try
		{
			passData = readPass();
			readRecipes();
			filterRecipes(passData[0], passData[1]);

			colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			colCals.setCellValueFactory(cellData -> cellData.getValue().calsProperty());
			trimmedRecipeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			ObservableList<Recipe> list = trimmedRecipeList.getItems();
			for (int i = 0; i < filteredRecipes.size(); i++)
			{
				list.add(filteredRecipes.get(i));
			}
			trimmedRecipeList.setItems(list);

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Reads pass.csv, determines and returns search type and target
	private String[] readPass() throws IOException
	{
		String row;
		BufferedReader csvReader = new BufferedReader(new FileReader("src/application/data/pass.csv"));
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

	private void readRecipes() throws IOException
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

	private void filterRecipes(String target, String type)
	{
		searchTarget.setText(target);
		searchType.setText(type + " Search");

		// Name
		if (type == "Name")
		{
			for (int i = 0; i < recipes.size(); i++)
			{
				if (recipes.get(i).getName().toLowerCase().contains(target.toLowerCase()))
				{
					filteredRecipes.add(recipes.get(i));
				}
			}
			filteredRecipes.sort(Comparator.comparing(Recipe::getName));
		}

		// Ingredient
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
			filteredRecipes.sort(Comparator.comparing(Recipe::getName));
		}

		// Tag
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
			filteredRecipes.sort(Comparator.comparing(Recipe::getName));
		}

		// Calories
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
			filteredRecipes.sort(Comparator.comparing(Recipe::getCaloriesPerServing));
		}
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

	// This puts the recipe arraylist into the listview
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
