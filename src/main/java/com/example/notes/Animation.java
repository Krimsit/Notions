package com.example.notes;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class Animation {
    /**
     * Создаёт плавную анимацию при наведении на объект
     *
     * @param control объект
     */
    public static void ScaleButtonAnimation(Node control) {
        // Анимация при наведении курсора
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(100), control);
        scaleIn.setByX(1);
        scaleIn.setByY(1);
        scaleIn.setToX(1.1);
        scaleIn.setToY(1.1);
        control.setOnMouseEntered(e -> scaleIn.play());

        // Анимация, когда курсор убирают
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(100), control);
        scaleOut.setByX(1.1);
        scaleOut.setByY(1.1);
        scaleOut.setToX(1);
        scaleOut.setToY(1);
        control.setOnMouseExited(e -> scaleOut.play());
    }

    /**
     * Создаёт тултип для объекта
     *
     * @param control объект
     * @param text    текст тултипа
     */
    public static void CreateTooltip(Node control, String text) {
        Tooltip tooltip = new Tooltip(text);

        // Через сколько покажется тултип
        tooltip.setShowDelay(Duration.millis(100));

        Tooltip.install(control, tooltip);
    }
}
