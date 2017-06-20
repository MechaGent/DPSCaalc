package Mk01.Control;

import Manifests.Enemy.Mk01.AvailableEnemies;
import Manifests.Mods.Mk01.ModFolio;
import Manifests.Weapons.Mk02.AvailableWeapons;
import Mk01.Control.Trial.TrialModes;

public class MainClass
{
	public static void main(String[] args)
	{
		final TrialModes testMode = TrialModes.find_TTK;
		final AvailableEnemies inEnemyChoice = AvailableEnemies.Lancer;
		final int inEnemyLevel = 10;
		final AvailableWeapons inWeaponChoice = AvailableWeapons.Braton;
		final ModFolio inWeaponMods = generateQuickFolio();
		final String finalReportPath = "";		//empty string directs output to System.out
		
		final Trial test = new Trial(testMode, inEnemyChoice, inEnemyLevel, inWeaponChoice, inWeaponMods, finalReportPath);
		test.runTrial();
		test.collateReports();
	}
	
	private static ModFolio generateQuickFolio()
	{
		return ModFolio.getEmptyFolio();
	}
}
