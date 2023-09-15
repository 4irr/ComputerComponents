package com.example.gurbocoursework.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Accessories {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accessoriesID;
    @Size(min=5, message = "Минимальная длина значения - 5 символов")
    @Pattern(regexp = "^[^0-9]*$", message = "Поле может содержать только символы")
    private String type;
    @Size(min=5, message = "Минимальная длина значения - 5 символов")
    private String name;
    private String cell;
    @Min(value = 0, message = "Не должно быть меньше нуля")
    private float price;
    @Min(value = 0, message = "Не должно быть меньше нуля")
    private float weight;
}
