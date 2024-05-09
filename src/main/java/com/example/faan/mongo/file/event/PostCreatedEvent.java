package com.example.faan.mongo.file.event;

import com.example.faan.mongo.modelos.dto.SavePost;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class PostCreatedEvent extends ApplicationEvent {

    private final SavePost post;

    public PostCreatedEvent(SavePost post) {
        super(post);
        this.post = post;
    }

}
