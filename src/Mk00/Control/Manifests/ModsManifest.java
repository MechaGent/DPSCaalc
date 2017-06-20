package Mk00.Control.Manifests;

import java.util.EnumSet;

import Enums.locDamTypes;
import Mk00.Weapons.ModFolio.ModFolioBuilder;

public class ModsManifest
{
	public static ModFolioBuilder applyMod(ModFolioBuilder in, RankedModChoice... choices)
	{
		for (RankedModChoice curChoice : choices)
		{
			applyMod(in, curChoice.mod, curChoice.desiredRank);
		}

		return in;
	}

	public static ModFolioBuilder applyMod(ModFolioBuilder in, Mods choice, int desiredRank)
	{
		in.addModName(choice);

		if (desiredRank == -1)
		{
			desiredRank = choice.maxRank;
		}

		switch (choice)
		{
			case Serration: // "+165% DAMAGE"
			{
				final double baseEffect = 0.15;
				final double rankedEffect = getRankedEffect(baseEffect, desiredRank, choice.getMaxRank());

				in.modifyBaseDamageMult(rankedEffect);
				break;
			}
			case Split_Chamber: // "+90% MULTISHOT"
			{
				final double baseEffect = 0.15;
				final double rankedEffect = getRankedEffect(baseEffect, desiredRank, choice.getMaxRank());

				in.modifyMultiShotMult(rankedEffect);

				break;
			}
			case Point_Strike: // "+150% CRITICAL CHANCE"
			{
				final double baseEffect = 0.25;
				final double rankedEffect = getRankedEffect(baseEffect, desiredRank, choice.getMaxRank());

				in.modifyCritChanceMult(rankedEffect);
				break;
			}
			case Vital_Sense: // "+120% CRIT DAMAGE"
			{
				final double baseEffect = 0.20;
				final double rankedEffect = getRankedEffect(baseEffect, desiredRank, choice.getMaxRank());

				in.modifyCritMultMult(rankedEffect);
				break;
			}
			case High_Voltage: // "+60% Electricity +60% Status Chance"
			{
				final double baseEffect1 = 0.15;
				final double rankedEffect1 = getRankedEffect(baseEffect1, desiredRank, choice.getMaxRank());
				final double baseEffect2 = 0.15;
				final double rankedEffect2 = getRankedEffect(baseEffect2, desiredRank, choice.getMaxRank());

				in.modifyModdedBaseElemMult(locDamTypes.Electricity, rankedEffect1);
				in.modifyProcChanceMult(rankedEffect2);
				;
				break;
			}
			case Rime_Rounds: // "+60% Electricity +60% Status Chance"
			{
				final double baseEffect1 = 0.15;
				final double rankedEffect1 = getRankedEffect(baseEffect1, desiredRank, choice.getMaxRank());
				final double baseEffect2 = 0.15;
				final double rankedEffect2 = getRankedEffect(baseEffect2, desiredRank, choice.getMaxRank());

				in.modifyModdedBaseElemMult(locDamTypes.Cold, rankedEffect1);
				in.modifyProcChanceMult(rankedEffect2);
				;
				break;
			}
			case Thermite_Rounds: // "+60% Electricity +60% Status Chance"
			{
				final double baseEffect1 = 0.15;
				final double rankedEffect1 = getRankedEffect(baseEffect1, desiredRank, choice.getMaxRank());
				final double baseEffect2 = 0.15;
				final double rankedEffect2 = getRankedEffect(baseEffect2, desiredRank, choice.getMaxRank());

				in.modifyModdedBaseElemMult(locDamTypes.Heat, rankedEffect1);
				in.modifyProcChanceMult(rankedEffect2);
				;
				break;
			}
			case Malignant_Force: // "+60% Electricity +60% Status Chance"
			{
				final double baseEffect1 = 0.15;
				final double rankedEffect1 = getRankedEffect(baseEffect1, desiredRank, choice.getMaxRank());
				final double baseEffect2 = 0.15;
				final double rankedEffect2 = getRankedEffect(baseEffect2, desiredRank, choice.getMaxRank());

				in.modifyModdedBaseElemMult(locDamTypes.Toxin, rankedEffect1);
				in.modifyProcChanceMult(rankedEffect2);
				;
				break;
			}
			case Point_Blank_Prime:
			{
				final double baseEffect1 = 0.15;
				final double rankedEffect1 = getRankedEffect(baseEffect1, desiredRank, choice.getMaxRank());
				in.modifyBaseDamageMult(rankedEffect1);
				break;
			}
			case Hells_Chamber:
			{
				final double baseEffect1 = 0.20;
				final double rankedEffect1 = getRankedEffect(baseEffect1, desiredRank, choice.getMaxRank());
				in.modifyMultiShotMult(rankedEffect1);
				break;
			}
			default:
			{
				throw new IllegalArgumentException("Unrecognized mod: " + choice.toString());
			}
		}

		return in;
	}

