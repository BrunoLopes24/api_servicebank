package com.brlopes.dto;

/**
 * The LoginResponseDTO class is a record class in Java.
 * It is used to store and retrieve the token details for a successful login.
 * This class is typically used to transfer data between processes.
 * As a record, it has a concise syntax for declaring simple, immutable data carrier classes and
 * it automatically provides several methods such as equals, hashCode, toString, getters etc.
 *
 * @param token This is the token generated after a successful login.
 */
public record LoginResponseDTO(String token) {

}