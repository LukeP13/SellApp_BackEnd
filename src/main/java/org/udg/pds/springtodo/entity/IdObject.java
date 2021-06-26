package org.udg.pds.springtodo.entity;

import lombok.Data;
import lombok.NonNull;

@Data
public class IdObject {

    public IdObject(@NonNull Long id) {
        this.id = id;
    }

    @NonNull
    private Long id;
}
