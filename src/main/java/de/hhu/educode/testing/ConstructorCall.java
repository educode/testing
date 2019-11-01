package de.hhu.educode.testing;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConstructorCall {
    private final Class<?>[] parameterTypes;
    private final Object[] args;
}
