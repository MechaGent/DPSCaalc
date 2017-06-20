package Manifests.Weapons.Mk01;

import Manifests.Weapons.Mk01.Stats.basicGun;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusFormat;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;

public abstract class WeaponStats
{
	private final String name;
	private final String type;
	private final String Trigger;
	
	public WeaponStats(String inName, LotusObject in)
	{
		this.name = inName;
		this.type = in.getVarAsVar("type").getValueAsString();
		this.Trigger = in.getVarAsVar("Trigger").getValueAsString();
	}
	
	public String getName()
	{
		return this.name;
	}

	public String getType()
	{
		return this.type;
	}

	public String getTrigger()
	{
		return this.Trigger;
	}

	public static WeaponStats parseWeapon(String inName, LotusFormat in)
	{
		return parseWeapon(inName, (LotusObject) in);
	}
	
	public static WeaponStats parseWeapon(String inName, LotusObject in)
	{
		final ParsableWeaponTypes parseType = ParsableWeaponTypes.parse(in.getVarAsVar("parseType"));
		final WeaponStats result;
		
		switch(parseType)
		{
			case basicGun:
			{
				result = new basicGun(inName, in);
				break;
			}
			default:
			{
				throw new IllegalArgumentException(parseType.toString());
			}
		}
		
		return result;
	}
}
