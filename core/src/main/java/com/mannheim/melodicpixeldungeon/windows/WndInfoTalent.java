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

package com.mannheim.melodicpixeldungeon.windows;

import com.mannheim.melodicpixeldungeon.Dungeon;
import com.mannheim.melodicpixeldungeon.actors.hero.Talent;
import com.mannheim.melodicpixeldungeon.messages.Messages;
import com.mannheim.melodicpixeldungeon.scenes.PixelScene;
import com.mannheim.melodicpixeldungeon.ui.Icons;
import com.mannheim.melodicpixeldungeon.ui.RedButton;
import com.mannheim.melodicpixeldungeon.ui.RenderedTextBlock;
import com.mannheim.melodicpixeldungeon.ui.TalentIcon;
import com.mannheim.melodicpixeldungeon.ui.Window;
import com.watabou.utils.Callback;

public class WndInfoTalent extends Window {

	private static final float GAP	= 2;

	private static final int WIDTH = 120;

	public WndInfoTalent(Talent talent, int points, TalentButtonCallback buttonCallback){
		super();

		IconTitle titlebar = new IconTitle();

		titlebar.icon( new TalentIcon( talent ) );
		String title = Messages.titleCase(talent.title());
		if (points > 0){
			title += " +" + points;
		}
		titlebar.label( title, Window.TITLE_COLOR );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		boolean metaDesc = (buttonCallback != null && buttonCallback.metamorphDesc()) ||
				(Dungeon.hero != null && Dungeon.hero.metamorphedTalents.containsValue(talent));

		RenderedTextBlock txtInfo = PixelScene.renderTextBlock(talent.desc(metaDesc), 6);
		txtInfo.maxWidth(WIDTH);
		txtInfo.setPos(titlebar.left(), titlebar.bottom() + 2*GAP);
		add( txtInfo );
		resize( WIDTH, (int)(txtInfo.bottom() + GAP) );

		if (buttonCallback != null) {
			RedButton button = new RedButton( buttonCallback.prompt() ) {
				@Override
				protected void onClick() {
					super.onClick();
					hide();
					buttonCallback.call();
				}
			};
			button.icon(Icons.get(Icons.TALENT));
			button.setRect(0, txtInfo.bottom() + 2*GAP, WIDTH, 18);
			add(button);
			resize( WIDTH, (int)button.bottom()+1 );
		}

	}

	public static abstract class TalentButtonCallback implements Callback {

		public abstract String prompt();

		public boolean metamorphDesc(){
			return false;
		}

	}

}