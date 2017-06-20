package Manifests.Weapons.Mk02;

import DamagePackets.Packets.scaledDamagePacket;
import DamagePackets.Packets.scaledDamagePacket.scaledDamagePacketBuilder;
import DamagePackets.Packets.unitDamagePacket;
import DamagePackets.Packets.unitDamagePacket.unitDamagePacketBuilder;
import DataStructures.Linkages.CharList.Mk03.CharList;
import Enums.TriggerTypes;
import Enums.locDamTypes;
import Manifests.Mods.Mk01.ModCard.ModEffect;
import Manifests.Mods.Mk01.ModFolio;
import Manifests.Mods.Mk01.RankedModCard;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusArray;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusVar;
import Random.XorShiftStar.Mk02.XorShiftStar;

public class Bullet
{
	private final TriggerTypes Trigger;
	private final int bulletsConsumedPerTriggerPull;
	private final scaledDamagePacket normalDamage;
	private final double normalDamageSum;
	private final int baseCritLevel;
	private final double partialCritChance;
	private final double critMult;
	private final int baseExtraBulletLevel;
	private final double partialExtraBulletChance;
	private final ProcRoller Proccy;

	public Bullet(LotusObject in)
	{
		this.Trigger = TriggerTypes.parse(in.getVarAsVar("Trigger"));
		this.bulletsConsumedPerTriggerPull = in.getVarAsVar("bulletsConsumedPerTriggerPull").getValueAsInt();
		this.normalDamage = new scaledDamagePacket(in.getVarAsObject("scaledDamagePacket"));
		this.normalDamageSum = this.normalDamage.getScaledSum();

		final double critChance = parseAsPercentage(in.getVarAsVar("critChance"));
		this.baseCritLevel = (int) critChance;
		this.partialCritChance = critChance - this.baseCritLevel;

		this.critMult = in.getVarAsVar("critMult").getValueAsDouble();

		final double extraBulletChance = parseAsPercentage(in.getVarAsVar("extraBulletChance"));
		this.baseExtraBulletLevel = (int) extraBulletChance + 1;
		this.partialExtraBulletChance = extraBulletChance - this.baseExtraBulletLevel;

		this.Proccy = new ProcRoller(in.getVarAsVar("procChance").getValueAsDouble(), this.normalDamage);
	}

	public Bullet(TriggerTypes inTrigger, int inBulletsConsumedPerTriggerPull, scaledDamagePacket inNormalDamage, double inCritChance, double inCritMult, double inMultishotChance, ProcRoller inProccy)
	{
		this.Trigger = inTrigger;
		this.bulletsConsumedPerTriggerPull = inBulletsConsumedPerTriggerPull;
		this.normalDamage = inNormalDamage;
		this.normalDamageSum = this.normalDamage.getScaledSum();
		this.baseCritLevel = (int) inCritChance;
		this.partialCritChance = inCritChance - this.baseCritLevel;
		this.critMult = inCritMult;
		this.baseExtraBulletLevel = (int) inMultishotChance;
		this.partialExtraBulletChance = inMultishotChance - this.baseExtraBulletLevel;
		this.Proccy = inProccy;
	}

	public static Bullet[] parseArray(LotusArray in)
	{
		final Bullet[] result = new Bullet[in.getLength()];

		for (int i = 0; i < result.length; i++)
		{
			result[i] = new Bullet(in.getVarAsObject(i));
		}

		return result;
	}

	private static double parseAsPercentage(LotusVar in)
	{
		return parseAsPercentage(in.getValueAsString());
	}

	private static double parseAsPercentage(String in)
	{
		final int lastIndex = in.length() - 1;
		final double result;

		if (lastIndex != -1)
		{
			if (in.charAt(lastIndex) == '%')
			{
				result = Double.parseDouble(in.substring(0, lastIndex)) / 100.0;
			}
			else
			{
				result = Double.parseDouble(in);
			}
		}
		else
		{
			result = 0;
		}

		return result;
	}

	public TriggerTypes getTrigger()
	{
		return this.Trigger;
	}

