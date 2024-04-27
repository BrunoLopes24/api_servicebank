package com.brlopes.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * The ClientDTO class is a Data Transfer Object (DTO) class.
 * It is used to transfer data between processes.
 * As a DTO, it should only contain fields for data transfer and should not contain any business logic.
 * This class implements Serializable, which means its instances can be converted to a byte stream and back.
 * This is useful when you need to send objects across a network or store them in files permanently.
 *
 * The class uses Lombok annotations for automatic generation of getters, setters, and other common methods.
 */
@Getter
@Setter
@Data
public class ClientDTO implements Serializable {

    /**
     * The name of the client.
     */
    private String name;

    /**
     * The age of the client.
     */
    private Integer age;

    /**
     * Default constructor.
     * It is needed for the serialization and deserialization of objects.
     */
    public ClientDTO() {
    }

    /**
     * Constructor with all properties.
     * It is used to create a new instance of ClientDTO with provided name and age.
     *
     * @param name The name of the client.
     * @param age The age of the client.
     */
    public ClientDTO(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}