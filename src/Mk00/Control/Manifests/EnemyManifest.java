package Mk00.Control.Manifests;

import Enums.locMatTypes;
import Mk00.Enemy.WeinerPuppet;
import Mk00.Enemy.WeinerPuppet.WeinerPuppetBuilder;

public class EnemyManifest
{
	public static enum Enemies
	{
		Corpus_Crewman(1), Grineer_Heavy_Gunner(4), Infested_Boiler(12);
		
		private final int baseLevel;

		private Enemies(int inBaseLevel)
		{
			this.baseLevel = inBaseLevel;
		}

		public int getBaseLevel()
		{
			return this.baseLevel;
		}
	}
	
	public static WeinerPuppet getEnemy(Enemies choice, int levelDesired)
	{
		return getEnemy(choice, levelDesired, false, false, false);
	}
	
	public static WeinerPuppet getEnemy(Enemies choice, int levelDesired, boolean godMode_Health, boolean godMode_Armor, boolean godMode_Shield)
	{
		final WeinerPuppetBuilder builder = new WeinerPuppetBuilder(choice.getBaseLevel());
		final WeinerPuppet result;
		builder.setEngageGodMode_Health(godMode_Health);
		builder.setEngageGodMode_Armor(godMode_Armor);
		builder.setEngageGodMode_Shield(godMode_Shield);
		
		switch(choice)
		{
			case Infested_Boiler:
			{
				builder.setHealthMax(1200, true);
				builder.setHealthMat(locMatTypes.Fossilized);
				
				break;
			}
			case Corpus_Crewman:
			{
				builder.setHealthMax(60, true);
				builder.setHealthMat(locMatTypes.Flesh);
				builder.setShieldMax(150, true);
				builder.setShieldMat(locMatTypes.Shield);
				
				break;
			}
			case Grineer_Heavy_Gunner:
			{
				builder.setHealthMax(300, true);
				builder.setHealthMat(locMatTypes.Cloned_Flesh);
				builder.setArmorMax(500, true);
				builder.setArmorMat(locMatTypes.Ferrite_Armor);
				
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}
		
		result = builder.buildScaledToLevel(levelDesired);
		
		return result;
	}
}
