package com.tadahtech.pub.menu.menus;

import com.tadahtech.pub.menu.Button;
import com.tadahtech.pub.menu.Menu;
import com.tadahtech.pub.tier.Tier;
import com.tadahtech.pub.tier.TierItem;

import java.util.List;

/**
 * @author Timothy Andis
 */
public class TierMenu extends Menu {

    private Tier tier;

    public TierMenu(Tier tier) {
        super("Tier Rewards");
        this.tier = tier;
    }

    @Override
    protected Button[] setUp() {
        Button[] buttons = new Button[27];
        List<TierItem> drops = tier.getDrops();
        for(int i = 0; i < drops.size(); i++) {
            buttons[i] = create(drops.get(i).getItemStack());
        }
        return buttons;
    }

}
