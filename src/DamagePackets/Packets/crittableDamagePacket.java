package DamagePackets.Packets;

import DamagePackets.Points.unitDamagePoint;
import DataStructures.Linkages.CharList.Mk03.CharList;

public class crittableDamagePacket extends scaledDamagePacket
{
	private static crittableDamagePacket emptyPacket = new crittableDamagePacket(0.0, 0, 0.0);
	
	private final int critLevel;
	private final double critMult;
	
	public crittableDamagePacket(double inScalar, int inCritLevel, double inCritMult, unitDamagePoint... inProps)
	{
		super(inScalar, inProps);
		this.critLevel = inCritLevel;
		this.critMult = inCritMult;
	}
	
	public crittableDamagePacket(scaledDamagePacket old, int inCritLevel, double inCritMult)
	{
		super(old);
		this.critLevel = inCritLevel;
		this.critMult = inCritMult;
	}

	public int getCritLevel()
	{
		return this.critLevel;
	}

	public double getCritMult()
	{
		return this.critMult;
	}
	
	
	public CharList toCharList_orig()
	{
		final CharList result = super.toCharList();
		
		result.push(")\t");
		result.push(Double.toString(this.critMult));
		result.push(")\tcritMult(");
		result.push(Integer.toString(this.critLevel));
		result.push("critLevel(");
		
		return result;
	}
	
	@Override
	public CharList toCharList()
	{
		return this.toCharList("\t");
	}
	
	@Override
	public CharList toCharList(String delim)
	{
		return this.toCharList(new CharList(), delim);
	}
	
	@Override
	public CharList toCharList(CharList prior, String delim)
	{
		final CharList result = super.toCharList(prior, delim);
		
		result.push(delim);
		result.push(')');
		result.pushAsString(this.critMult);
		result.push("CritMult(");
		result.push(delim);
		result.push(')');
		result.pushAsString(this.critLevel);
		result.push("CritLevel(");
		
		return result;
	}

	public static crittableDamagePacket getEmptyPacket()
	{
		return emptyPacket;
	}
}
