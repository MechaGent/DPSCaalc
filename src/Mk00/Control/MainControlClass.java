package Mk00.Control;

import Mk00.Control.Trial.TrialModes;
import Mk00.Control.Manifests.EnemyManifest.Enemies;
import Mk00.Control.Manifests.ModBuildManifest.PartialBuilds;
import Mk00.Control.Manifests.WeaponManifest.Weapons;
import Mk00.Weapons.ModFolio;

public class MainControlClass
{
	public static void main(String[] args)
	{
		test_TrialSystems();
	}
	
	private static void test_TrialSystems()
	{
		final TrialModes trialMode = TrialModes.findDPS_Sustained;
		final Weapons weapon = Weapons.Grakata;
		final ModFolio mods = PartialBuilds.Rifle_CritProcHybrid.getPartialBuild();
		final Enemies enemy = Enemies.Infested_Boiler;
		final int enemyLevel = 50;
		
		final Trial test = Trial.initTrial(trialMode, weapon, mods, enemy, enemyLevel);
		test.runTrial();
	}
}
