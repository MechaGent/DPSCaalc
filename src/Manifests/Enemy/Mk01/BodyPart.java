package Manifests.Enemy.Mk01;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusArray;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusVar;
import Random.XorShiftStar.Mk02.XorShiftStar;

public class BodyPart
{
	private static BodyPart standardTorso = new BodyPart("StandardTorso", false, 1.0, 0.4);
	private static BodyPart standardHead = new BodyPart("StandardHead", true, 2.0, 0.10);
	private static BodyPart missedPart = new BodyPart("missedPart", false, 0.0, -1.0);

	private final String name;
	private final boolean isHead;
	private final double multiplier;
	private final double RelativeSurfaceArea;

	public BodyPart(String inName, boolean inIsHead, double inMultiplier, double inRelativeSurfaceArea)
	{
		this.name = inName;
		this.isHead = inIsHead;
		this.multiplier = inMultiplier;
		this.RelativeSurfaceArea = inRelativeSurfaceArea;
	}

	public String getName()
	{
		return this.name;
	}

	public boolean isHead()
	{
		return this.isHead;
	}
	
	public boolean isMissedPart()
	{
		return this == missedPart;
	}

	public double getMultiplier()
	{
		return this.multiplier;
	}

	public double getRelativeSurfaceArea()
	{
		return this.RelativeSurfaceArea;
	}
	
	public CharList toCharList()
	{
		final CharList result = new CharList();
		
		result.add("name(");
		result.add(this.name);
		result.add(')');
		
		result.add("\tisHead(");
		result.add(Boolean.toString(this.isHead));
		result.add(')');
		
		result.add("\tmult(");
		result.add(Double.toString(this.multiplier));
		result.add(')');
		
		result.add("\tRelSurfArea(");
		result.add(Double.toString(this.RelativeSurfaceArea));
		result.add(')');
		
		return result;
	}

	public static BodyPart getStandardTorso()
	{
		return standardTorso;
	}
	
	public static BodyPart getStandardHead()
	{
		return standardHead;
	}
	
	public static BodyPart getMissedPart()
	{
		return missedPart;
	}

	public static BodyPart[] getStandardHumanoidDummy()
	{
		final BodyPart[] result = new BodyPart[6];
		result[0] = new BodyPart("Head", true, 2.0, 0.10);
		result[1] = new BodyPart("Chest", false, 1.0, 0.40);
		result[2] = new BodyPart("Left Arm", false, 1.0, 0.10);
		result[3] = new BodyPart("Right Arm", false, 1.0, 0.10);
		result[4] = new BodyPart("Left Leg", false, 1.0, 0.15);
		result[5] = new BodyPart("Right Leg", false, 1.0, 0.15);

		return result;
	}
	
	public static BodyPart[] parse(LotusArray in)
	{
		return parse(in, "PartName", "isHeadPart", "Multiplier", "RelativeSurfaceArea");
	}
	
	public static BodyPart[] parse(LotusArray in, String nameKey, String isHeadKey, String multKey, String areaKey)
	{
		final BodyPart[] result = new BodyPart[in.getLength()];

		for (int i = 0; i < result.length; i++)
		{
			final LotusObject curr = in.getVarAsObject(i);
			final String partName = curr.getVarAsVar(nameKey).getValueAsString();
			final boolean isHeadPart = curr.getVarAsVar(isHeadKey).getValueAsBoolean();
			final double mult = curr.getVarAsVar(multKey).getValueAsDouble();
			
			final LotusVar wrapArea = curr.getVarAsVar(areaKey);
			final double relSurfArea;
			
			if(wrapArea != null)
			{
				relSurfArea = curr.getVarAsVar(areaKey).getValueAsDouble();
			}
			else
			{
				relSurfArea = 1;
			}

			result[i] = new BodyPart(partName, isHeadPart, mult, relSurfArea);
		}

		return result;
	}

	public static interface AccuracyMapper
	{
		public abstract BodyPart partHit(XorShiftStar in);

		public static AccuracyMapper getInstance(BodyPart in)
		{
			return new FlatAccuracyMapper(in);
		}

		public static AccuracyMapper getInstance(BodyPart[] in)
		{
			return new FullBodyAccuracyMapper(in);
		}
	}

	private static class FlatAccuracyMapper implements AccuracyMapper
	{
		private final BodyPart targetPart;

		public FlatAccuracyMapper(BodyPart in)
		{
			this.targetPart = in;
		}

		@Override
		public BodyPart partHit(XorShiftStar inIn)
		{
			return this.targetPart;
		}
	}

	private static class FullBodyAccuracyMapper implements AccuracyMapper
	{
		private final BodyPart[] body;

		public FullBodyAccuracyMapper(BodyPart[] inBody)
		{
			this.body = inBody;
		}

		@Override
		public BodyPart partHit(XorShiftStar in)
		{
			if (this.body.length > 0)
			{
				double dice = in.nextDouble(1);
				int i = 0;

				while (this.body[i].RelativeSurfaceArea < dice)
				{
					dice -= this.body[i].RelativeSurfaceArea;
					i++;
				}
				
				return this.body[i];
			}
			else
			{
				return null;
			}
		}
	}
}
