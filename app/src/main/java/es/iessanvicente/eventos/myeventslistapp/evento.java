package es.iessanvicente.eventos.myeventslistapp;

import java.io.Serializable;

public class evento implements Serializable
{
    /*
    * DECLARACION DE VARIBALES CON SUS GET Y SET
    * */
    int id;
    String nombre;
    String direccion;
    String fecha_hora;
    String descripcion;
    int activo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    /*
    * CONSTRUCTOR CON ARGUMENTOS DE LA CLASE CON INICIALIZACIÓN DE DATOS
    * */
    public evento(String nombre, String direccion, String fecha_hora, String descripcion, int activo)
    {
        this.nombre = nombre;
        this.direccion = direccion;
        this.fecha_hora = fecha_hora;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public evento(int id, String nombre, String direccion, String fecha_hora, String descripcion, int activo)
    {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.fecha_hora = fecha_hora;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public String DatosEvento()
    {
        return getId()+". "+ getNombre()+" \n "+getFecha_hora();
    }
}
