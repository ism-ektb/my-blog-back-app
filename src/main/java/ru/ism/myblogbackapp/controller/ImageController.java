package ru.ism.myblogbackapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ism.myblogbackapp.service.PostsService;

@RestController
@RequestMapping("/api/posts/{id}/image")
@RequiredArgsConstructor
@Tag(name = "Контроллер изображений")
public class ImageController {

    private final PostsService postsService;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление изображения к посту с номером равным id")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Изображение обновлено или добавлено"),
            @ApiResponse(responseCode = "401", description = "неверный ввод данных"),
            @ApiResponse(responseCode = "404", description = "Пост с введенным id отсутствует")})
    public ResponseEntity<Void> uploadAvatar(@PathVariable("id") Long id,
                                             @RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        postsService.uploadImage(id, file);
        return ResponseEntity.status(201).build();
    }

    @GetMapping(produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "Получение изображения к посту с номером равным id")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Изображение обновлено или добавлено"),
            @ApiResponse(responseCode = "401", description = "неверный ввод данных"),
            @ApiResponse(responseCode = "404", description = "Пост с введенным id отсутствует")})
    public ResponseEntity<byte[]> getImage(@PathVariable("id") long id) {

        byte[] bytes = postsService.getImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(bytes);
    }
}
