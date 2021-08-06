package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Ingredient is a Java class representing an Ingredient object with a name,
 * amount, unit of measurement, and number of calories. Contains methods for
 * getting and setting all data fields.
 *
 * @author Reed Olm - avr414 - UTSA CS 3443 - CookingCompanion 2021
 */
public class Ingredient
{
	private String name;
	private double amount;
	private EUnitType unit;
	private int calories;
	//These allow the TableView lambda to display Ingredients on the table
	private final StringProperty nameP = new SimpleStringProperty();
	private final StringProperty amountP = new SimpleStringProperty();
	private final StringProperty unitP = new SimpleStringProperty();
	private final StringProperty caloriesP = new SimpleStringProperty();

	/**
	 * constructor: 4-arg constructor, taking in a name, an amount, a unit, and
	 * calories
	 * 
	 * @param name     Name of Ingredient (String)
	 * @param amount   Amount of Ingredient (double)
	 * @param unit     Unit of measurement (EUnitType)
	 * @param calories Number of calories in Ingredient (int)
	 */
	public Ingredient(String name, double amount, EUnitType unit, int calories)
	{
		this.name = name;
		this.amount = amount;
		this.unit = unit;
		this.calories = calories;
		this.setNameP(name);
		this.setAmountP(amount);
		this.setUnitP(unit);
		this.setCaloriesP(calories);
	}

	/**
	 * constructor: 3-arg constructor, taking in a name, an amount, and unit
	 * 
	 * @param name   Name of Ingredient (String)
	 * @param amount Amount of Ingredient (double)
	 * @param unit   Unit of measurement (EUnitType)
	 */
	public Ingredient(String name, double amount, EUnitType unit)
	{
		this.name = name;
		this.amount = amount;
		this.unit = unit;
		this.calories = 0;
	}

	/**
	 * constructor: no-arg constructor defaults to nothing
	 */
	public Ingredient(int swag)
	{
		this.name = null;
		this.amount = 0;
		this.unit = null;
		this.calories = 0;
	}

	/**
	 * Gets the name of the Ingredient
	 * 
	 * @return String name of this Ingredient
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Sets the name of the Ingredient
	 * 
	 * @param name Name to set this Ingredient (String)
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the amount of the Ingredient
	 * 
	 * @return double Amount of this Ingredient
	 */
	public double getAmount()
	{
		return this.amount;
	}

	/**
	 * Sets the amount of the Ingredient
	 * 
	 * @param amount Amount to set this Ingredient (double)
	 */
	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	/**
	 * Gets the unit of the Ingredient
	 * 
	 * @return EUnitType unit of this Ingredient
	 */
	public EUnitType getUnit()
	{
		return this.unit;
	}

	/**
	 * Sets the unit of the Ingredient
	 * 
	 * @param unit Unit to set this Ingredient (EUnitType)
	 */
	public void setUnit(EUnitType unit)
	{
		this.unit = unit;
	}

	/**
	 * Gets the calories of the Ingredient
	 * 
	 * @return int Calorie count of this Ingredient
	 */
	public int getCalories()
	{
		return this.calories;
	}

	/**
	 * Sets the calorie count of the Ingredient
	 * 
	 * @param calories Calories in Ingredient (int)
	 */
	public void setCalories(int calories)
	{
		this.calories = calories;
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

	// JavaFX Wrappers for amount for using table views!
	public StringProperty amountProperty()
	{
		return amountP;
	}

	public final String getAmountP()
	{
		return amountProperty().get();
	}

	public final void setAmountP(double amount)
	{
		amountProperty().set(String.valueOf(amount));
	}

	// JavaFX Wrappers for unit for using table views!
	public StringProperty unitProperty()
	{
		return unitP;
	}

	public final String getUnitP()
	{
		return unitProperty().get();
	}

	public final void setUnitP(EUnitType unit)
	{
		unitProperty().set(unit.toString());
	}

	// JavaFX Wrappers for calories for using table views!
	public StringProperty caloriesProperty()
	{
		return caloriesP;
	}

	public final String getCaloriesP()
	{
		return caloriesProperty().get();
	}

	public final void setCaloriesP(int calories)
	{
		caloriesProperty().set(String.valueOf(calories));
	}
}
