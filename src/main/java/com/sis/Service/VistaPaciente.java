package com.sis.Service;

import com.sis.Interface.IObservador;
import com.sis.Model.Enum.TipoEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VistaPaciente implements IObservador {

    @Autowired
    private SmsService smsService;

    @Override
    public void actualizar(TipoEvento tipo, Object datos) {
        System.out.println("Evento recibido por el paciente: " + tipo);
        System.out.println("Datos: " + datos);
        switch (tipo){
            case CITA_CONFIRMADA:
                smsService.enviarMensaje("Paciente","Su cita ha sido confirmada " + datos);
                break;
            case DIAGNOSTICO_LISTO:
                smsService.enviarMensaje("Paciente","Su diagnostico está listo " + datos);
                break;
            case RESULTADO_DISPONIBLE:
                smsService.enviarMensaje("Paciente","Ya puede consultar los resultados de su exámen: " + datos);
                break;
            default:
                smsService.enviarMensaje("Paciente","El evento no es relevante para el paciente.");
        }
    }
}
