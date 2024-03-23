package com.finalproject.dio.finaldioproject.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CepDTO {
    private String code;
    private String city;
    private String state;
}
