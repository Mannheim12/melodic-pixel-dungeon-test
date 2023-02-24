/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.mannheim.melodicpixeldungeon.actors.hero;

import com.mannheim.melodicpixeldungeon.Assets;
import com.mannheim.melodicpixeldungeon.Badges;
import com.mannheim.melodicpixeldungeon.Challenges;
import com.mannheim.melodicpixeldungeon.Dungeon;
import com.mannheim.melodicpixeldungeon.QuickSlot;
import com.mannheim.melodicpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.mannheim.melodicpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.mannheim.melodicpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.mannheim.melodicpixeldungeon.actors.hero.abilities.huntress.SpectralBlades;
import com.mannheim.melodicpixeldungeon.actors.hero.abilities.mage.WildMagic;
import com.mannheim.melodicpixeldungeon.actors.hero.abilities.mage.WarpBeacon;
import com.mannheim.melodicpixeldungeon.actors.hero.abilities.mage.ElementalBlast;
import com.mannheim.melodicpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.mannheim.melodicpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.mannheim.melodicpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.mannheim.melodicpixeldungeon.actors.hero.abilities.warrior.HeroicLeap;
import com.mannheim.melodicpixeldungeon.actors.hero.abilities.warrior.Shockwave;
import com.mannheim.melodicpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.mannheim.melodicpixeldungeon.items.BrokenSeal;
import com.mannheim.melodicpixeldungeon.items.Item;
import com.mannheim.melodicpixeldungeon.items.MerchantsBeacon;
import com.mannheim.melodicpixeldungeon.items.TengusMask;
import com.mannheim.melodicpixeldungeon.items.Waterskin;
import com.mannheim.melodicpixeldungeon.items.armor.ClothArmor;
import com.mannheim.melodicpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.mannheim.melodicpixeldungeon.items.artifacts.CapeOfThorns;
import com.mannheim.melodicpixeldungeon.items.artifacts.CloakOfShadows;
import com.mannheim.melodicpixeldungeon.items.bags.VelvetPouch;
import com.mannheim.melodicpixeldungeon.items.food.Food;
import com.mannheim.melodicpixeldungeon.items.potions.PotionOfHealing;
import com.mannheim.melodicpixeldungeon.items.potions.PotionOfInvisibility;
import com.mannheim.melodicpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.mannheim.melodicpixeldungeon.items.potions.PotionOfMindVision;
import com.mannheim.melodicpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.mannheim.melodicpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.mannheim.melodicpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.mannheim.melodicpixeldungeon.items.scrolls.ScrollOfRage;
import com.mannheim.melodicpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.mannheim.melodicpixeldungeon.items.scrolls.exotic.ScrollOfDivination;
import com.mannheim.melodicpixeldungeon.items.songs.SongOfLullaby;
import com.mannheim.melodicpixeldungeon.items.wands.WandOfMagicMissile;
import com.mannheim.melodicpixeldungeon.items.weapon.SpiritBow;
import com.mannheim.melodicpixeldungeon.items.weapon.melee.Dagger;
import com.mannheim.melodicpixeldungeon.items.weapon.melee.Gloves;
import com.mannheim.melodicpixeldungeon.items.weapon.melee.MagesStaff;
import com.mannheim.melodicpixeldungeon.items.weapon.melee.WornShortsword;
import com.mannheim.melodicpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.mannheim.melodicpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.mannheim.melodicpixeldungeon.messages.Messages;
import com.watabou.utils.DeviceCompat;

import com.mannheim.melodicpixeldungeon.items.weapon.instrument.Flute; //TODO remove this after testing

public enum HeroClass {

	WARRIOR( HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR ),
	MAGE( HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK ),
	ROGUE( HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER ),
	HUNTRESS( HeroSubClass.SNIPER, HeroSubClass.WARDEN );

	private HeroSubClass[] subClasses;

	HeroClass( HeroSubClass...subClasses ) {
		this.subClasses = subClasses;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;
		Talent.initClassTalents(hero);

		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor)i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();

		Waterskin waterskin = new Waterskin();
		waterskin.collect();

		new ScrollOfIdentify().identify();

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;
		}

		for (int s = 0; s < QuickSlot.SIZE; s++){
			if (Dungeon.quickslot.getItem(s) == null){
				Dungeon.quickslot.setSlot(s, waterskin);
				break;
			}
		}

		//TODO remove this stuff after testing
		//new Flute().collect();
		new TengusMask().collect();
		new ScrollOfDivination().collect();
		//new SongOfLullaby().collect();
		new CapeOfThorns().collect();
		new ScrollOfLullaby().collect();
	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.quantity(3).collect();
		Dungeon.quickslot.setSlot(0, stones);

		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
		}

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;

		staff = new MagesStaff(new WandOfMagicMissile());

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollOfUpgrade().identify();
		new PotionOfLiquidFlame().identify();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.artifact = cloak).identify();
		hero.belongings.artifact.activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, knives);

		new ScrollOfMagicMapping().identify();
		new PotionOfInvisibility().identify();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new Gloves()).identify();
		SpiritBow bow = new SpiritBow();
		bow.identify().collect();

		Dungeon.quickslot.setSlot(0, bow);

		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();
	}

	public String title() {
		return Messages.get(HeroClass.class, name());
	}

	public String desc(){
		return Messages.get(HeroClass.class, name()+"_desc");
	}

	public String shortDesc(){
		return Messages.get(HeroClass.class, name()+"_desc_short");
	}

	public HeroSubClass[] subClasses() {
		return subClasses;
	}

	public ArmorAbility[] armorAbilities(){
		switch (this) {
			case WARRIOR: default:
				return new ArmorAbility[]{new HeroicLeap(), new Shockwave(), new Endure()};
			case MAGE:
				return new ArmorAbility[]{new ElementalBlast(), new WildMagic(), new WarpBeacon()};
			case ROGUE:
				return new ArmorAbility[]{new SmokeBomb(), new DeathMark(), new ShadowClone()};
			case HUNTRESS:
				return new ArmorAbility[]{new SpectralBlades(), new NaturesPower(), new SpiritHawk()};
		}
	}

	public String spritesheet() {
		switch (this) {
			case WARRIOR: default:
				return Assets.Sprites.WARRIOR;
			case MAGE:
				return Assets.Sprites.MAGE;
			case ROGUE:
				return Assets.Sprites.ROGUE;
			case HUNTRESS:
				return Assets.Sprites.HUNTRESS;
		}
	}

	public String splashArt(){
		switch (this) {
			case WARRIOR: default:
				return Assets.Splashes.WARRIOR;
			case MAGE:
				return Assets.Splashes.MAGE;
			case ROGUE:
				return Assets.Splashes.ROGUE;
			case HUNTRESS:
				return Assets.Splashes.HUNTRESS;
		}
	}
	
	public boolean isUnlocked(){
		//always unlock on debug builds
		if (DeviceCompat.isDebug()) return true;
		
		switch (this){
			case WARRIOR: default:
				return true;
			case MAGE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
			case ROGUE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
			case HUNTRESS:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
		}
	}
	
	public String unlockMsg() {
		return shortDesc() + "\n\n" + Messages.get(HeroClass.class, name()+"_unlock");
	}

}