	public int getBulletsConsumedPerTriggerPull()
	{
		return this.bulletsConsumedPerTriggerPull;
	}

	public scaledDamagePacket getNormalDamage()
	{
		return this.normalDamage;
	}

	public double getNormalDamageSum()
	{
		return this.normalDamageSum;
	}

	public int getBaseCritLevel()
	{
		return this.baseCritLevel;
	}

	public double getPartialCritChance()
	{
		return this.partialCritChance;
	}
	
	public int rollForCritLevel(XorShiftStar in)
	{
		if(in.flipCoin(this.partialCritChance))
		{
			return this.baseCritLevel + 1;
		}
		else
		{
			return this.baseCritLevel;
		}
	}

	public double getCritMult()
	{
		return this.critMult;
	}

	/**
	 * 
	 * @return the minimum number of bullets this Object will spawn per triggerpull
	 */
	public int getBaseMultishotLevel()
	{
		return this.baseExtraBulletLevel;
	}

	/**
	 * 
	 * @return the chance that an additional bullet will be spawned per triggerpull
	 */
	public double getPartialMultishotChance()
	{
		return this.partialExtraBulletChance;
	}
	
	/**
	 * 
	 * @param in
	 * @return the number of bullets spawned
	 */
	public int rollForMultishot(XorShiftStar in)
	{
		if(in.flipCoin(this.partialExtraBulletChance))
		{
			return this.baseExtraBulletLevel + 1;
		}
		else
		{
			return this.baseExtraBulletLevel;
		}
	}

	public ProcRoller getProccy()
	{
		return this.Proccy;
	}
	
	public locDamTypes[] rollForProcs(XorShiftStar in)
	{
		return this.Proccy.rollForProcs(in);
	}
	
	public CharList toCharList()
	{
		return this.toCharList("\t");
	}
	
	public CharList toCharList(String delim)
	{
		return this.toCharList(new CharList(), delim);
	}
	
	public CharList toCharList(CharList prior, String delim)
	{
		prior.add("TriggerType: ");
		prior.addAsString(this.Trigger);
		prior.add(delim);
		prior.add("BulletsConsumedPerTriggerpull: ");
		prior.addAsString(this.bulletsConsumedPerTriggerPull);
		prior.add(delim);
		prior.add("NormalDamage:{");
		prior.add(delim);
		this.normalDamage.toCharList(prior, delim);
		prior.add(delim);
		prior.add('}');
		prior.add(delim);
		prior.add("NormalDamagePropSum: ");
		prior.addAsString(this.normalDamageSum);
		prior.add(delim);
		prior.add("CritChance: ");
		prior.addAsString(this.baseCritLevel + this.partialCritChance);
		prior.add(delim);
		prior.add("CritMult: ");
		prior.addAsString(this.critMult);
		prior.add(delim);
		prior.add("ExtraBulletChance: ");
		prior.addAsString(this.baseExtraBulletLevel + this.partialExtraBulletChance);
		prior.add(delim);
		this.Proccy.toCharList(prior, delim);
		
		return prior;
	}

	public static class BulletBuilder
	{
		private final Bullet base;

		private double baseDamageMult;
		private final unitDamagePacketBuilder elemMults;
		
		@SuppressWarnings("unused")
		private double moddedDamageMult;
		
		private final locDamTypes[] elemOrder;
		private final boolean[] hasElems;
		private int numElems;

		private double critChanceMult;
		private double critMultMult;
		private double extraBulletChanceMult;
		private double procChanceMult;
		private double moddedProcChanceAddition;

		public BulletBuilder(Bullet in)
		{
			this.base = in;

			this.baseDamageMult = 0;
			this.elemMults = new unitDamagePacketBuilder();
			this.moddedDamageMult = 0;
			this.elemOrder = new locDamTypes[4];
			this.hasElems = new boolean[] {
											false,
											false,
											false,
											false };
			this.numElems = 0;

			this.critChanceMult = 0;
			this.critMultMult = 0;
			this.extraBulletChanceMult = 0;
			this.procChanceMult = 0;
			this.moddedProcChanceAddition = 0;
		}

