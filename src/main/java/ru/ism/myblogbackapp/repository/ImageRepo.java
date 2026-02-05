package ru.ism.myblogbackapp.repository;

import java.util.Optional;

public interface ImageRepo {
    /**
     * Обновить изображение для поста
     *
     * @param postId
     * @param image
     */
    void updateImage(long postId, byte[] image);

    /**
     * Получить изображение для поста
     *
     * @param postId
     * @return
     */
    byte[] getImage(long postId);
}
