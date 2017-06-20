package Enums;

public enum StatusProcEffectValues
{
	ViralHealthReductionMult(0.5),
	MagneticShieldReductionMult(1.0 - 0.75),
	ChilledMovementSpeedReductionMult(0.5),
	CorrosiveDegradeMult(0.75),
	FinalDamageMult_Gas(0.5),
	FinalDamageMult_Toxin(0.5),
	FinalDamageMult_Heat(0.5),
	WeaknessReductionMult(0.3),
	FinalDamageMult_Slash(0.35)
	;
	
	private final double value;

	private StatusProcEffectValues(double inValue)
	{
		this.value = inValue;
	}

	public double getValue()
	{
		return this.value;
	}
}
