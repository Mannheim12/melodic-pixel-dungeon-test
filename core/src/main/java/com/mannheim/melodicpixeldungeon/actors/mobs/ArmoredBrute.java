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

package com.mannheim.melodicpixeldungeon.actors.mobs;

import com.mannheim.melodicpixeldungeon.Dungeon;
import com.mannheim.melodicpixeldungeon.actors.buffs.Buff;
import com.mannheim.melodicpixeldungeon.items.Generator;
import com.mannheim.melodicpixeldungeon.items.Item;
import com.mannheim.melodicpixeldungeon.items.armor.PlateArmor;
import com.mannheim.melodicpixeldungeon.items.armor.ScaleArmor;
import com.mannheim.melodicpixeldungeon.messages.Messages;
import com.mannheim.melodicpixeldungeon.sprites.CharSprite;
import com.mannheim.melodicpixeldungeon.sprites.ShieldedSprite;
import com.watabou.utils.Random;

public class ArmoredBrute extends Brute {

	{
		spriteClass = ShieldedSprite.class;
		
		//see rollToDropLoot
		loot = Generator.Category.ARMOR;
		lootChance = 1f;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(6, 10);
	}
	
	@Override
	protected void triggerEnrage () {
		Buff.affect(this, ArmoredRage.class).setShield(HT/2 + 1);
		if (Dungeon.level.heroFOV[pos]) {
			sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "enraged") );
		}
		spend( TICK );
		hasRaged = true;
	}
	
	@Override
	public Item createLoot() {
		if (Random.Int( 4 ) == 0) {
			return new PlateArmor().random();
		}
		return new ScaleArmor().random();
	}
	
	//similar to regular brute rate, but deteriorates much slower. 60 turns to death total.
	public static class ArmoredRage extends Brute.BruteRage {
		
		@Override
		public boolean act() {
			
			if (target.HP > 0){
				detach();
				return true;
			}
			
			absorbDamage( 1 );
			
			if (shielding() <= 0){
				target.die(null);
			}
			
			spend( 3*TICK );
			
			return true;
		}
		
	}
}
