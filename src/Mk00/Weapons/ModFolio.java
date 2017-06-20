package Mk00.Weapons;

import DataStructures.Linkages.CharList.Mk03.CharList;
import DataStructures.Linkages.SingleLinkedList.Unsorted.Mk03.SingleLinkedList;
import Enums.locDamTypes;
import Mk00.Control.Manifests.ModsManifest.Mods;
import Mk00.Damage.Packets.ScaledDamagePacket;
import Mk00.Damage.Packets.UnitDamagePacket;

public class ModFolio
{
	private final double fireRateMult;
	private final double reloadTimeMult;
	private final double magSizeMult;

	private final double baseDamageMult;
	private final double finalDamageMult;
	private final UnitDamagePacket moddedBaseElemMults;
	
	/**
	 * Currently unsupported
	 */
	private final UnitDamagePacket flatFinalElemAdditives;
	
	private final double critChanceMult;
	private final double critMultMult;
	private final double procChanceMult;
	private final double procChanceFlatAdditive;
	private final double multiShotMult;
	
	private final Mods[] appliedMods;

	public ModFolio(double inFireRateMult, double inReloadTimeMult, double inMagSizeMult, double inBaseDamageMult, double inFinalDamageMult, UnitDamagePacket inModdedBaseElemMults, UnitDamagePacket inFlatFinalElemAdditives, double inCritChanceMult, double inCritMultMult, double inProcChanceMult, double inProcChanceFlatAdditive, double inMultiShotMult, Mods[] inAppliedMods)
	{
		this.fireRateMult = inFireRateMult;
		this.reloadTimeMult = inReloadTimeMult;
		this.magSizeMult = inMagSizeMult;
		this.baseDamageMult = inBaseDamageMult;
		this.finalDamageMult = inFinalDamageMult;
		this.moddedBaseElemMults = inModdedBaseElemMults;
		this.flatFinalElemAdditives = inFlatFinalElemAdditives;
		this.critChanceMult = inCritChanceMult;
		this.critMultMult = inCritMultMult;
		this.procChanceMult = inProcChanceMult;
		this.procChanceFlatAdditive = inProcChanceFlatAdditive;
		this.multiShotMult = inMultiShotMult;
		this.appliedMods = inAppliedMods;
	}
	
	public ModFolio(double inFireRateMult, double inReloadTimeMult, double inMagSizeMult, double inBaseDamageMult, double inFinalDamageMult, UnitDamagePacket inModdedBaseElemMults, UnitDamagePacket inFlatFinalElemAdditives, double inCritChanceMult, double inCritMultMult, double inProcChanceMult, double inProcChanceFlatAdditive, double inMultiShotMult, SingleLinkedList<Mods> inAppliedMods)
	{
		this.fireRateMult = inFireRateMult;
		this.reloadTimeMult = inReloadTimeMult;
		this.magSizeMult = inMagSizeMult;
		this.baseDamageMult = inBaseDamageMult;
		this.finalDamageMult = inFinalDamageMult;
		this.moddedBaseElemMults = inModdedBaseElemMults;
		this.flatFinalElemAdditives = inFlatFinalElemAdditives;
		this.critChanceMult = inCritChanceMult;
		this.critMultMult = inCritMultMult;
		this.procChanceMult = inProcChanceMult;
		this.procChanceFlatAdditive = inProcChanceFlatAdditive;
		this.multiShotMult = inMultiShotMult;
		
		final Mods[] applied = new Mods[inAppliedMods.getSize()];
		int i = 0;
		
		for(Mods cur: inAppliedMods)
		{
			applied[i++] = cur;
		}
		
		this.appliedMods = applied;
	}
	
	public Mods[] getMods()
	{
		return this.appliedMods;
	}
	
	public CharList getModsAsCharList()
	{
		final CharList result = new CharList();
		
		for(Mods cur: this.appliedMods)
		{
			result.add('\t');
			result.add(cur.toString());
		}
		
		return result;
	}

	public double getFireRateMult()
	{
		return this.fireRateMult;
	}

	public double getReloadTimeMult()
	{
		return this.reloadTimeMult;
	}

	public double getMagSizeMult()
	{
		return this.magSizeMult;
	}

	public ScaledDamagePacket getModdedPacket(ScaledDamagePacket base)
	{
		final double scalar = base.getScalar() * (1.0 + this.baseDamageMult);
		final double[] data = new double[locDamTypes.getNumTypes()];

		for (locDamTypes curType : locDamTypes.getAll())
		{
			final double moddedProp;
			
			switch (curType)
			{
				case Impact:
				case Puncture:
				case Slash:
				{
					moddedProp = base.getDamagePortion(curType) * (1.0 + this.moddedBaseElemMults.getDamagePortion(curType));
					break;
				}
				default:
				{
					moddedProp = base.getDamagePortion(curType) + this.moddedBaseElemMults.getDamagePortion(curType);
					break;
				}
			}
			
			data[curType.ordinal()] = moddedProp;
		}
		
		return new ScaledDamagePacket(data, scalar);
	}

