package com.example.Cucufato2024.servicios;

import com.example.Cucufato2024.dto.LineaPrestamoDto;
import com.example.Cucufato2024.dto.PrestamoDto;
import com.example.Cucufato2024.entidades.Material;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface ServicioCarrito {
    public PrestamoDto getCart(HttpSession session);

    public void setCart(HttpSession session, PrestamoDto cart);

    public void removeCart(HttpSession session);

    public LineaPrestamoDto getItem(Material product, HttpSession session);

    public void addItem(LineaPrestamoDto lineaPrestamoDto, HttpSession session);

    public PrestamoDto getItems(HttpSession session);
}
