package ru.ism.myblogbackapp.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ism.myblogbackapp.model.dto.in.PostDtoIn;
import ru.ism.myblogbackapp.model.dto.in.PostDtoUpdate;
import ru.ism.myblogbackapp.model.dto.out.PostOutDto;
import ru.ism.myblogbackapp.model.dto.out.PostsOutDto;
import ru.ism.myblogbackapp.service.PostsService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostsService service;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public PostsOutDto searchPosts(
            @RequestParam(value = "search") String search,
            @RequestParam(value = "pageNumber") int page,
            @RequestParam(value = "pageSize") int size
    ) {
        return service.searchPosts(search, page, size);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public PostOutDto getPostById(@PathVariable("id") Long id) {
        return service.getPost(id);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostOutDto> createPost(@RequestBody PostDtoIn postDtoIn) {
        PostOutDto postOutDto = service.createPost(postDtoIn);
        return ResponseEntity.status(HttpStatus.CREATED).body(postOutDto);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostOutDto> updatePost(
            @PathVariable("id") Long id,
            @RequestBody PostDtoUpdate postDtoUpdate) {
        PostOutDto postOutDto = service.updatePost(id, postDtoUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(postOutDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable("id") Long id) {
        service.deletePost(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
