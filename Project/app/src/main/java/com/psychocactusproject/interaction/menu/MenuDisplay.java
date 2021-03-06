package com.psychocactusproject.interaction.menu;

import android.graphics.Canvas;

import com.psychocactusproject.interaction.scripts.Clickable;

public interface MenuDisplay extends Clickable {

    public String getRoleName();

    public String[] getOptionNames();

    public ContextMenu.MenuOption[] getMenuOptions();

    public void onOptionSelected(String option);

    public ContextMenu getMenu();

    public boolean hasMenuOpen();

    public void openMenu();

    public void closeMenu();

    public void updateMenu();

    public void renderMenu(Canvas canvas);

}
