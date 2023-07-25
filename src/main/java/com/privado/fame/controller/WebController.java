package com.privado.fame.controller;

import com.privado.fame.model.DetalleOrden;
import com.privado.fame.model.Orden;
import com.privado.fame.model.Producto;
import com.privado.fame.service.ProductoService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web")
public class WebController {

    private final Logger log =LoggerFactory.getLogger(WebController.class);
    @Autowired
    private ProductoService productoService;

    List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

    Orden orden = new Orden();

    @GetMapping("")
    public String home(){
        return "web/home";
    }


    @GetMapping("/tienda")
    public String tienda(Model model) {
        //List<Producto> productos = productoService.findAll();
        List<Producto> productos = productoService.findAll().stream().filter(p -> p.getEstado().contains("Activo")).collect(Collectors.toList());

        model.addAttribute("productos", productos);

        return "web/tienda";
    }

    @GetMapping("/producto/{id}")
    private String productoInfo(@PathVariable Integer id, Model  model){
        log.info("Id producto enviado {}", id);
        Producto producto = new Producto();
        Optional<Producto> productoOptional = productoService.get(id);
        producto = productoOptional.get();

        model.addAttribute("producto", producto);

        String categoria = producto.getCategoria();
        String empresa = producto.getEmpresa();
        List<Producto> productosRecom = productoService.findAll().stream().filter(p -> p.getCategoria().contains(categoria) && p.getEmpresa().contains(empresa)).collect(Collectors.toList());
        log.info("Producto Añadido: {}", producto.getCategoria());

        model.addAttribute("productosRecom", productosRecom);
        return "web/producto";
    }

    @GetMapping("carrito")
    private String carrito (Model model){
        double sumaTotal = 0;
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        sumaTotal=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
        return ("web/carrito");
    }

    @PostMapping("cart")
    private String addCart(@RequestParam Integer id,  Model model){
        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

        Optional<Producto> optionalProducto = productoService.get(id);
        log.info("Producto Añadido: {}",  optionalProducto.get());
        model.addAttribute("producto", producto);

        producto=optionalProducto.get();

        detalleOrden.setPrecio(producto.getPrecio());
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio());
        detalleOrden.setProductos(producto);

        detalles.add(detalleOrden);

        sumaTotal=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();

        orden.setTotal(sumaTotal);


        return productoInfo(id, model);
    }

    @GetMapping("armada")
    public String armada(Model model) {
        //List<Producto> productos = productoService.findAll();
        List<Producto> productos = productoService.findAll().stream().filter(p -> p.getEmpresa().contains("Armada") && p.getEstado().contains("Activo")).collect(Collectors.toList());

        model.addAttribute("productos", productos);

        return "web/tienda";
    }

    @GetMapping("aerea")
    public String fuerzaAerea(Model model) {
        //List<Producto> productos = productoService.findAll();
        List<Producto> productos = productoService.findAll().stream().filter(p -> p.getEmpresa().contains("Fuerza Aerea") && p.getEstado().contains("Activo")).collect(Collectors.toList());

        model.addAttribute("productos", productos);

        return "web/tienda";
    }

    @GetMapping("terrestre")
    public String fuerzaTerrestre(Model model) {
        //List<Producto> productos = productoService.findAll();
        List<Producto> productos = productoService.findAll().stream().filter(p -> p.getEmpresa().contains("Fuerza Terrestre") && p.getEstado().contains("Activo")).collect(Collectors.toList());

        model.addAttribute("productos", productos);

        return "web/tienda";
    }

}

