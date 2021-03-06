package application;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * CardModel is a Java class representing a RecipeObject with a name, number of
 * servings number of total calories, list of ingredients, and list of tags.
 * Contains methods for getting and setting, as well as
 *
 * @author Reed Olm - avr414 - UTSA CS 3443 - CookingCompanion 2021
 */
public class Recipe
{
	private String name;
	private int servings;
	private int calories;
	private int caloriesPerServing;
	private ArrayList<Ingredient> ingredients;
	private ArrayList<String> tags;
	private ArrayList<String> prep;
	//Wrappers that allow TableView to display Recipes
	private final StringProperty nameP = new SimpleStringProperty();
	private final StringProperty calsP = new SimpleStringProperty();

	/**
	 * constructor: 5-arg constructor containing name, servings, total calories,
	 * list of ingredients, and list of tags
	 * 
	 * @param name        Name of Recipe (String)
	 * @param servings    Number of servings (int)
	 * @param calories    Number of calories (int)
	 * @param ingredients List of all ingredients (ArrayList<Ingredient>)
	 * @param tags        List of all tags (ArrayList<Sring>)
	 * @param prep        Instructions on preparation of dish (String)
	 */
	public Recipe(String name, int servings, int calories, ArrayList<Ingredient> ingredients, ArrayList<String> tags, ArrayList<String> prep)
	{
		this.name = name;
		this.servings = servings;
		this.calories = calories;
		this.ingredients = ingredients;
		this.tags = tags;
		this.prep = prep;
		this.setNameP(name);
		this.setCalsP(String.valueOf(calories / servings));
		this.caloriesPerServing = calories/servings;
	}

	/**
	 * Gets the name of the Recipe
	 * 
	 * @return String name of this Recipe
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Sets the name of the Recipe
	 * 
	 * @param name Name to set this Recipe (String)
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the number of servings of the Recipe
	 * 
	 * @return int Number of servings in this Recipe
	 */
	public int getServings()
	{
		return this.servings;
	}

	/**
	 * Sets the number of servings in the Recipe
	 * 
	 * @param servings Number of servings to set in this Recipe (int)
	 */
	public void setServings(int servings)
	{
		this.servings = servings;
	}

	/**
	 * Gets the number of calories of the Recipe
	 * 
	 * @return int Number of calories in this Recipe
	 */
	public int getCalories()
	{
		return this.calories;
	}

	/**
	 * Sets the number of calories in the Recipe
	 * 
	 * @param calories Number of calories to set in this Recipe (int)
	 */
	public void setCalories(int calories)
	{
		this.calories = calories;
	}
	
	/**
	 * Gets the number of calories per serving
	 * 
	 * @return int Number of calories per serving
	 */
	public int getCaloriesPerServing()
	{
		return this.caloriesPerServing;
	}
	
	/**
	 * sets the number of calories per serving
	 * 
	 * @param calories Number of calories in recipe (int)
	 * @param servings Number of servings in recipe (int)
	 */
	public void setCaloriesPerServing(int calories, int servings)
	{
		this.caloriesPerServing = calories/servings;
	}

	/**
	 * Gets the ArrayList of Ingredients
	 * 
	 * @return ArrayList ingredients of Ingredients
	 */
	public ArrayList<Ingredient> getIngredients()
	{
		return this.ingredients;
	}

	/**
	 * Sets the ArrayList of Ingredients
	 * 
	 * @param ingredients Ingredients to set (ArrayList<Ingredient>)
	 */
	public void setIngredients(ArrayList<Ingredient> ingredients)
	{
		this.ingredients = ingredients;
	}

	/**
	 * Gets the ArrayList of tags
	 * 
	 * @return ArrayList tags of Tags
	 */
	public ArrayList<String> getTags()
	{
		return this.tags;
	}

	/**
	 * Sets the ArrayList of tags
	 * 
	 * @param tags Tags to set (ArrayList<String>)
	 */
	public void setTags(ArrayList<String> tags)
	{
		this.tags = tags;
	}

	/**
	 * Gets the prep instructions of the Recipe
	 * 
	 * @return ArrayList instructions of this Recipe
	 */
	public ArrayList<String> getPrep()
	{
		return this.prep;
	}

	/**
	 * Sets the prep of the Recipe
	 * 
	 * @param prep Prep to set this Recipe (ArrayList<String>)
	 */
	public void setPrep(ArrayList<String> prep)
	{
		this.prep = prep;
	}

	// JavaFX Wrappers for name for using table views!
	public StringProperty nameProperty()
	{
		return nameP;
	}

	public final String getNameP()
	{
		return nameProperty().get();
	}

	public final void setNameP(String name)
	{
		nameProperty().set(name);
	}

	// JavaFX Wrappers for calories for using table views!
	public StringProperty calsProperty()
	{
		return calsP;
	}

	public final String getCalsP()
	{
		return calsProperty().get();
	}

	public final void setCalsP(String cals)
	{
		calsProperty().set(cals);
	}
}
