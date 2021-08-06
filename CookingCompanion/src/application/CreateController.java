package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The CreateController class, represents the Create new recipe view, and
 * connects UI with back end and implements EventHandler to get action events
 * from JavaFX and Initializable to switch scenes and update certain models.
 * 
 * @author Reed Olm - avr414 - UTSA CS 3443 - CookingCompanion 2021
 */
public class CreateController implements Initializable, EventHandler<ActionEvent>
{
	// declaring array lists to hold All recipes, and the current ingredient
	ArrayList<Recipe> recipes = new ArrayList<Recipe>();

	@FXML
	public Button exitWithoutSaving, save, addIngredient, removeIngredient, addTag, removeTag, addInstruction, removeInstruction;

	@FXML
	public ChoiceBox<EUnitType> unitSelection;

	@FXML
	public ListView<String> tagList, instructions;

	@FXML
	public TableView<Ingredient> ingredientList;

	@FXML
	public TableColumn<Ingredient, String> colName, colAmount, colUnit, colCalories;

	@FXML
	public Text caloriesTotal, ingError;

	@FXML
	public TextField enterCalories, enterAmount, enterName, enterTag, recipeName, totalServings, enterInstruction;

	/**
	 * Overrides handle event to get button action events from JavaFX, branching
	 * if-statements detect each specific button buttons include: Exit, Save,
	 * add/remove ingredient, add/remove tag, add/remove instruction
	 * 
	 * @param event ActionEvent to get ActionEvent (ActionEvent)
	 */
	@FXML
	@Override
	public void handle(ActionEvent event)
	{
		// Exit button detected
		if (event.getSource() == exitWithoutSaving)
		{
			// starts the process of swapping scenes to the Home scene
			Stage appStage;
			Parent root;
			appStage = (Stage) exitWithoutSaving.getScene().getWindow();
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

		// Save button detected
		else if (event.getSource() == save)
		{
			// Produces an error if a comma is used, or if the recipe name is blank
			if (recipeName.getText().contains(","))
			{
				ingError.setText("Please don't use commas.");
			} else if (recipeName.getText().equals(""))
			{
				ingError.setText("Please enter a recipe name.");
			}
			// Tries to save data unless a non-number is entered in the servings
			else
			{
				try
				{
					// Catches non int values in field
					@SuppressWarnings("unused")
					int servingTest = Integer.valueOf(totalServings.getText());
					// Calls save data function to save stuff
					saveData(recipeName.getText());
				} catch (NumberFormatException e)
				{
					ingError.setText("Error: Please only use numbers in the serving size");
				}
			}
		}

		// AddInstruction button detected
		else if (event.getSource() == addInstruction)
		{
			// Catches empty inputs and commas, then stores the input
			if (enterInstruction.getText() != "")
			{
				if (enterInstruction.getText().contains(","))
				{
					ingError.setText("Please don't use commas.");
				} else
				{
					String entered = enterInstruction.getText();
					instructions.getItems().addAll(entered);
					enterInstruction.clear();
				}
			}
		}

		// RemoveInstruction button detected
		else if (event.getSource() == removeInstruction)
		{
			ObservableList<String> list = instructions.getItems();
			if (list.contains(enterInstruction.getText()))
			{
				list.remove(enterInstruction.getText());
			}
			enterInstruction.clear();
		}

		// Add Tag button detected
		else if (event.getSource() == addTag)
		{
			// Catches empty inputs and commas, then stores the input
			if (enterTag.getText() != "")
			{
				if (enterTag.getText().contains(","))
				{
					ingError.setText("Please don't use commas.");
				} else
				{
					String entered = enterTag.getText();
					tagList.getItems().addAll(entered);
					enterTag.clear();
				}
			}
		}

		// Remove Tag button detected
		else if (event.getSource() == removeTag)
		{
			ObservableList<String> list = tagList.getItems();
			if (list.contains(enterTag.getText()))
			{
				list.remove(enterTag.getText());
			}
			enterTag.clear();
		}

		// Add Ingredient button detected
		else if (event.getSource() == addIngredient)
		{
			ObservableList<Ingredient> list = ingredientList.getItems();
			// Catches empty inputs, text in number fields, and commas, then stores the
			// input
			try
			{
				if (enterName.getText() != "" && enterAmount.getText() != "" && unitSelection.getValue() != null && enterCalories.getText() != "")
				{
					if (enterName.getText().contains(","))
					{
						ingError.setText("Please don't use commas.");
					} else
					{
						Ingredient ing = new Ingredient(enterName.getText(), Double.valueOf(enterAmount.getText()), unitSelection.getValue(), Integer.valueOf(enterCalories.getText()));
						list.add(ing);
						ingredientList.setItems(list);
						enterName.clear();
						enterAmount.clear();
						unitSelection.getSelectionModel().clearSelection();
						unitSelection.setValue(null);
						enterCalories.clear();
					}
				} else
				{
					ingError.setText("Error: Fill out all 4 ingredient fields.");
				}
				int sum = 0;
				for (int i = 0; i < list.size(); i++)
				{
					sum += list.get(i).getCalories();
				}
				caloriesTotal.setText(String.valueOf(sum));
			} catch (NumberFormatException e)
			{
				ingError.setText("Error: Only enter numbers into amount and calorie boxes.");
			}
		}

		// Remove Ingredient button detected
		else if (event.getSource() == removeIngredient)
		{
			ObservableList<Ingredient> list = ingredientList.getItems();
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i).getName().equals(enterName.getText()))
				{
					list.remove(i);
				}
			}
			int sum = 0;
			for (int i = 0; i < list.size(); i++)
			{
				sum += list.get(i).getCalories();
			}
			caloriesTotal.setText(String.valueOf(sum));
		}

	}

	/**
	 * MouseEvent action event detector, that allows program to detect which
	 * list/table/column was clicked, then gather the item that was clicked, and
	 * will place the item into it's respective input box, this allows the user to
	 * select an item to delete easily
	 * 
	 * @param event MouseEvent to get MouseEvent (MouseEvent)
	 */
	@FXML
	private void putSelectedListItemInBox(MouseEvent event)
	{
		// Resets error message
		ingError.setText("");

		// Instruction selection
		if (event.getSource() == instructions)
		{
			enterInstruction.setText(instructions.getSelectionModel().getSelectedItem());
		}

		// Tag selection
		else if (event.getSource() == tagList)
		{
			enterTag.setText(tagList.getSelectionModel().getSelectedItem());
		}

		// Ingredient selection
		else if (event.getSource() == ingredientList)
		{
			if (ingredientList.getSelectionModel().getSelectedItem() != null)
			{
				enterName.setText(ingredientList.getSelectionModel().getSelectedItem().getName());
				enterAmount.setText(String.valueOf(ingredientList.getSelectionModel().getSelectedItem().getAmount()));
				unitSelection.setValue(ingredientList.getSelectionModel().getSelectedItem().getUnit());
				enterCalories.setText(String.valueOf(ingredientList.getSelectionModel().getSelectedItem().getCalories()));
			}
		}
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
		// Set error text to empty
		ingError.setText("");
		// Sets ChoiceBox options to be that of my Enum UnitType.java file
		unitSelection.getItems().setAll(EUnitType.values());

		// Sets selection modes for lists/table Views to single selection
		instructions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tagList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		ingredientList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// Sets the table to properly place Ingredient info into the correct columns
		// using
		// a lambda expression and wrappers from the Ingredient java file
		colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		colAmount.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
		colUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
		colCalories.setCellValueFactory(cellData -> cellData.getValue().caloriesProperty());

		// Default number of calories
		caloriesTotal.setText("0");
		// Try to call readData();
		try
		{
			readData();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Function that reads from recipes.csv, and places the parsed information into
	 * the recipes ArrayList for the current view.
	 * 
	 */
	private void readData() throws IOException
	{
		String row;
		BufferedReader csvReader = new BufferedReader(new FileReader("src/application/data/recipes.csv"));
		// Parse file
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

			// Adds recipe to the arraylist
			Recipe recipe = new Recipe(name, servings, calories, ingredients, tags, prep);
			recipes.add(recipe);
		}
		csvReader.close();
	}

	/**
	 * Takes the information from the view to save it to file. If the name of the
	 * current recipe is already in storage, it will be overwritten and saved anew.
	 * 
	 * @param overwrite name of the current recipe, if it already exists, it will
	 *                  overwrite it in the file (String)
	 */
	private void saveData(String overwrite)
	{
		// Name comparator
		int oIndex = -1;
		for (int i = 0; i < recipes.size(); i++)
		{
			if (recipes.get(i).getName().equalsIgnoreCase(overwrite))
				oIndex = i;
		}
		// New-recipe writer
		if (oIndex == -1)
		{
			try
			{
				FileWriter writer = new FileWriter("src/application/data/recipes.csv", true);
				// write Recipe name
				writer.append(recipeName.getText());
				writer.append(",");
				// write Recipe Calories
				writer.append(caloriesTotal.getText());
				writer.append(",");
				// write Recipe Servings
				writer.append(totalServings.getText());
				writer.append(",");
				// write numTags and Tags
				ObservableList<String> listOfTags = tagList.getItems();
				writer.append(String.valueOf(listOfTags.size()));
				writer.append(",");
				for (int i = 0; i < listOfTags.size(); i++)
				{
					writer.append(listOfTags.get(i));
					writer.append(",");
				}
				// write numSteps and Steps
				ObservableList<String> listOfInstructions = instructions.getItems();
				writer.append(String.valueOf(listOfInstructions.size()));
				writer.append(",");
				for (int i = 0; i < listOfInstructions.size(); i++)
				{
					writer.append(listOfInstructions.get(i));
					writer.append(",");
				}

				// write numIngredients and Ingredients
				ObservableList<Ingredient> listOfIngredients = ingredientList.getItems();
				writer.append(String.valueOf(listOfIngredients.size()));
				writer.append(",");
				for (int i = 0; i < listOfIngredients.size(); i++)
				{
					writer.append(listOfIngredients.get(i).getName());
					writer.append(",");
					writer.append(String.valueOf(listOfIngredients.get(i).getAmount()));
					writer.append(",");
					writer.append(listOfIngredients.get(i).getUnit().getText());
					writer.append(",");
					writer.append(String.valueOf(listOfIngredients.get(i).getCalories()));
					writer.append(",");
				}
				writer.append("\n");

				writer.flush();
				writer.close();

			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		// Writer that overwrites the matching recipe name's recipe
		else
		{
			FileWriter writer;
			try
			{
				writer = new FileWriter("src/application/data/recipes.csv", false);
				for (int x = 0; x < recipes.size(); x++)
				{
					// detects the matching recipe, and instead of writing from the array, writes
					// from the text fields
					if (oIndex == x)
					{
						// write Recipe name
						writer.append(recipeName.getText());
						writer.append(",");
						// write Recipe Calories
						writer.append(caloriesTotal.getText());
						writer.append(",");
						// write Recipe Servings
						writer.append(totalServings.getText());
						writer.append(",");
						// write numTags and Tags
						ObservableList<String> listOfTags = tagList.getItems();
						writer.append(String.valueOf(listOfTags.size()));
						writer.append(",");
						for (int i = 0; i < listOfTags.size(); i++)
						{
							writer.append(listOfTags.get(i));
							writer.append(",");
						}
						// write numSteps and Steps
						ObservableList<String> listOfInstructions = instructions.getItems();
						writer.append(String.valueOf(listOfInstructions.size()));
						writer.append(",");
						for (int i = 0; i < listOfInstructions.size(); i++)
						{
							writer.append(listOfInstructions.get(i));
							writer.append(",");
						}

						// write numIngredients and Ingredients
						ObservableList<Ingredient> listOfIngredients = ingredientList.getItems();
						writer.append(String.valueOf(listOfIngredients.size()));
						writer.append(",");
						for (int i = 0; i < listOfIngredients.size(); i++)
						{
							writer.append(listOfIngredients.get(i).getName());
							writer.append(",");
							writer.append(String.valueOf(listOfIngredients.get(i).getAmount()));
							writer.append(",");
							writer.append(listOfIngredients.get(i).getUnit().getText());
							writer.append(",");
							writer.append(String.valueOf(listOfIngredients.get(i).getCalories()));
							writer.append(",");
						}
						writer.append("\n");
					}
					// writes from array for non-matching names
					else
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
				}
				writer.flush();
				writer.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