	public double getBaseDamageMult()
	{
		return this.baseDamageMult;
	}

	public double getFinalDamageMult()
	{
		return this.finalDamageMult;
	}

	public UnitDamagePacket getModdedBaseElemMults()
	{
		return this.moddedBaseElemMults;
	}

	public UnitDamagePacket getFlatFinalElemAdditives()
	{
		return this.flatFinalElemAdditives;
	}

	public double getCritChanceMult()
	{
		return this.critChanceMult;
	}

	public double getCritMultMult()
	{
		return this.critMultMult;
	}

	public double getProcChanceMult()
	{
		return this.procChanceMult;
	}

	public double getMultiShotMult()
	{
		return this.multiShotMult;
	}

	public double getProcChanceFlatAdditive()
	{
		return this.procChanceFlatAdditive;
	}

	public static class ModFolioBuilder
	{
		private double fireRateMult;
		private double reloadTimeMult;
		private double magSizeMult;
		private double baseDamageMult;
		private double finalDamageMult;
		private final UnitDamagePacket moddedBaseElemMults;
		private final UnitDamagePacket flatFinalElemAdditives;
		private double critChanceMult;
		private double critMultMult;
		private double procChanceMult;
		private double procChanceFlatAdditive;
		private double multiShotMult;
		private SingleLinkedList<Mods> appliedMods;

		public ModFolioBuilder()
		{
			this.reloadTimeMult = 0;
			this.magSizeMult = 0;
			this.fireRateMult = 0;
			this.baseDamageMult = 0;
			this.finalDamageMult = 0;
			this.moddedBaseElemMults = new UnitDamagePacket();
			this.flatFinalElemAdditives = new UnitDamagePacket();
			this.critChanceMult = 0;
			this.critMultMult = 0;
			this.procChanceMult = 0;
			this.procChanceFlatAdditive = 0;
			this.multiShotMult = 0;
			this.appliedMods = new SingleLinkedList<Mods>();
		}

		public void modifyReloadTimeMult(double inReloadTimeMult)
		{
			this.reloadTimeMult += inReloadTimeMult;
		}

		public void modifyMagSizeMult(double inMagSizeMult)
		{
			this.magSizeMult += inMagSizeMult;
		}

		public void modifyFireRateMult(double inFireRateMult)
		{
			this.fireRateMult += inFireRateMult;
		}

		public void modifyBaseDamageMult(double inBaseDamageMult)
		{
			this.baseDamageMult += inBaseDamageMult;
		}

		public void modifyFinalDamageMult(double inFinalDamageMult)
		{
			this.finalDamageMult += inFinalDamageMult;
		}

		public void modifyModdedBaseElemMult(locDamTypes type, double value)
		{
			this.moddedBaseElemMults.setDamagePortion(type, this.moddedBaseElemMults.getDamagePortion(type) + value);
		}

		public void modifyFlatFinalElemAdditives(locDamTypes type, double value)
		{
			this.flatFinalElemAdditives.setDamagePortion(type, this.flatFinalElemAdditives.getDamagePortion(type) + value);
		}

		public void modifyCritChanceMult(double inCritChanceMult)
		{
			this.critChanceMult += inCritChanceMult;
		}

		public void modifyCritMultMult(double inCritMultMult)
		{
			this.critMultMult += inCritMultMult;
		}

		public void modifyProcChanceMult(double inProcChanceMult)
		{
			this.procChanceMult += inProcChanceMult;
		}

		public void modifyProcChanceFlatAdditive(double inProcChanceFlatAdditive)
		{
			this.procChanceFlatAdditive += inProcChanceFlatAdditive;
		}

		public void modifyMultiShotMult(double inMultiShotMult)
		{
			this.multiShotMult += inMultiShotMult;
		}
		
		public void addModName(Mods in)
		{
			this.appliedMods.add(in);
		}

		public ModFolio build()
		{
			return new ModFolio(this.fireRateMult, this.reloadTimeMult, this.magSizeMult, this.baseDamageMult, this.finalDamageMult, this.moddedBaseElemMults, this.flatFinalElemAdditives, this.critChanceMult, this.critMultMult, this.procChanceMult, this.procChanceFlatAdditive, this.multiShotMult, this.appliedMods);
		}
	}
}
