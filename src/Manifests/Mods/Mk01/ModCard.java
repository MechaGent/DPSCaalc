package Manifests.Mods.Mk01;

import Enums.ModEffectTargets;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusArray;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;

public class ModCard
{
	private final String name;
	protected final int maxRank;
	private final ModEffect[] baseEffects;

	public ModCard(LotusObject in)
	{
		this(in.getVarAsVar("name").getValueAsString(),

				in.getVarAsVar("maxRank").getValueAsInt(),

				parseEffects(in.getVarAsArray("modEffects")));
	}

	public ModCard(String inName, int inMaxRank, ModEffect[] inBaseEffects)
	{
		this.name = inName;
		this.maxRank = inMaxRank;
		this.baseEffects = inBaseEffects;
	}

	private static ModEffect[] parseEffects(LotusArray in)
	{
		final ModEffect[] result = new ModEffect[in.getLength()];

		for (int i = 0; i < result.length; i++)
		{
			result[i] = new ModEffect(in.getVarAsObject(i));
		}

		return result;
	}

	public String getName()
	{
		return this.name;
	}

	public int getMaxRank()
	{
		return this.maxRank;
	}

	public ModEffect[] getBaseEffects()
	{
		return this.baseEffects;
	}

	public static class ModEffect
	{
		private final ModEffectTargets target;
		private final double value;
		private final double delta;

		public ModEffect(LotusObject in)
		{
			this(ModEffectTargets.parse(in.getVarAsVar("target").getValueAsString()),

					in.getVarAsVar("value").getValueAsDouble(),

					in.getVarAsVar("delta").getValueAsDouble());
		}

		public ModEffect(ModEffectTargets inTarget, double inValue, double inDelta)
		{
			this.target = inTarget;
			this.value = inValue;
			this.delta = inDelta;
		}

		public ModEffectTargets getTarget()
		{
			return this.target;
		}

		public double getRankedValue(int rank)
		{
			return this.value + (this.delta * rank);
		}
	}
}
