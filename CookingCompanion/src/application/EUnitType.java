package application;

/**
 * The EUnitType class represents every measurement type you'd use while 
 * cooking. This file allows the choiceBox to have a list of units for 
 * selection. This is also easily editable if I think of more units!
 * 
 * @author Reed Olm - avr414 - UTSA CS 3443 - CookingCompanion 2021
 */
public enum EUnitType
{
	PINCH("PINCH"),
	TSP("TSP"),
	TBSP("TBSP"),
	OZ("OZ"),
	LB("LB"),
	CUP("CUP"),
	PINT("PINT"),
	QUART("QUART"),
	GALLON("GALLON"),
	GRAM("GRAM"),
	SMALL("SMALL"),
	MEDIUM("MEDIUM"),
	LARGE("LARGE");
	
	//data field 
	private String text;
	
	/**
	 * constructor: takes in a String representing a Unit Type
	 * and initializes it
	 * 
	 * @param text Text of the EUnitType (String)
	 */
	EUnitType(String text)
	{
		this.text = text;
	}
	
	/**
	 * Getter that turns an EUnitType to a String
	 * 
	 * @return String string representing a recipe
	 */
	public String getText()
	{
		return this.text;
	}
	
	/**
	 * Function that turns a String into an EUnitType
	 * 
	 * @param text the given text representation of an EUnitType (EUnitType)
	 * @return EUnitType the Type that matches the given String
	 */
	public static EUnitType fromString(String text)
	{
		for(EUnitType e : EUnitType.values())
		{
			if(e.text.equalsIgnoreCase(text))
			{
				return e;
			}
		}
		return null;
	}
	
	/**
	 * Overrides toString method to convert a type into a string
	 * 
	 * @return String string representing a recipe
	 */
	public String toString()
	{
		return(this.getText().toLowerCase());
	}
}
