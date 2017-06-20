package Mk00.Damage.Packets;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Enums.locDamTypes;

public class UnitDamagePacket
{
	protected final double[] data;
	private double summedPortions;
	private boolean hasSummedPortions;

	public UnitDamagePacket()
	{
		this(new double[locDamTypes.getNumTypes()]);
	}

	public UnitDamagePacket(double[] inData)
	{
		this.data = inData;
		this.summedPortions = 0;
		this.hasSummedPortions = false;
	}

	/**
	 * 
	 * @param type
	 * @return raw portion of the damage
	 */
	public double getDamagePortion(locDamTypes type)
	{
		return this.data[type.ordinal()];
	}
	
	public void setDamagePortion(locDamTypes type, double value)
	{
		this.data[type.ordinal()] = value;
	}

	/**
	 * 
	 * @param type
	 * @return this.getDamagePortion(type) * internal scalar (or 1 if none present)
	 */
	public double getScaledDamage(locDamTypes type)
	{
		return this.getDamagePortion(type);
	}

	public double getSummedDamagePortions()
	{
		if (!this.hasSummedPortions)
		{
			this.summedPortions = this.sumDamagePortions();
			this.hasSummedPortions = true;
		}

		return this.summedPortions;
	}

	private double sumDamagePortions()
	{
		double result = 0;

		for (int i = 0; i < this.data.length; i++)
		{
			result += data[i];
		}

		return result;
	}

	public double getScalar()
	{
		return 1.0;
	}

	/**
	 * 
	 * @return sum of all damage
	 */
	public double getTotalDamage()
	{
		return this.getSummedDamagePortions() * this.getScalar();
	}
	
	public UnitDamagePacket deepCloneSelf()
	{
		return deepClone(this);
	}
	
	public CharList toCharList()
	{
		final CharList result = new CharList();
		boolean hasFirst = false;
		
		for(locDamTypes curType: locDamTypes.getAll())
		{
			final double cur = this.data[curType.ordinal()];
			
			if(cur != 0)
			{
				if(hasFirst)
				{
					result.add('\t');
				}
				else
				{
					hasFirst = true;
				}
				
				result.add(curType.toString());
				result.add('(');
				result.add(Double.toString(cur));
				result.add(')');
			}
		}
		
		return result;
	}
	
	public static UnitDamagePacket deepClone(UnitDamagePacket in)
	{
		final UnitDamagePacket result = new UnitDamagePacket();
		
		for(int i = 0; i < result.data.length; i++)
		{
			final double cur = in.data[i];
			
			if(cur != 0)
			{
				result.data[i] = cur;
			}
		}
		
		return result;
	}
}
