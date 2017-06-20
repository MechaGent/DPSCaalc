package Enums;

import CustomExceptions.UnhandledEnum.Mk01.UnhandledEnumException;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusVar;

public enum TriggerTypes
{
	Automatic;
	
	public static TriggerTypes parse(LotusVar in)
	{
		return parse(in.getValueAsString());
	}
	
	public static TriggerTypes parse(String in)
	{
		final TriggerTypes result;
		
		switch(in.toLowerCase())
		{
			case "automatic":
			{
				result = Automatic;
				break;
			}
			default:
			{
				throw new UnhandledEnumException(in);
			}
		}
		
		return result;
	}
}
