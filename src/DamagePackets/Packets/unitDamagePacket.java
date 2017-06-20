package DamagePackets.Packets;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;

import DamagePackets.Points.unitDamagePoint;
import DataStructures.Linkages.CharList.Mk03.CharList;
import DataStructures.Linkages.SingleLinkedList.Unsorted.Mk03.SingleLinkedList;
import Enums.locDamTypes;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusArray;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;

public class unitDamagePacket implements Iterable<unitDamagePoint>
{
	protected final unitDamagePoint[] props;

	public unitDamagePacket(LotusObject in)
	{
		this.props = initProps(in.getVarAsArray("flavoredValues"));
	}

	public unitDamagePacket(unitDamagePoint... points)
	{
		Arrays.sort(points);
		this.props = points;
	}
	
	public unitDamagePacket(unitDamagePacket old)
	{
		this.props = old.props;
	}

	private static unitDamagePoint[] initProps(LotusArray flavoredValues)
	{
		final unitDamagePoint[] result = new unitDamagePoint[flavoredValues.getLength()];

		for (int i = 0; i < result.length; i++)
		{
			final LotusObject curFlav = flavoredValues.getVarAsObject(i);
			final locDamTypes flavor = locDamTypes.parse(curFlav.getVarAsVar("flavor"));
			final double value = curFlav.getVarAsVar("value").getValueAsDouble();
			result[i] = new unitDamagePoint(flavor, value);
		}

		Arrays.sort(result);

		return result;
	}

	public int getNumFlavors()
	{
		return this.props.length;
	}

	public unitDamagePoint getPointAt(int i)
	{
		return this.props[i];
	}

	public double getFlavoredValue(locDamTypes in)
	{
		final int precLimit = in.getPrecedence();

		for (int i = 0; i < this.props.length; i++)
		{
			final unitDamagePoint cur = this.props[i];
			final locDamTypes curFlav = cur.getFlavor();

			if (curFlav == in)
			{
				return cur.getValue();
			}
			else
			{
				if (curFlav.getPrecedence() > precLimit)
				{
					return 0;
				}
			}
		}

		return 0;
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
		prior.add('{');

		for (int i = 0; i < this.props.length; i++)
		{
			prior.add(delim);
			prior.add(this.props[i].toCharList(), true);
		}

		if(this.props.length != 0)
		{
			prior.add(delim);
		}
		
		prior.add('}');

		return prior;
	}

	@Override
	public Iterator<unitDamagePoint> iterator()
	{
		return new locIterator(this.props);
	}

	private static class locIterator implements Iterator<unitDamagePoint>
	{
		private final unitDamagePoint[] spine;
		private int place;

		public locIterator(unitDamagePoint[] in)
		{
			this.spine = in;
			this.place = 0;
		}

		@Override
		public boolean hasNext()
		{
			return this.place < this.spine.length;
		}

		@Override
		public unitDamagePoint next()
		{
			return this.spine[this.place++];
		}
	}

	public static class unitDamagePacketBuilder
	{
		private final EnumMap<locDamTypes, mutFlavoredValue> flavors;

		public unitDamagePacketBuilder()
		{
			this.flavors = initFlavors();
		}

		public unitDamagePacketBuilder(unitDamagePacket old)
		{
			this.flavors = initFlavors(old.props);
		}

		private static EnumMap<locDamTypes, mutFlavoredValue> initFlavors()
		{
			final EnumMap<locDamTypes, mutFlavoredValue> result = new EnumMap<locDamTypes, mutFlavoredValue>(locDamTypes.class);

			for (locDamTypes cur : locDamTypes.getAll())
			{
				result.put(cur, new mutFlavoredValue(cur));
			}

			return result;
		}

		private static EnumMap<locDamTypes, mutFlavoredValue> initFlavors(unitDamagePoint[] in)
		{
			final EnumMap<locDamTypes, mutFlavoredValue> result = initFlavors();

			for (unitDamagePoint point : in)
			{
				result.get(point.getFlavor()).value += point.getValue();
			}

			return result;
		}

		public void addToFlavor(locDamTypes flavor, double value)
		{
			this.flavors.get(flavor).value += value;
		}

		protected unitDamagePoint[] buildProps()
		{
			final SingleLinkedList<unitDamagePoint> props = new SingleLinkedList<unitDamagePoint>();

			for (locDamTypes cur : locDamTypes.getAll())
			{
				final mutFlavoredValue curVal = this.flavors.get(cur);

				if (curVal.value != 0)
				{
					props.add(new unitDamagePoint(cur, curVal.value));
				}
			}

			final unitDamagePoint[] result = new unitDamagePoint[props.getSize()];
			int i = 0;

			for (unitDamagePoint curPoint : props)
			{
				result[i] = curPoint;
			}

			Arrays.sort(result);

			return result;
		}

		public void redistributeCombinations(locDamTypes[] elemOrder, int numElems)
		{
			int i = 0;

			while (numElems > 1)
			{
				final mutFlavoredValue var1 = this.flavors.get(elemOrder[i++]);
				final mutFlavoredValue var2 = this.flavors.get(elemOrder[i++]);
				final mutFlavoredValue varC = this.flavors.get(locDamTypes.parseComplexElement(var1.flavor, var2.flavor));
				varC.value += var1.value + var2.value;
				var1.value = 0;
				var2.value = 0;

				numElems -= 2;
			}
		}

		public unitDamagePacket build()
		{
			return new unitDamagePacket(this.buildProps());
		}
		
		public unitDamagePacket build(locDamTypes[] elemOrder, int numElems)
		{
			this.redistributeCombinations(elemOrder, numElems);
			return this.build();
		}

		protected static class mutFlavoredValue
		{
			public final locDamTypes flavor;
			public double value;

			public mutFlavoredValue(locDamTypes inFlavor)
			{
				this.flavor = inFlavor;
				this.value = 0;
			}
		}
	}
}
