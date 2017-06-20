package Mk00.Control.Manifests;

import Enums.locDamTypes;
import Mk00.Control.Manifests.ModsManifest.Mods;
import Mk00.Weapons.ModFolio;
import Mk00.Weapons.ModFolio.ModFolioBuilder;

public class ModBuildManifest
{
	public static enum PartialBuilds
	{
		Rifle_StandardCrit, Rifle_CritProcHybrid, Shotgun_Standard, Shotgun_VaurSpecial, Empty;
		
		public ModFolio getPartialBuild()
		{
			return ModBuildManifest.getPartialBuild(this);
		}
	}

	public static ModFolio getPartialBuild(PartialBuilds choice)
	{
		final ModFolioBuilder result = new ModFolioBuilder();

		switch (choice)
		{
			
			case Shotgun_VaurSpecial:
			{
				
				
				result.modifyBaseDamageMult(1.65);	 						//Primed Point Blank, R10
				result.modifyMultiShotMult(1.20);							//Hell's Chamber, R5
				result.modifyBaseDamageMult(0.60);							//Blaze, R3
				result.modifyModdedBaseElemMult(locDamTypes.Gas, 1.50);		//Blaze - R3, and Contagious Spread, R5
				result.modifyReloadTimeMult(0.30);							//Tactical Pump, R5
				result.modifyReloadTimeMult(0.15);							//Seeking Fury
				result.modifyCritChanceMult(0.90);							//Blunderbuss
				result.modifyCritMultMult(1.10);							//Primed Ravage
				break;
			}
			case Shotgun_Standard:
			{
				//ModsManifest.applyMod(result, Mods.Point_Blank_Prime, -1);
				Mods.Point_Blank_Prime.applyToModFolio(result, -1);
				Mods.Hells_Chamber.applyToModFolio(result, -1);
				
				
				//result.modifyBaseDamageMult(1.65);	 						//Primed Point Blank, R10
				//result.modifyMultiShotMult(1.20);							//Hell's Chamber, R5
				result.modifyBaseDamageMult(0.60);							//Blaze, R3
				result.modifyModdedBaseElemMult(locDamTypes.Gas, 1.50);	//Blaze - R3, and Contagious Spread, R5
				result.modifyReloadTimeMult(0.30);							//Tactical Pump, R5
				result.modifyReloadTimeMult(0.15);							//Seeking Fury
				
				
				break;
			}
			case Empty:
			{
				break;
			}
			case Rifle_StandardCrit:
			{
				result.modifyMultiShotMult(0.90);	//Split Chamber, R5
				result.modifyBaseDamageMult(1.65);	//Serration, R10
				result.modifyCritChanceMult(1.50);	//Point Strike, R5
				result.modifyCritMultMult(1.20);	//Vital Sense, R5
				break;
			}
			case Rifle_CritProcHybrid:
			{
				result.modifyMultiShotMult(0.90);	//Split Chamber, R5
				result.modifyBaseDamageMult(1.65);	//Serration, R10
				result.modifyCritChanceMult(1.50);	//Point Strike, R5
				result.modifyCritMultMult(1.20);	//Vital Sense, R5
				result.modifyModdedBaseElemMult(locDamTypes.Corrosive, 1.20);	//High Voltage and Malignant Force, both R5
				result.modifyProcChanceMult(1.2);								//High Voltage and Malignant Force, both R5
				
				result.modifyModdedBaseElemMult(locDamTypes.Blast, 1.20);		//Thermite Rounds and Rime Rounds, both R5
				result.modifyProcChanceMult(1.2);								//Thermite Rounds and Rime Rounds, both R5
				
				break;
			}
			default:
			{
				throw new IllegalArgumentException(choice.toString());
			}
		}

		return result.build();
	}
}
