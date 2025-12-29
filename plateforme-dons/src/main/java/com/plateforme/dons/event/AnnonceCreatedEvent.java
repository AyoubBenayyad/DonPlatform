package com.plateforme.dons.event;

import com.plateforme.dons.entity.Annonce;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AnnonceCreatedEvent extends ApplicationEvent {

    private final Annonce annonce;

    public AnnonceCreatedEvent(Object source, Annonce annonce) {
        super(source);
        this.annonce = annonce;
    }
}
