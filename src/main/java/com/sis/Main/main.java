package com.sis.Main;

import com.sis.Model.Enum.TipoEvento;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.ConfigurableApplicationContext;
import com.sis.Service.EventBus;
import com.sis.Service.VistaEnfermera;
import com.sis.Service.VistaPaciente;
import com.sis.Service.VistaDoctor;

@SpringBootApplication(scanBasePackages = "com.sis")
@EntityScan(basePackages = "com.sis.Model")
@EnableJpaRepositories(basePackages = "com.sis.Repository") // SUPER IMPORTANT TO HAVE IN MIND THIS TAGS!!!!!!

public class main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(main.class, args);
        EventBus eventBus = context.getBean(EventBus.class);
        VistaEnfermera vistaEnfermera = context.getBean(VistaEnfermera.class);
        VistaPaciente vistaPaciente = context.getBean(VistaPaciente.class);
        VistaDoctor vistaDoctor = context.getBean(VistaDoctor.class);
        eventBus.suscribir(vistaEnfermera);
        eventBus.suscribir(vistaPaciente);
        eventBus.suscribir(vistaDoctor);

        eventBus.publicar(TipoEvento.PACIENTE_LLEGADA, "Paciente: Juan Pérez - Dolor abdominal");
        eventBus.publicar(TipoEvento.NUEVO_TRIAGE, "Triage nivel 2 - Doctor en camino");
        eventBus.publicar(TipoEvento.DIAGNOSTICO_LISTO, "Diagnóstico: Gripe común");
        eventBus.publicar(TipoEvento.CITA_CONFIRMADA, "Cita programada para el lunes 10 AM");
        eventBus.publicar(TipoEvento.RESULTADO_DISPONIBLE, "Examen de sangre listo para revisión");
    }

}


