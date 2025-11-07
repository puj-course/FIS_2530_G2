package com.sis.Service;

import com.sis.Interface.IObservador;
import com.sis.Model.Enum.TipoEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VistaEnfermera implements IObservador {

    @Autowired
    private SmsService smsService;

    @Override
    public void actualizar(TipoEvento tipo, Object datos) {
        System.out.println("Evento recibido por la enfermera: " + tipo);
        System.out.println("Datos: " + datos);
        switch (tipo) {
            case PACIENTE_LLEGADA:
                smsService.enviarMensaje("Enfermera","Nuevo paciente llegó al hospital "+ datos);
                break;
            case NUEVO_TRIAGE:
                smsService.enviarMensaje("Enfermera","Se debe realizar un triage al nuevo paciente " + datos);
                break;
            default:
                smsService.enviarMensaje("Enfermera","El evento no es relevante para la enfermera.");
        }
    }
}