package com.psychocactusproject.engine.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import org.w3c.dom.Text;

public class TextUtil {

    /**
     * Dibuja el texto indicado, centrándolo entre las posiciones indicadas
     * @param canvas Lienzo sobre el que se dibuja la línea
     * @param upLeft Esquina superior izquierda del espacio
     * @param downRight Esquina inferior derecha del espacio
     * @param text Texto que se desea dibujar
     * @param textPaint Instancia de paint en que se indica el formato del texto dibujado
     * @throws IllegalArgumentException Si en el espacio indicado no cabe el texto generado
     */
    public static void drawCenteredLine(Canvas canvas, Point upLeft, Point downRight, String text, Paint textPaint)
            throws IllegalArgumentException {
        // Ajusta parámetros
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int availableWidth = downRight.getX() - upLeft.getX();
        int textWidth = bounds.right - bounds.left;
        // Salta excepción si no se dispone de espacio para el texto
        if (textWidth > availableWidth) {
            throw new IllegalArgumentException("No hay suficiente espacio para el texto " +
                    "que se desea dibujar.");
        }
        // Tamaño de los bordes / 2, + offsetX
        int textPositionX = ((availableWidth - textWidth) / 2) + upLeft.getX() - 5;
        // Media de altura de las posiciones dadas como margen + offsetY
        int textPositionY = ((upLeft.getY() + downRight.getY()) / 2) + 20;
        // Dibujado del texto
        canvas.drawText(text, textPositionX, textPositionY, textPaint);
    }

    /**
     * Dibuja el mensaje indicado, centrándolo entre el tamaño indicado, y saltando líneas
     * cuando se acaba el espacio
     * @param canvas Lienzo sobre el que se dibuja la línea
     * @param message Mensaje dibujado, en que cada espacio es interpretado como
     *                separación entre palabras
     * @param width Espacio disponible para dibujar
     * @param textPaint Instancia de paint en que se indica el formato del texto dibujado
     */
    public static void drawCenteredText(Canvas canvas, String message, int width, Paint textPaint) {
        // Separa las palabras por las que está formado el mensaje
        String[] messageWords = message.split(" ");
        int wordIndex = 0;
        int lines = 0;
        // Para todas las palabras, permite dibujar, como máximo, tres palabras por línea
        while (wordIndex < messageWords.length) {
            int wordsPerLine = 3;
            StringBuilder line = new StringBuilder();
            for (int internalIndex = wordIndex;
                // Mientras que el índice de la linea sea menor que el límite por línea
                // y el índice sea menor que el total de palabras
                 internalIndex < wordIndex + wordsPerLine && internalIndex < messageWords.length;
                 internalIndex++) {
                line.append(messageWords[internalIndex]).append(" ");
            }
            Point startPoint = new Point(0, (lines * 80) + 80);
            Point endPoint = new Point(width, ((lines + 1) * 80) + 80);
            // Dibuja la línea generada
            textPaint.setTextSize(70);
            TextUtil.drawCenteredLine(canvas, startPoint, endPoint, line.toString(), textPaint);
            // Actualiza los índices
            wordIndex += wordsPerLine;
            lines++;
        }
    }
}
