package com.psychocactusproject.interaction.menu;

import android.graphics.Canvas;

public interface MenuDisplay {

    public String getRoleName();

    public ContextMenu.MenuOption[] getMenuOptions();

    public void onOptionSelected(ContextMenu.MenuOption option);

    public ContextMenu getMenu();

    public boolean hasMenuOpen();

    public void openMenu();

    public void closeMenu();

    public void renderMenu(Canvas canvas);

}