	private static double getRankedEffect(double baseEffect, int desiredRank, int maxRank)
	{
		return getRankedEffect(baseEffect, desiredRank, maxRank, false);
	}

	private static double getRankedEffect(double baseEffect, int desiredRank, int maxRank, boolean overrideMaxRankCheck)
	{
		if (!overrideMaxRankCheck && desiredRank > maxRank)
		{
			throw new IndexOutOfBoundsException("Max Rank Exceeded! Tried to go to rank " + desiredRank + ", but can only go to rank " + maxRank);
		}
		else
		{
			return baseEffect * (1 + desiredRank);
		}
	}

	public static enum Mods
	{
		Serration(
				10,
				Rarities.Uncommon,
				SortingTags.Rifle,
				SortingTags.BaseDamage),
		Split_Chamber(
				5,
				Rarities.Rare,
				SortingTags.Rifle,
				SortingTags.Multishot),
		Point_Strike(
				5,
				Rarities.Common,
				SortingTags.Rifle,
				SortingTags.CriticalChance),
		Vital_Sense(
				5,
				Rarities.Rare,
				SortingTags.Rifle,
				SortingTags.CriticalMultiplier),
		High_Voltage(
				3,
				Rarities.Rare,
				SortingTags.Rifle,
				SortingTags.ElemDamage_Electricity,
				SortingTags.StatusChance,
				SortingTags.MultiEffect,
				SortingTags.Set_TethrasDoom),
		Rime_Rounds(
				3,
				Rarities.Rare,
				SortingTags.Rifle,
				SortingTags.ElemDamage_Cold,
				SortingTags.StatusChance,
				SortingTags.MultiEffect,
				SortingTags.Set_TethrasDoom),
		Thermite_Rounds(
				3,
				Rarities.Rare,
				SortingTags.Rifle,
				SortingTags.ElemDamage_Heat,
				SortingTags.StatusChance,
				SortingTags.MultiEffect,
				SortingTags.Set_TethrasDoom),
		Malignant_Force(
				3,
				Rarities.Rare,
				SortingTags.Rifle,
				SortingTags.ElemDamage_Toxin,
				SortingTags.StatusChance,
				SortingTags.MultiEffect,
				SortingTags.Set_TethrasDoom),
		Point_Blank_Prime(
				10,
				Rarities.Legendary,
				SortingTags.Shotgun,
				SortingTags.BaseDamage),
		Hells_Chamber(
				5,
				Rarities.Rare,
				SortingTags.Shotgun,
				SortingTags.Multishot);

		private final int maxRank;
		private final Rarities rarity;
		private final EnumSet<SortingTags> tags;

		private Mods(int inMaxRank, Rarities inRarity, SortingTags... inTags)
		{
			this.maxRank = inMaxRank;
			this.rarity = inRarity;
			this.tags = initTags(inTags);
		}

		public boolean hasTag(SortingTags in)
		{
			return this.tags.contains(in);
		}

		public int getMaxRank()
		{
			return this.maxRank;
		}

		public Rarities getRarity()
		{
			return this.rarity;
		}
		
		public ModFolioBuilder applyToModFolio(ModFolioBuilder in, int desiredRank)
		{
			return applyMod(in, this, desiredRank);
		}

		private static EnumSet<SortingTags> initTags(SortingTags[] inTags)
		{
			final EnumSet<SortingTags> result = EnumSet.noneOf(SortingTags.class);

			for (SortingTags curTag : inTags)
			{
				result.add(curTag);
			}

			return result;
		}
	}

	public static enum SortingTags
	{
		Rifle,
		Shotgun,
		BaseDamage,
		ElemDamage_PhysicalType,
		ElemDamage_Impact,
		ElemDamage_Puncture,
		ElemDamage_Slash,
		ElemDamage_Finisher,
		ElemDamage_Cold,
		ElemDamage_Electricity,
		ElemDamage_Heat,
		ElemDamage_Toxin,
		ElemDamage_Blast,
		ElemDamage_Corrosive,
		ElemDamage_Gas,
		ElemDamage_Magnetic,
		ElemDamage_Radiation,
		ElemDamage_Viral,
		Multishot,
		CriticalChance,
		CriticalMultiplier,
		StatusChance,
		MultiEffect,
		Set_TethrasDoom,;
	}

	public static enum Rarities
	{
		Common,
		Uncommon,
		Rare,
		Legendary;
	}

	public static class RankedModChoice
	{
		private final Mods mod;
		private final int desiredRank;

		public RankedModChoice(Mods inMod, int inDesiredRank)
		{
			this.mod = inMod;
			this.desiredRank = inDesiredRank;
		}

		public Mods getMod()
		{
			return this.mod;
		}

		public int getDesiredRank()
		{
			return this.desiredRank;
		}
	}
}
