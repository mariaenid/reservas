package com.reservas.org.rabbitmq;

import com.reservas.org.domain.Reserva;
import com.sun.jmx.snmp.Timestamp;

public class MessageConsenso {
    public long timeExecution;
    public  Integer pesoTransaction;
    public String hostname;
    public MessageQueue messageQueue;
}
