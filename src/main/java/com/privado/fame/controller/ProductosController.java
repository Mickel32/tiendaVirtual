package com.privado.fame.controller;

import com.privado.fame.model.Producto;
import com.privado.fame.model.Usuario;
import com.privado.fame.service.ProductoService;
import com.privado.fame.service.UploadFileService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/administrador")
public class ProductosController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductosController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UploadFileService upload;

    @GetMapping("")
    public String inicio(){
        return "administrador/inicio";
    }

    @GetMapping("productos")
    public String productos(Model model){
        model.addAttribute("productos", productoService.findAll());
        return "administrador/productos";
    }

    @GetMapping("crearProd")
    public String crearProducto(){
        return "administrador/crearProd";
    }

    @PostMapping ("save")
    public String save (Producto producto,@RequestParam("img") MultipartFile file) throws IOException {
        LOGGER.info("Este es el producto {}",producto);
        Usuario  u = new Usuario(1, "","","","","","","");
        producto.setUsuario(u);

        //imagen

        if (producto.getId()==null){
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }else{

        }


        producto.setEstado("Activo");
        productoService.save(producto);
        return "redirect:/administrador/productos";
    }

    @GetMapping("editarProd/{id}")
    public String editarProducto (@PathVariable Integer id, Model model){
        Producto producto = new  Producto();
        Optional<Producto> optionalProducto= productoService.get(id);
        producto=optionalProducto.get();

        LOGGER.info("Producto buscado: {}", producto);
        model.addAttribute("producto", producto);
        return "administrador/editarProd";
    }

    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
        Producto product = new Producto();
        product=productoService.get(producto.getId()).get();
        //imagen

        if (file.isEmpty()){

            producto.setImagen(product.getImagen());
        }else {
            if (!product.getImagen().equals("default.jpg")){
                upload.deleteImage(product.getImagen());
            }
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        producto.setUsuario(product.getUsuario());
        producto.setEstado("Activo");
        productoService.update(producto);
        return "redirect:/administrador/productos";
    }
    @GetMapping("/inactivarProd/{id}")
    public String inactivar(@PathVariable Integer id, Model model){
        Producto producto = new  Producto();
        Optional<Producto> optionalProducto= productoService.get(id);
        producto=optionalProducto.get();

        LOGGER.info("Producto buscado: {}", producto);
        model.addAttribute("producto", producto);
        producto.setEstado("Inactivo");
        productoService.update(producto);
      return "redirect:/administrador/productos";
    }

    @GetMapping("/activarProd/{id}")
    public String activar(@PathVariable Integer id, Model model){
        Producto producto = new  Producto();
        Optional<Producto> optionalProducto= productoService.get(id);
        producto=optionalProducto.get();

        LOGGER.info("Producto buscado: {}", producto);
        model.addAttribute("producto", producto);
        producto.setEstado("Activo");
        productoService.update(producto);
        return "redirect:/administrador/productos";
    }



}
