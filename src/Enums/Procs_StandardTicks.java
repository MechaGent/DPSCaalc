package Enums;

public enum Procs_StandardTicks
{
	Blast_Knockdown(1),
	Blast_Standup(1),
	Chilled(7),
	Stunned_FromElectricity(4),
	Poisoned(8+1),
	Ablaze(6+1),
	FirePanic_Humanoid(4+1),
	FirePanic_Infested(3+1),
	FirePanic_ChargersAndMoas(2+1),
	Magnetized(4+1),
	Weakened(6+1),
	Confused(12+1),
	Bleeding(6+1),
	Plagued(6+1);
	
	private final int maxTicks;
	
	private Procs_StandardTicks(int in)
	{
		this.maxTicks = in;
	}
	
	public int getMaxTicks()
	{
		return this.maxTicks;
	}
}