		public void applyMods(ModFolio in)
		{
			for (RankedModCard curr : in)
			{
				this.applyMod(curr);
			}
		}

		public void applyMod(RankedModCard in)
		{
			for (ModEffect curr : in.getBaseEffects())
			{
				this.applyEffect(curr, in.getRank());
			}
		}

		/**
		 * 
		 * @param in
		 * @param rank
		 * @return true if effect was recognized, false otherwise
		 */
		public boolean applyEffect(ModEffect in, int rank)
		{
			boolean result = true;
			final double delta = in.getRankedValue(rank);

			switch (in.getTarget())
			{
				case BaseDamage:
				{
					this.baseDamageMult += delta;
					break;
				}
				case ModdedDamage:
				{
					this.moddedDamageMult += delta;
					break;
				}
				case elemMult_Cold:
				{
					this.applyElemMult(locDamTypes.Cold, delta);
					break;
				}
				case elemMult_Electricity:
				{
					this.applyElemMult(locDamTypes.Electricity, delta);
					break;
				}
				case elemMult_Heat:
				{
					this.applyElemMult(locDamTypes.Heat, delta);
					break;
				}
				case elemMult_Toxin:
				{
					this.applyElemMult(locDamTypes.Toxin, delta);
					break;
				}
				default:
				{
					result = false;
					//throw new UnhandledEnumException(in.getTarget());
				}
			}
			
			return result;
		}

		private void applyElemMult(locDamTypes in, double delta)
		{
			final int index;

			switch (in)
			{
				case Cold:
				{
					index = 0;
					break;
				}
				case Electricity:
				{
					index = 1;
					break;
				}
				case Heat:
				{
					index = 2;
					break;
				}
				case Toxin:
				{
					index = 3;
					break;
				}
				default:
				{
					index = -1;
					break;
				}
			}

			this.elemMults.addToFlavor(in, delta);

			if (in.isSimpleElement())
			{
				if (!this.hasElems[index])
				{
					this.numElems++;
					this.elemOrder[this.numElems - 1] = in;
					this.hasElems[index] = true;
				}
			}
		}

		private scaledDamagePacket buildDamage()
		{
			final scaledDamagePacketBuilder result = new scaledDamagePacketBuilder();
			final unitDamagePacket locElemMults = this.elemMults.build(this.elemOrder, this.numElems);
			final double scalar = this.base.normalDamage.getScalar() * (1.0 + this.baseDamageMult);
			result.addToScalar(scalar);

			for (locDamTypes curType : locDamTypes.getAll())
			{
				final double base = this.base.normalDamage.getFlavoredValue(curType);

				if (base != 0)
				{
					final double modded;

					if (curType.isIPS())
					{
						modded = base * (1.0 + locElemMults.getFlavoredValue(curType));
					}
					else
					{
						modded = base + locElemMults.getFlavoredValue(curType);
					}

					result.addToFlavor(curType, modded);
				}
			}

			return result.build();
		}

		public Bullet build()
		{
			final TriggerTypes Trigger = this.base.Trigger;
			final int bulletsConsumedPerTriggerPull = this.base.bulletsConsumedPerTriggerPull;
			final scaledDamagePacket normalDamage = this.buildDamage();
			final double moddedCritChance = (this.base.baseCritLevel + this.base.partialCritChance) * (1.0 + this.critChanceMult);
			final double critMult = this.base.critMult * (1.0 + this.critMultMult);
			final double moddedExtraBulletChance = (this.base.baseExtraBulletLevel + this.base.partialExtraBulletChance) * (1.0 + this.extraBulletChanceMult);
			final double moddedProcChance = (this.base.Proccy.getProcChance() * (1.0 + this.procChanceMult)) + this.moddedProcChanceAddition;
			final ProcRoller Proccy = new ProcRoller(moddedProcChance, normalDamage);

			return new Bullet(Trigger, bulletsConsumedPerTriggerPull, normalDamage, moddedCritChance, critMult, moddedExtraBulletChance, Proccy);
		}
	}
}
