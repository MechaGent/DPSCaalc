package DamagePackets.Packets;

import DamagePackets.Points.unitDamagePoint;
import DataStructures.Linkages.CharList.Mk03.CharList;
import Enums.locDamTypes;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusVar;

public class scaledDamagePacket extends unitDamagePacket
{
	private final double Scalar;
	
	public scaledDamagePacket(LotusObject in)
	{
		super(in);
		
		final LotusVar wrapScalar = in.getVarAsVar("scalar");

		if (wrapScalar != null)
		{
			this.Scalar = wrapScalar.getValueAsDouble();
		}
		else
		{
			this.Scalar = getSum(this.props);
			
			for(int i = 0; i < this.props.length; i++)
			{
				final unitDamagePoint old = this.props[i];
				this.props[i] = new unitDamagePoint(old.getFlavor(), old.getValue() / this.Scalar);
			}
		}
	}
	
	private static double getSum(unitDamagePoint[] points)
	{
		double result = 0;

		for (unitDamagePoint point : points)
		{
			result += point.getValue();
		}
		
		return result;
	}
	
	public scaledDamagePacket(double inScalar, unitDamagePoint... inProps)
	{
		super(inProps);
		this.Scalar = inScalar;
	}
	
	public scaledDamagePacket(scaledDamagePacket old)
	{
		super(old);
		this.Scalar = old.Scalar;
	}
	
	public double getScalar()
	{
		return this.Scalar;
	}
	
	public double getScaledSum()
	{
		double result = 0;
		
		for(int i = 0; i < this.props.length; i++)
		{
			result += this.props[i].getValue();
		}
		
		return result * this.Scalar;
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
	public CharList toCharList(CharList result, String delim)
	{
		result.add("Scalar(");
		result.addAsString(this.Scalar);
		result.add(')');
		result.add(delim);
		super.toCharList(result, delim);

		return result;
	}
	
	public static class scaledDamagePacketBuilder extends unitDamagePacketBuilder
	{
		private double scalar;

		public scaledDamagePacketBuilder()
		{
			super();
			this.scalar = 0;
		}
		
		public scaledDamagePacketBuilder(scaledDamagePacket old)
		{
			super(old);
			this.scalar = old.Scalar;
		}
		
		public void addToScalar(double in)
		{
			this.scalar += in;
		}
		
		@Override
		public scaledDamagePacket build()
		{
			return new scaledDamagePacket(this.scalar, super.buildProps());
		}
		
		@Override
		public scaledDamagePacket build(locDamTypes[] elemOrder, int numElems)
		{
			this.redistributeCombinations(elemOrder, numElems);
			return this.build();
		}
	}
}
