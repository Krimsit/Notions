package com.example.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Основная модель данных. Хранит информацию о сущности заметка
 */
public class Note implements Serializable{
    /**
     * Заголовок заметки
     */
    private String title;
    /**
     * Наполнение заметки
     */
    private String text;
    /**
     * Идентификатор заметки
     */
    private Integer id;
    /**
     * Дата создания
     */
    private LocalDateTime createdOn;

    /**
     * Get метод
     * @return возвращает дату создания заметки
     */
    public LocalDateTime getCreatedOn(){
        return createdOn;
    }

    /**
     * Set метод. Присваивает дату создания заметки
     * @param createdOn дата создания
     */
    public void setCreatedOn(LocalDateTime createdOn){
        this.createdOn = createdOn;
    }

    /**
     * Get метод.
     * @return возвращает заголовок заметки
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set метод. Присваивает заголовок заметки
     * @param title название заметки
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get метод.
     * @return возращает наполнение заметки
     */
    public String getText() {
        return text;
    }

    /**
     * Set метод. Присваивает наполнение заметки
     * @param text текст заметки
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get метод
     * @return возвращает идентификатор заметки
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set метод. Присваивает идентификатор заметки
     * @param id - идентификатор
     */
    public void setId(Integer id) {
        this.id = id;
    }

}
