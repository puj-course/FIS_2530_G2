package com.sis.Service;

import com.sis.Interface.IObservador;
import com.sis.Model.Enum.TipoEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VistaDoctor implements IObservador {

    @Autowired
    private SmsService smsService;

    @Override
    public void actualizar(TipoEvento tipo, Object datos) {
        System.out.println("Evento recibido por el doctor: " + tipo);
        System.out.println("Datos: " + datos);
        switch (tipo) {
            case NUEVO_TRIAGE:
                smsService.enviarMensaje("Doctor","Nuevo Triage registrado" + datos);
                break;
            case DIAGNOSTICO_LISTO:
                smsService.enviarMensaje("Doctor","Diagnostico listo para revisión" + datos);
                break;
            case CITA_CONFIRMADA:
                smsService.enviarMensaje("Doctor","Cita confirmada para un paciente" + datos);
                break;
            default:
                smsService.enviarMensaje("Doctor","El evento no es relevante para el doctor.");
        }
    }
}