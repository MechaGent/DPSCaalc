package Enums;

import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusVar;

public enum locMatTypes
{
	Flesh(
			true,
			false,
			false),
	Cloned_Flesh(
			true,
			false,
			false),
	Fossilized(
			true,
			false,
			false),
	Infested(
			true,
			false,
			false),
	Infested_Flesh(
			true,
			false,
			false),
	Sinew(
			true,
			false,
			false),
	Machinery(
			true,
			false,
			false),
	Robotic(
			true,
			false,
			false),
	Object(
			true,
			true,
			true),
	Shield(
			false,
			false,
			true),
	Proto_Shield(
			false,
			false,
			true),
	Ferrite_Armor(
			false,
			true,
			false),
	Alloy_Armor(
			false,
			true,
			false);

	private static final locMatTypes[] All = locMatTypes.values();

	private final boolean isValidHealthType;
	private final boolean isValidArmorType;
	private final boolean isValidShieldType;

	private locMatTypes(boolean inIsValidHealthType, boolean inIsValidArmorType, boolean inIsValidShieldType)
	{
		this.isValidHealthType = inIsValidHealthType;
		this.isValidArmorType = inIsValidArmorType;
		this.isValidShieldType = inIsValidShieldType;
	}
	
	public static locMatTypes parseVar(LotusObject in, String varName)
	{
		return parse(in.getVarAsVar(varName));
	}
	
	public static locMatTypes parse(LotusVar in)
	{
		return parse(in.getValueAsString());
	}

	public static locMatTypes parse(String in)
	{
		final locMatTypes result;

		switch (in)
		{
			case "Flesh":
			{
				result = Flesh;
				break;
			}
			case "Cloned_Flesh":
			{
				result = Cloned_Flesh;
				break;
			}
			case "Fossilized":
			{
				result = Fossilized;
				break;
			}
			case "Infested":
			{
				result = Infested;
				break;
			}
			case "Infested_Flesh":
			{
				result = Infested_Flesh;
				break;
			}
			case "Sinew":
			{
				result = Sinew;
				break;
			}
			case "Machinery":
			{
				result = Machinery;
				break;
			}
			case "Robotic":
			{
				result = Robotic;
				break;
			}
			case "object":
			case "Object":
			{
				result = Object;
				break;
			}
			case "Shield":
			{
				result = Shield;
				break;
			}
			case "Proto_Shield":
			{
				result = Proto_Shield;
				break;
			}
			case "Ferrite_Armor":
			{
				result = Ferrite_Armor;
				break;
			}
			case "Alloy_Armor":
			{
				result = Alloy_Armor;
				break;
			}
			default:
			{
				throw new IllegalArgumentException("Unrecognized matType: " + in);
			}
		}

		return result;
	}

	public boolean isValidHealthType()
	{
		return this.isValidHealthType;
	}

	public boolean isValidArmorType()
	{
		return this.isValidArmorType;
	}

	public boolean isValidShieldType()
	{
		return this.isValidShieldType;
	}

	public static final int getNumTypes()
	{
		return All.length;
	}

	public static locMatTypes[] getAll()
	{
		return All;
	}
}
