package com.example.Cucufato2024.servicios.impl;

import com.example.Cucufato2024.dto.LineaPrestamoDto;
import com.example.Cucufato2024.dto.PrestamoDto;
import com.example.Cucufato2024.entidades.Material;
import com.example.Cucufato2024.servicios.ServicioCarrito;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devon Ravihansa on 8/29/2017.
 */
@Service
public class ServicioCarritoImpl implements ServicioCarrito {

    private static final String SESSION_KEY_SHOPPING_CART = "shoppingCart";

    public PrestamoDto getCart(HttpSession session){
        PrestamoDto cart = (PrestamoDto) session.getAttribute(SESSION_KEY_SHOPPING_CART);

        if(cart == null){
            cart = new PrestamoDto();
            cart.setFecha(Date.valueOf(LocalDate.now()));
            setCart(session, cart);
        }

        return cart;
    }

    public void setCart(HttpSession session, PrestamoDto cart){
        session.setAttribute(SESSION_KEY_SHOPPING_CART, cart);
    }

    public void removeCart(HttpSession session){
        session.removeAttribute(SESSION_KEY_SHOPPING_CART);
    }

    //Comprueba (y devuelve) si está el material en una linea de préstamo del carrito
    public LineaPrestamoDto getItem(Material product, HttpSession session){
        for (LineaPrestamoDto item : getCart(session).getLineaPrestamoDtos()){
            if(item.getMaterial().getId() == product.getId()){
                return item;
            }
        }
        return null;
    }

    public void addItem(LineaPrestamoDto lineaPrestamoDto, HttpSession session){
        //intento recuperar la línea de pedido que tiene el mismo material para no repetirla
        LineaPrestamoDto item = getItem(lineaPrestamoDto.getMaterial(), session);

        //Si ya hay una línea de pedido con ese material, incrementa el pedido
        //TODO comprobar que la suma de lo que hay en el carrito + lo que se pide no supera lo disponible
        if (item != null){

            //Le añado la cantidad que está pidiendo ahora a lo que ya había pedido
            item.setCantidadPedida(item.getCantidadPedida() + lineaPrestamoDto.getCantidadPedida());

        } else {

            //Si aún no se ha añadido ese material al carrito, lo añadimos
            PrestamoDto prestamo = getCart(session);  //Cojo el préstamo temporal de la sesión actual para leer los datos necesarios

            //A la línea de pedido que me pasan, le pongo como préstamo "padre" el préstamo del carrito y otros valores
            lineaPrestamoDto.setDevuelto(false);
            lineaPrestamoDto.setFecha(prestamo.getFecha());
            lineaPrestamoDto.setPrestamoDto(prestamo);
            getCart(session).getLineaPrestamoDtos().add(lineaPrestamoDto);
        }
    }

    @Override
    public PrestamoDto getItems(HttpSession session) {
        return getCart(session);
        //return getCart(session).getLineaPrestamoDtos();
    }
}