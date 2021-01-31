package com.psychocactusproject.interaction.menu;

public interface MenuDisplay {

    public String getRoleName();

    public ContextMenu.MenuOption[] getMenuOptions();

    public void onOptionSelected(ContextMenu.MenuOption option);

}
