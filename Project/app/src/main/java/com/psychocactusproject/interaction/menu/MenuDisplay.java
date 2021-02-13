package com.psychocactusproject.interaction.menu;

import android.graphics.Canvas;

import com.psychocactusproject.interaction.scripts.Clickable;
import com.psychocactusproject.manager.engine.Point;

public interface MenuDisplay extends Clickable {

    public String getRoleName();

    public ContextMenu.MenuOption[] getMenuOptions();

    public void onOptionSelected(ContextMenu.MenuOption option);

    public ContextMenu getMenu();

    public boolean hasMenuOpen();

    public void openMenu();

    public void closeMenu();

    public void renderMenu(Canvas canvas);

}
