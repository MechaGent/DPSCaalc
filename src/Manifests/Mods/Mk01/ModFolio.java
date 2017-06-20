package Manifests.Mods.Mk01;

import java.util.EnumMap;
import java.util.Iterator;

import Enums.ModEffectTargets;
import Manifests.Mods.Mk01.ModCard.ModEffect;

public class ModFolio implements Iterable<RankedModCard>
{
	private static final ModFolio nullFolio = new ModFolio(new RankedModCard[0]);
	
	private final RankedModCard[] Mods;
	private final EnumMap<ModEffectTargets, Double> rankedValues;

	public ModFolio(RankedModCard[] inMods)
	{
		this.Mods = inMods;
		this.rankedValues = initMap(inMods);
	}
	
	private static EnumMap<ModEffectTargets, Double> initMap(RankedModCard[] in)
	{
		final EnumMap<ModEffectTargets, Double> result = initBlankMap();
		
		for(RankedModCard card: in)
		{
			final int rank = card.getMaxRank();
			
			for(ModEffect effect: card.getBaseEffects())
			{
				final ModEffectTargets curTarget = effect.getTarget();
				final double value = effect.getRankedValue(rank);
				final Double old = result.get(curTarget);
				
				result.put(curTarget, value + old);
			}
		}
		
		return result;
	}
	
	private static EnumMap<ModEffectTargets, Double> initBlankMap()
	{
		final EnumMap<ModEffectTargets, Double> result = new EnumMap<ModEffectTargets, Double>(ModEffectTargets.class);
		
		for(ModEffectTargets target: ModEffectTargets.getAll())
		{
			result.put(target, 0.0);
		}
		
		return result;
	}
	
	public boolean isNullFolio()
	{
		return this == nullFolio;
	}
	
	public int length()
	{
		return this.Mods.length;
	}
	
	public RankedModCard getAt(int index)
	{
		return this.Mods[index];
	}

	@Override
	public Iterator<RankedModCard> iterator()
	{
		return new locIterator(this);
	}
	
	public double getEffectMagnitude(ModEffectTargets in)
	{
		return this.rankedValues.get(in);
	}
	
	public static ModFolio getEmptyFolio()
	{
		return nullFolio;
	}

	private static class locIterator implements Iterator<RankedModCard>
	{
		private final RankedModCard[] arr;
		private int place;
		
		public locIterator(ModFolio in)
		{
			this.arr = in.Mods;
			this.place = 0;
		}

		@Override
		public boolean hasNext()
		{
			return this.place < this.arr.length;
		}

		@Override
		public RankedModCard next()
		{
			return this.arr[this.place++];
		}
	}
}
