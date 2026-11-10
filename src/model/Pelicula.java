package model;

public class Pelicula {
    private String id;
    private String titulo;
    private int duracionMin;
    private String genero;
    private int clasificacionEdad;

    public Pelicula(String id, String titulo, int duracionMin, String genero, int clasificacionEdad) {
        this.id = id;
        this.titulo = titulo;
        this.duracionMin = duracionMin;
        this.genero = genero;
        this.clasificacionEdad = clasificacionEdad;
    }

    public boolean esAptaPara(int edad) {
        return edad >= clasificacionEdad;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getDuracionMin() { return duracionMin; }
    public void setDuracionMin(int duracionMin) { this.duracionMin = duracionMin; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public int getClasificacionEdad() { return clasificacionEdad; }
    public void setClasificacionEdad(int clasificacionEdad) { this.clasificacionEdad = clasificacionEdad; }

    @Override
    public String toString() {
        return titulo + " (" + genero + ", " + duracionMin + " min, +" + clasificacionEdad + ")";
    }
}