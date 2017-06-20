package DamagePackets.Points;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Enums.locDamTypes;

public class unitDamagePoint implements Comparable<unitDamagePoint>
{
	private final locDamTypes flavor;
	private final double value;
	
	public unitDamagePoint(locDamTypes inFlavor, double inValue)
	{
		this.flavor = inFlavor;
		this.value = inValue;
	}

	public locDamTypes getFlavor()
	{
		return this.flavor;
	}

	public double getValue()
	{
		return this.value;
	}
	
	public CharList toCharList()
	{
		final CharList result = new CharList();
		
		result.add(this.flavor.toString());
		result.add('(');
		result.add(Double.toString(this.value));
		result.add(')');
		
		return result;
	}

	@Override
	public int compareTo(unitDamagePoint inArg0)
	{
		final int flavPrec = this.flavor.getPrecedence() - inArg0.flavor.getPrecedence();
		final int result;
		
		if(flavPrec != 0)
		{
			result = flavPrec;
		}
		else
		{
			final double delta = this.value - inArg0.value;
			
			if(delta > 0)
			{
				result = 1;
			}
			else if(delta < 0)
			{
				result = -1;
			}
			else
			{
				result = 0;
			}
		}
		
		return result;
	}
}
