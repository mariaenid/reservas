package com.reservas.org.rabbitmq;

import javax.persistence.Enumerated;

public enum  TypeMessage {
    REGISTER,
    CONFIRM,
    NOTIFY,
    N0T_ALLOWED,
    ALLOWED
}

