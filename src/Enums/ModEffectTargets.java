package Enums;

import CustomExceptions.UnhandledEnum.Mk01.UnhandledEnumException;

/**
 * Use prefixes to denote to which subset each effect belongs
 * 
 * @author MechaGent
 *
 */
public enum ModEffectTargets
{
	BaseDamage,
	ModdedDamage,
	elemMult_Cold,
	elemMult_Electricity,
	elemMult_Heat,
	elemMult_Toxin,
	fireRate,
	reloadTime;

	private static ModEffectTargets[] All = ModEffectTargets.values();

	public static ModEffectTargets parse(String in)
	{
		final ModEffectTargets result;

		switch (in.toLowerCase())
		{
			case "basedamage":
			{
				result = BaseDamage;
				break;
			}
			case "moddeddamage":
			{
				result = ModdedDamage;
				break;
			}
			case "elemmult_cold":
			{
				result = elemMult_Cold;
				break;
			}
			case "elemmult_electricity":
			{
				result = elemMult_Electricity;
				break;
			}
			case "elemmult_heat":
			{
				result = elemMult_Heat;
				break;
			}
			case "elemmult_toxin":
			{
				result = elemMult_Toxin;
				break;
			}
			case "firerate":
			{
				result = fireRate;
				break;
			}
			case "reloadtime":
			{
				result = reloadTime;
				break;
			}
			default:
			{
				throw new UnhandledEnumException(in);
			}
		}

		return result;
	}

	public static ModEffectTargets[] getAll()
	{
		return All;
	}

	public static int getNumTypes()
	{
		return All.length;
	}

	private static void updateParseMethod()
	{
		for (ModEffectTargets target : All)
		{
			System.out.println("case \"" + target.toString().toLowerCase() + "\": { result = " + target.toString() + "; break; }");
		}
	}

	public static void main(String[] args)
	{
		updateParseMethod();
	}
}
