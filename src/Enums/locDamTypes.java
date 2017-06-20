package Enums;

import Parsers.LotusParser.aTopLayer.Interfaces.LotusVar;

public enum locDamTypes
{
	Impact(
			0,
			true,
			false),
	Puncture(
			1,
			true,
			false),
	Slash(
			2,
			true,
			false),
	Finisher(
			-1,
			false,
			false),
	Cold(
			3,
			false,
			false),
	Electricity(
			3,
			false,
			false),
	Heat(
			3,
			false,
			false),
	Toxin(
			3,
			false,
			false),
	Blast(
			4,
			false,
			true),
	Corrosive(
			4,
			false,
			true),
	Gas(
			4,
			false,
			true),
	Magnetic(
			4,
			false,
			true),
	Radiation(
			4,
			false,
			true),
	Viral(
			4,
			false,
			true),;

	private static final locDamTypes[] All = locDamTypes.values();
	private static final locDamTypes[] atomicElements = new locDamTypes[] {
																			Cold,
																			Electricity,
																			Heat,
																			Toxin };

	private final int precedence;
	private final boolean isIPS;
	private final boolean isComplexElement;

	private locDamTypes(int inPrecedence, boolean inIsIPS, boolean inIsComplexElement)
	{
		this.precedence = inPrecedence;
		this.isIPS = inIsIPS;
		this.isComplexElement = inIsComplexElement;
	}

	public boolean isIPS()
	{
		return this.isIPS;
	}

	public boolean isSimpleElement()
	{
		return !this.isIPS && !this.isComplexElement;
	}

	public boolean isComplexElement()
	{
		return this.isComplexElement;
	}

	public int getPrecedence()
	{
		return this.precedence;
	}

	public static locDamTypes parse(LotusVar in)
	{
		return parse(in.getValueAsString());
	}

	public static locDamTypes parse(String in)
	{
		final locDamTypes result;

		switch (in.toLowerCase())
		{
			case "blast":
			{
				result = Blast;
				break;
			}
			case "cold":
			{
				result = Cold;
				break;
			}
			case "corrosive":
			{
				result = Corrosive;
				break;
			}
			case "electricity":
			{
				result = Electricity;
				break;
			}
			case "finisher":
			{
				result = Finisher;
				break;
			}
			case "gas":
			{
				result = Gas;
				break;
			}
			case "heat":
			{
				result = Heat;
				break;
			}
			case "impact":
			{
				result = Impact;
				break;
			}
			case "magnetic":
			{
				result = Magnetic;
				break;
			}
			case "puncture":
			{
				result = Puncture;
				break;
			}
			case "radiation":
			{
				result = Radiation;
				break;
			}
			case "slash":
			{
				result = Slash;
				break;
			}
			case "toxin":
			{
				result = Toxin;
				break;
			}
			case "viral":
			{
				result = Viral;
				break;
			}
			default:
			{
				throw new IllegalArgumentException("unknown enum: " + in);
			}
		}

		return result;
	}

	public static locDamTypes parseComplexElement(locDamTypes var1, locDamTypes var2)
	{
		final locDamTypes result;

		switch (var1)
		{
			case Cold:
			{
				switch (var2)
				{
					case Cold:
					{
						result = Cold;
						break;
					}
					case Electricity:
					{
						result = Magnetic;
						break;
					}
					case Heat:
					{
						result = Blast;
						break;
					}
					case Toxin:
					{
						result = Viral;
						break;
					}
					default:
					{
						throw new IllegalArgumentException("bad arg: " + var2.toString());
					}
				}
				break;
			}
			case Electricity:
			{
				switch (var2)
				{
					case Cold:
					{
						result = Magnetic;
						break;
					}
					case Electricity:
					{
						result = Electricity;
						break;
					}
					case Heat:
					{
						result = Radiation;
						break;
					}
					case Toxin:
					{
						result = Corrosive;
						break;
					}
					default:
					{
						throw new IllegalArgumentException("bad arg: " + var2.toString());
					}
				}
				break;
			}
			case Heat:
			{
				switch (var2)
				{
					case Cold:
					{
						result = Blast;
						break;
					}
					case Electricity:
					{
						result = Radiation;
						break;
					}
					case Heat:
					{
						result = Heat;
						break;
					}
					case Toxin:
					{
						result = Gas;
						break;
					}
					default:
					{
						throw new IllegalArgumentException("bad arg: " + var2.toString());
					}
				}
				break;
			}
			case Toxin:
			{
				switch (var2)
				{
					case Cold:
					{
						result = Viral;
						break;
					}
					case Electricity:
					{
						result = Corrosive;
						break;
					}
					case Heat:
					{
						result = Gas;
						break;
					}
					case Toxin:
					{
						result = Toxin;
						break;
					}
					default:
					{
						throw new IllegalArgumentException("bad arg: " + var2.toString());
					}
				}
				break;
			}
			default:
			{
				throw new IllegalArgumentException("bad arg: " + var2.toString());
			}
		}

		return result;
	}

	/**
	 * 
	 * @return all non-IPS atomic elements
	 */
	public static locDamTypes[] getAtomicElements()
	{
		return atomicElements;
	}

	public static int getNumTypes()
	{
		return All.length;
	}

	public static locDamTypes[] getAll()
	{
		return All;
	}
}
