package Manifests.Weapons.Mk02;

import DamagePackets.Packets.unitDamagePacket;
import DataStructures.Linkages.CharList.Mk03.CharList;
import DataStructures.Linkages.SingleLinkedList.Unsorted.Mk03.SingleLinkedList;
import Enums.locDamTypes;
import Random.XorShiftStar.Mk02.XorShiftStar;

public class ProcRoller
{
	private final double procChance;
	private final ProcOdds[] Odds;
	private final double oddsTotal;
	private final locDamTypes[] forcedProcs;

	public ProcRoller(double inProcChance, unitDamagePacket in)
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
					curOdds = 4 * in.getFlavoredValue(curType);
					break;
				}
				default:
				{
					curOdds = in.getFlavoredValue(curType);
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

		/*
		 * just a fill-in for now
		 */
		this.forcedProcs = new locDamTypes[0];
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
	public locDamTypes rollForProc_IfProcced(XorShiftStar dice)
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

	/**
	 * 
	 * @param dice
	 * @return an array containing (one of the elements to proc, assuming a proc has occurred) and (all forced procs)
	 */
	public locDamTypes[] rollForProcs(XorShiftStar dice)
	{
		final locDamTypes[] result = new locDamTypes[this.forcedProcs.length + (this.rollToSeeIfProcced(dice)? 1 : 0)];

		result[0] = this.rollForProc_IfProcced(dice);
		
		for(int i = 1; i < this.forcedProcs.length; i++)
		{
			result[i] = this.forcedProcs[i];
		}

		return result;
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
		prior.add("ProcChance: ");
		prior.addAsString(this.procChance);
		
		for(int i = 0; i < this.forcedProcs.length; i++)
		{
			prior.add(delim);
			prior.addAsString(this.forcedProcs[i]);
		}
		
		return prior;
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
