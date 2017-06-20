package Manifests.Weapons.Mk01;

import Parsers.LotusParser.aTopLayer.Interfaces.LotusVar;

public enum ParsableWeaponTypes
{
	basicGun;
	
	public static ParsableWeaponTypes parse(LotusVar in)
	{
		return parse(in.getValueAsString());
	}
	
	public static ParsableWeaponTypes parse(String in)
	{
		final ParsableWeaponTypes result;
		
		switch(in.toLowerCase())
		{
			case "basicgun":
			{
				result = basicGun;
				break;
			}
			default:
			{
				throw new IllegalArgumentException("Unknown enum: " + in);
			}
		}
		
		return result;
	}
}
