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
 * The EditorController class, represents the Edit existing recipe view, and
 * connects UI with back end and implements EventHandler to get action events
 * from JavaFX and Initializable to switch scenes and update certain models.
 * 
 * @author Reed Olm - avr414 - UTSA CS 3443 - CookingCompanion 2021
 */
public class EditorController implements Initializable, EventHandler<ActionEvent>
{
	ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	ArrayList<Ingredient> curIngList = new ArrayList<Ingredient>();

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

	@FXML
	public void handle(ActionEvent event)
	{
		// Exit button
		if (event.getSource() == exitWithoutSaving)
		{
			Stage appStage;
			Parent root;
			appStage = (Stage) exitWithoutSaving.getScene().getWindow();
			try
			{
				root = FXMLLoader.load(getClass().getResource("Home.fxml"));
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

		// Save button
		else if (event.getSource() == save)
		{
			if (recipeName.getText().contains(","))
			{
				ingError.setText("Please don't use commas.");
			} else
			{
				try
				{
					// Catches non int values in field
					@SuppressWarnings("unused")
					int servingTest = Integer.valueOf(totalServings.getText());
					// Save stuff
					saveData(recipeName.getText());
				} catch (NumberFormatException e)
				{
					ingError.setText("Error: Please only use numbers in the serving size");
				} catch (IOException e)
				{
					e.printStackTrace();
				}

			}
		}

		// AddInstruction
		else if (event.getSource() == addInstruction)
		{
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

		// RemoveInstruction
		else if (event.getSource() == removeInstruction)
		{
			ObservableList<String> list = instructions.getItems();
			if (list.contains(enterInstruction.getText()))
			{
				list.remove(enterInstruction.getText());
			}
			enterInstruction.clear();
		}

		// Add Tag
		else if (event.getSource() == addTag)
		{
			if (enterTag.getText() != "")
			{
				String entered = enterTag.getText();
				tagList.getItems().addAll(entered);
				enterTag.clear();
			}
		}

		// Add Tag
		else if (event.getSource() == addTag)
		{
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

		// Add Ingredient
		else if (event.getSource() == addIngredient)
		{
			ObservableList<Ingredient> list = ingredientList.getItems();
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

		// Remove Ingredient
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

	@FXML
	private void putSelectedListItemInBox(MouseEvent event)
	{
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		ingError.setText("");
		unitSelection.getItems().setAll(EUnitType.values());

		instructions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tagList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		ingredientList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// Sets the table to properly place Ingredient info into the correct columns
		colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		colAmount.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
		colUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
		colCalories.setCellValueFactory(cellData -> cellData.getValue().caloriesProperty());

		caloriesTotal.setText("0");
		try
		{
			readData();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		String passName = null;

		try
		{
			passName = readPass();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		// Filling recipe data with matching name recipe from list
		for (int i = 0; i < recipes.size(); i++)
		{
			if (recipes.get(i).getName().equals(passName))
			{
				Recipe cur = recipes.get(i);

				// Filling ingredient list
				ObservableList<Ingredient> list = ingredientList.getItems();
				for (int j = 0; j < cur.getIngredients().size(); j++)
				{
					list.add(cur.getIngredients().get(j));
				}
				ingredientList.setItems(list);

				// Filling calories
				int sum = 0;
				for (int j = 0; j < list.size(); j++)
				{
					sum += list.get(j).getCalories();
				}
				caloriesTotal.setText(String.valueOf(sum));

				// Filling servings and recipe name
				totalServings.setText(String.valueOf(cur.getServings()));
				recipeName.setText(cur.getName());

				// Filling tags
				ObservableList<String> tags = tagList.getItems();
				for (int j = 0; j < cur.getTags().size(); j++)
				{
					tags.add(cur.getTags().get(j));
				}

				// Filling steps
				ObservableList<String> steps = instructions.getItems();
				for (int j = 0; j < cur.getPrep().size(); j++)
				{
					steps.add(cur.getPrep().get(j));
				}
			}
		}

	}

	// This reads from the pass data
	private String readPass() throws IOException
	{
		String pass = null;
		String row;
		BufferedReader passReader = new BufferedReader(new FileReader("src/application/data/pass.csv"));
		while ((row = passReader.readLine()) != null)
		{
			String[] data = row.split(",");
			pass = data[0];
		}
		passReader.close();

		return pass;
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
			recipes.add(recipe);
		}
		csvReader.close();
	}

	// This puts the recipe arraylist into the listview
	private void saveData(String overwrite) throws IOException
	{
		int oIndex = -1;
		for (int i = 0; i < recipes.size(); i++)
		{
			if (recipes.get(i).getName().equalsIgnoreCase(overwrite))
				oIndex = i;
		}
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
		} else
		{
			FileWriter writer;
			try
			{
				writer = new FileWriter("src/application/data/recipes.csv", false);
				for (int x = 0; x < recipes.size(); x++)
				{
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
					} else
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
