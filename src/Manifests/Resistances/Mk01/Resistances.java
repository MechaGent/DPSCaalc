package Manifests.Resistances.Mk01;

import java.util.Map.Entry;

import Enums.locDamTypes;
import Enums.locMatTypes;
import HandyStuff.FileParser;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusFactory;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusFormat;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusVar;

public class Resistances
{
	private static double bypassValue = Double.NEGATIVE_INFINITY;
	private static double invulnValue = Double.POSITIVE_INFINITY;

	/**
	 * resistances[locDamType][locMatType]
	 */
	private static final double[][] resistances = initResistances();

	private static final double[][] initResistances()
	{
		final LotusObject in = helper_initResistances("H:/Users/Thrawnboo/workspace - Java/DPSCaalc/src/Manifests/DataFiles/ResistanceValues.txt");

		final double[][] result = new double[locDamTypes.getNumTypes()][locMatTypes.getNumTypes()];

		for (Entry<String, LotusFormat> DamEntry : in.getEntrySet())
		{
			final locDamTypes curDamType = locDamTypes.valueOf(DamEntry.getKey());
			final LotusObject contents = (LotusObject) DamEntry.getValue();

			for (Entry<String, LotusFormat> MatEntry : contents.getEntrySet())
			{
				final locMatTypes curMatType = locMatTypes.valueOf(MatEntry.getKey());
				final LotusVar reallyRawValue = (LotusVar) MatEntry.getValue();
				final String rawValue = reallyRawValue.getValueAsString();

				if (rawValue.equalsIgnoreCase("bypass"))
				{
					result[curDamType.ordinal()][curMatType.ordinal()] = bypassValue;
					// result[curDamType.ordinal()][curMatType.ordinal()] = Double.NEGATIVE_INFINITY;
					// System.out.println("triggered on: " + curDamType.toString() + " and " + curMatType.toString() + " with result: " + Double.toString(result[curDamType.ordinal()][curMatType.ordinal()]));
				}
				else
				{
					result[curDamType.ordinal()][curMatType.ordinal()] = Double.parseDouble(rawValue);
				}
			}
		}

		return result;
	}

	private static final LotusObject helper_initResistances(String fullPath)
	{
		final String rawLines = FileParser.parseFileAsString(fullPath);
		final LotusObject result = LotusFactory.createNewLotusObject(rawLines);
		return result;
	}

	public static boolean isBypassed(double in)
	{
		return in == bypassValue;
	}

	public static boolean isInvulnerable(double in)
	{
		return in == invulnValue;
	}

	public static double getResistance(locDamTypes damType, locMatTypes matType)
	{
		return resistances[damType.ordinal()][matType.ordinal()];
	}
	
	public static void main(String[] args)
	{
		test_ResistanceCalcs();
	}
	
	private static void test_ResistanceCalcs()
	{
		final double resist = getResistance(locDamTypes.Impact, locMatTypes.Cloned_Flesh);
		final double result = 15 * (1.0 + resist);
		
		System.out.println("result: " + result);
	}
}
