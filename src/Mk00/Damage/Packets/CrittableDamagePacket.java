package Mk00.Damage.Packets;

import DataStructures.Linkages.CharList.Mk03.CharList;

public class CrittableDamagePacket extends ScaledDamagePacket
{
	private final int critLevel;
	private final double critMult;
	
	public CrittableDamagePacket(double[] inData, double inScalar, int inCritLevel, double inCritMult)
	{
		super(inData, inScalar);
		this.critLevel = inCritLevel;
		this.critMult = inCritMult;
	}
	
	public CrittableDamagePacket(double inScalar, int inCritLevel, double inCritMult)
	{
		super(inScalar);
		this.critLevel = inCritLevel;
		this.critMult = inCritMult;
	}

	public CrittableDamagePacket(ScaledDamagePacket old, int inCritLevel, double inCritMult)
	{
		super(old.data, old.Scalar);
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
	
	@Override
	public CrittableDamagePacket deepCloneSelf()
	{
		return new CrittableDamagePacket(super.deepCloneSelf(), this.critLevel, this.critMult);
	}
	
	@Override
	public CharList toCharList()
	{
		final CharList result = super.toCharList();
		
		result.push(')');
		result.push(Double.toString(this.critMult));
		result.push(" CritMult(");
		result.push(')');
		result.push(Integer.toString(this.critLevel));
		result.push("CritLevel(");
		
		return result;
	}
}
