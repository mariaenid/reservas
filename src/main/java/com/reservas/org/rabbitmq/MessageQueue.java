package com.reservas.org.rabbitmq;


import com.reservas.org.domain.Reserva;

public class MessageQueue {

    public TypeMessage message_type;
    public TypeMessage status_Reserva;
    public String hostname;
    public Integer peso;
    // public String id_Reserva;
    public Reserva message_body;
    public Integer posicion_cola;
    // public String message_body;
}
