package Enums;

public enum AfflictionTypes
{
	Ragdolled,
	StandingUp_fromRagdoll,
	BulletDamage,
	Stagger_fromImpact,			//Impact,
	Weakness,					//Puncture,
	GreviouslyPapercut,			//Slash,
	Chilled,					//Cold,
	Stunned_fromElectricity,	//Electricity,
	Damaged_fromElectricity,
	Ablaze,						//Heat,
	Stunned_fromFire,
	Poisoned,					//Toxin,
	KnockedDown_fromBlast,		//Blast,
	StandingUp_fromKnockdown,
	Corroded,					//Corrosive,
	Gassed,						//Gas,
	Magnetized,					//Magnetic,
	Confused_fromRadiation,		//Radiation,
	Plagued,					//Viral,
	None;
	
	private static final AfflictionTypes[] All = AfflictionTypes.values();
	private static final AfflictionTypes[] commonPoolSingletons = new AfflictionTypes[]{Stagger_fromImpact,	KnockedDown_fromBlast, StandingUp_fromKnockdown};
	
	public static int getNumTypes()
	{
		return All.length;
	}
	
	public static AfflictionTypes[] getAll()
	{
		return All;
	}
	
	public static AfflictionTypes[] getCommonPoolSingletons()
	{
		return commonPoolSingletons;
	}
	
	public static class AfflictionToken
	{
		private final AfflictionTypes type;
		private final AfflictionTokenMinter Owner;
		
		public AfflictionToken(AfflictionTypes inType, AfflictionTokenMinter inOwner)
		{
			this.type = inType;
			this.Owner = inOwner;
		}
		
		public boolean isExpired()
		{
			return this.Owner.afflictionIsExpired(this.type);
		}
		
		public AfflictionTypes getType()
		{
			return this.type;
		}
	}
	
	public static interface AfflictionTokenMinter
	{
		public boolean afflictionIsExpired(AfflictionTypes in);
		public void forceExpired();
	}
}
