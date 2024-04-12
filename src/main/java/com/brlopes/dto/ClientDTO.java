package com.brlopes.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ClientDTO implements Serializable {
    
    private String name;
    private Integer age;
    
    public ClientDTO() {
    }
    
    public ClientDTO(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
