package com.psychocactusproject.interaction.menu;

import android.graphics.Canvas;

import com.psychocactusproject.interaction.scripts.Clickable;

public interface MenuDisplay extends Clickable {

    public String getRoleName();

    public String[] getOptionNames();

    public void onOptionSelected(String option);

    public ContextMenu getMenu();

    public boolean isMenuOpen();

    public void openMenu();

    public void closeMenu();

    public void updateMenu();

    public void renderMenu(Canvas canvas);

    /**
     * Provisional: Devuelve true si alguna opción de las que existe en su menú está disponible
     * @return
     */
    public boolean isSomeOptionAvailable();

    /**
     * Provisional: El conjunto de estados en que se encuentra, permite ejecutar una acción
     * en el turno siguiente. Esto incluye estados como muerte, aturdimiento, detención, etc...
     * @return
     */
    public boolean isReadyForAction();

}
