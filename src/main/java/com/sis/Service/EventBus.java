package com.sis.Service;

import com.sis.Interface.IObservador;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventBus {

    private final List<IObservador> observadores;

    public EventBus() {
        this.observadores = new ArrayList<>();
    }

    public void suscribir(IObservador observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void desuscribir(IObservador observador) {
        observadores.remove(observador);
    }

    public void publicar(String tipo, Object datos) {
        for (IObservador observador : observadores) {
            try {
                observador.actualizar(tipo, datos);
            } catch (Exception e) {
                System.err.println("Error al notificar a los observadores: " + e.getMessage());
            }
        }
    }
}