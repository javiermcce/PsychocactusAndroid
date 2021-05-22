package com.psychocactusproject.interaction.scripts;

import com.psychocactusproject.graphics.interfaces.Dimensions;
import com.psychocactusproject.engine.util.Hitbox;

public interface Clickable extends Dimensions {

    /**
     * Nuevo planteamiento, provisional: Ahora también devuelve la confirmación. True si se
     * ejecuta, false si no se ejecuta nada
     * @param index
     * @return
     */
    public void executeClick(int index);

    public Hitbox[] getHitboxes();

    public boolean isAvailable(int index);

    public void enableClickable(int index);

    public void disableClickable(int index);
}
