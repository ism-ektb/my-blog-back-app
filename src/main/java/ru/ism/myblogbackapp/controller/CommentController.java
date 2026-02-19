package ru.ism.myblogbackapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ism.myblogbackapp.model.dto.in.CommentDtoIn;
import ru.ism.myblogbackapp.model.dto.in.CommentDtoUpdate;
import ru.ism.myblogbackapp.model.dto.out.CommentDtoOut;
import ru.ism.myblogbackapp.service.PostsService;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{id}/comments")
@RequiredArgsConstructor
@Tag(name = "Контроллер комментариев")
public class CommentController {

    private final PostsService postsService;

    @GetMapping
    @Operation(summary = "Получение списка комментариев к посту с номером равным id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Список комментариев отправлен"),
            @ApiResponse(responseCode = "401", description = "неверный ввод данных"),
            @ApiResponse(responseCode = "404", description = "Пост с введенным id отсутствует")})
    public List<CommentDtoOut> getCommentsByPostId(@PathVariable("id") long id) {
        return postsService.getCommentsByPostId(id);
    }

    @GetMapping("/{commentId}")
    @Operation(summary = "Получение комментария с номером commentId к посту с номером id ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Пост обновлен"),
            @ApiResponse(responseCode = "401", description = "неверный ввод данных"),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден по введенным параметрам")})
    public CommentDtoOut getCommentById(@PathVariable("id") long id,
                                        @PathVariable("commentId") long commentId) {
        return postsService.getCommentById(id, commentId);
    }

    @PostMapping
    @Operation(summary = "Добавление комментария к посту с номером id ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Пост обновлен"),
            @ApiResponse(responseCode = "401", description = "неверный ввод данных"),
            @ApiResponse(responseCode = "404", description = "Пост не найден по введенным параметрам")})
    public CommentDtoOut createComment(@PathVariable("id") long id,
                                       @RequestBody CommentDtoIn commentDtoIn) {
        return postsService.addComment(id, commentDtoIn);
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "Обновление комментария c номером commentId к посту с номером id ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Пост обновлен"),
            @ApiResponse(responseCode = "401", description = "неверный ввод данных"),
            @ApiResponse(responseCode = "404", description = "Комментаний не найден по введенным параметрам")})
    public CommentDtoOut updateComment(@PathVariable("id") long id,
                                       @PathVariable("commentId") long commentId,
                                       @RequestBody CommentDtoUpdate commentDtoUpdate) {
        return postsService.updateComment(id, commentId, commentDtoUpdate);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Удаление комментария с номером commentId к посту с номером id ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Пост обновлен"),
            @ApiResponse(responseCode = "401", description = "неверный ввод данных"),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден по введенным параметрам")})
    public void deleteComment(
            @PathVariable("id") long id,
            @PathVariable("commentId") long commentId) {
        postsService.deleteComment(id, commentId);
    }
}
