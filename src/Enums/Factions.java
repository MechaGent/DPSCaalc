package Enums;

import Parsers.LotusParser.aTopLayer.Interfaces.LotusVar;

public enum Factions
{
	Grineer, Corpus, Infested, Corrupted, Sentient, Tenno;
	
	public static Factions parse(LotusVar in)
	{
		return parse(in.getValueAsString());
	}
	
	public static Factions parse(String in)
	{
		final Factions result;
		
		switch(in.toLowerCase())
		{
			case "grineer":
			{
				result = Grineer;
				break;
			}
			case "corpus":
			{
				result = Corpus;
				break;
			}
			case "infested":
			{
				result = Infested;
				break;
			}
			case "corrupted":
			{
				result = Corrupted;
				break;
			}
			case "sentient":
			{
				result = Sentient;
				break;
			}
			case "tenno":
			{
				result = Tenno;
				break;
			}
			default:
			{
				throw new IllegalArgumentException("Unrecognized faction: " + in);
			}
		}
		
		return result;
	}
}
