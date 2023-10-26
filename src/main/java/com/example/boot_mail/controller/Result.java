package com.example.boot_mail.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Result implements Serializable {
    String flag;
    String message;
}
