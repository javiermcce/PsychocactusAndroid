package com.psychocactusproject.interaction.menu;

import android.graphics.Canvas;

import com.psychocactusproject.manager.engine.Point;

public interface MenuDisplay {

    public String getRoleName();

    public ContextMenu.MenuOption[] getMenuOptions();

    public void onOptionSelected(ContextMenu.MenuOption option);

    public ContextMenu getMenu();

    public boolean hasMenuOpen();

    public void openMenu();

    public void closeMenu();

    public void renderMenu(Canvas canvas);

    public Point getFatherPosition();

    public int getFatherWidth();

    public int getFatherHeight();

}
