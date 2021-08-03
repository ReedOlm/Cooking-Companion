package application;

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
	
	private String text;
	
	EUnitType(String text)
	{
		this.text = text;
	}
	
	public String getText()
	{
		return this.text;
	}
	
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
