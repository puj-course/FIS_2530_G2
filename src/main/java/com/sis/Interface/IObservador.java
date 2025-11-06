package com.sis.Interface;

import com.sis.Model.Enum.TipoEvento;

public interface IObservador {
    void actualizar(TipoEvento tipo, Object datos);
}