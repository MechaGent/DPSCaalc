package Mk00.Damage.Packets;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Enums.locDamTypes;

public class ScaledDamagePacket extends UnitDamagePacket
{
	protected final double Scalar;

	public ScaledDamagePacket(double inScalar)
	{
		super();
		this.Scalar = inScalar;
	}
	
	public ScaledDamagePacket(double[] data, double inScalar)
	{
		super(data);
		this.Scalar = inScalar;
	}
	
	public ScaledDamagePacket(UnitDamagePacket in, double inScalar)
	{
		this(in.data, inScalar);
	}
	
	@Override
	public double getScalar()
	{
		return this.Scalar;
	}
	
	@Override
	public double getScaledDamage(locDamTypes type)
	{
		return this.data[type.ordinal()] * this.Scalar;
	}
	
	@Override
	public ScaledDamagePacket deepCloneSelf()
	{
		return new ScaledDamagePacket(super.deepCloneSelf(), this.Scalar);
	}
	
	@Override
	public CharList toCharList()
	{
		final CharList result = super.toCharList();
		
		result.push(" {");
		result.add('}');
		
		result.push(Double.toString(this.Scalar));
		
		return result;
	}
}
