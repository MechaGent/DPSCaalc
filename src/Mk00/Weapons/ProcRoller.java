package Mk00.Weapons;

import DataStructures.Linkages.SingleLinkedList.Unsorted.Mk03.SingleLinkedList;
import Enums.locDamTypes;
import Mk00.Damage.Packets.UnitDamagePacket;
import Random.XorShiftStar.Mk02.XorShiftStar;

public class ProcRoller
{
	private final double procChance;
	private final ProcOdds[] Odds;
	private final double oddsTotal;

	public ProcRoller(double inProcChance, UnitDamagePacket in)
	{
		final SingleLinkedList<ProcOdds> rawOdds = new SingleLinkedList<ProcOdds>();
		double locTotal = 0;

		for (locDamTypes curType : locDamTypes.getAll())
		{
			final double curOdds;

			switch (curType)
			{
				case Impact:
				case Puncture:
				case Slash:
				{
					curOdds = 4 * in.getDamagePortion(curType);
					break;
				}
				default:
				{
					curOdds = in.getDamagePortion(curType);
					break;
				}
			}

			locTotal += curOdds;
			rawOdds.add(new ProcOdds(curType, curOdds));

		}

		this.procChance = inProcChance;

		this.Odds = new ProcOdds[rawOdds.getSize()];
		int i = 0;

		for (ProcOdds cur : rawOdds)
		{
			this.Odds[i] = cur;
			i++;
		}

		this.oddsTotal = locTotal;
	}

	public double getProcChance()
	{
		return this.procChance;
	}
	
	public double getOdds(locDamTypes type)
	{
		return this.Odds[type.ordinal()].odds;
	}
	
	public double getTotalOdds()
	{
		return this.oddsTotal;
	}

	/**
	 * picks one of the elements to proc, assuming a proc has occurred
	 * 
	 * @param dice
	 * @return
	 */
	public locDamTypes rollForProc(XorShiftStar dice)
	{
		double diceRoll = dice.nextDouble(this.oddsTotal);

		for (int i = 0; i < this.Odds.length; i++)
		{
			final ProcOdds cur = this.Odds[i];

			/*
			if (cur == null)
			{
				System.out.println("index: " + i + " of " + this.Odds.length);
			}
			*/

			final double locOdds = cur.getOdds();

			if (diceRoll > locOdds)
			{
				diceRoll -= locOdds;
			}
			else
			{
				return this.Odds[i].getProcType();
			}
		}

		return null;
	}

	/**
	 * checks if a proc occurred at all. Call rollForProc(dice) if this returns true;
	 * 
	 * @param dice
	 * @return
	 */
	public boolean rollToSeeIfProcced(XorShiftStar dice)
	{
		return dice.nextDouble(1.0) <= this.procChance;
	}

	private static class ProcOdds
	{
		private final locDamTypes procType;
		private final double odds;

		public ProcOdds(locDamTypes inProcType, double inOdds)
		{
			super();
			this.procType = inProcType;
			this.odds = inOdds;
		}

		public locDamTypes getProcType()
		{
			return this.procType;
		}

		public double getOdds()
		{
			return this.odds;
		}
	}
}
