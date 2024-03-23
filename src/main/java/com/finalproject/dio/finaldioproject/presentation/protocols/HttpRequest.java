package com.finalproject.dio.finaldioproject.presentation.protocols;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpRequest<T> {
    T body;
}
