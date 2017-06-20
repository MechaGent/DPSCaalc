package Manifests.Mods.Mk01;

import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;

public class RankedModCard extends ModCard
{
	private final int rank;

	public RankedModCard(LotusObject inIn, int inRank)
	{
		super(inIn);
		
		if(this.maxRank >= inRank)
		{
			this.rank = inRank;
		}
		else
		{
			throw new IndexOutOfBoundsException("Tried to rank up an R" + this.maxRank + " mod to R" + inRank + "!");
		}
	}

	public int getRank()
	{
		return this.rank;
	}
}
