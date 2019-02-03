package com.reservas.org.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.reservas.org.domain.Reserva;
import com.reservas.org.rabbitmq.*;
import com.reservas.org.repository.ReservaRepository;
import com.reservas.org.web.rest.errors.BadRequestAlertException;
import com.reservas.org.web.rest.util.HeaderUtil;
import com.reservas.org.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Reserva.
 */
@RestController
@RequestMapping("/api")
@Import(RabbitService.class)
public class ReservaResource {

    @Value("${consenso.myhostname}")
    private String myhostname;

    @Value("${consenso.peso}")
    private Integer peso;

    @Value("${consenso.timeout}")
    private long timeout;

    ArrayList<Integer> pesos_transacciones = new ArrayList<>();
    List<MessageConsenso> historial_reservas = new ArrayList<>();

    @Autowired
    private RabbitService rabbitService;

    private final Logger log = LoggerFactory.getLogger(ReservaResource.class);

    private static final String ENTITY_NAME = "reserva";

    private final ReservaRepository reservaRepository;

    public ReservaResource(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    /**
     * POST  /reservas : Create a new reserva.
     *
     * @param reserva the reserva to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reserva, or with status 400 (Bad Request) if the reserva has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reservas")
    @Timed
    public ResponseEntity<Reserva> createReserva(@RequestBody Reserva reserva) throws URISyntaxException {
    log.debug("REST request to save Reserva : {}", reserva);
        MessageQueue messageQueue = new MessageQueue();
        messageQueue.peso = 0;
        messageQueue.message_type = TypeMessage.NOTIFY;
        messageQueue.hostname = myhostname;
        messageQueue.message_body = reserva;
        messageQueue.peso += Integer.parseInt(myhostname);
        pesos_transacciones.add(messageQueue.peso);
        MessageConsenso messageConsenso = new MessageConsenso();
        messageConsenso.hostname = myhostname;
        messageConsenso.pesoTransaction = messageQueue.peso;
        messageConsenso.messageQueue = messageQueue;
        DateTime date = DateTime.now();
        messageConsenso.timeExecution = date.getMillis();
        historial_reservas.add(messageConsenso);

        log.info("ARrar {}", pesos_transacciones);

        messageQueue.posicion_cola = pesos_transacciones.size()-1; // encuentra la posicion en la cola para luego contabilizar si todos han confirmado

        if(reservaRepository.findByRestauranteIdAndSpotIdAndFecha(reserva.getRestaurante().getId(),
            reserva.getSpot().getId(),
            reserva.getFecha()) != null) {
            throw new BadRequestAlertException("La reserva ya existe", ENTITY_NAME, "idexists");

        }

        try {
            rabbitService.send_message(messageQueue);
        } catch (JsonProcessingException e) {
            log.info("No se pudo enviar por la cola {}", messageQueue);
            e.printStackTrace();
        }

        if (reserva.getId() != null) {
            throw new BadRequestAlertException("A new reserva cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Reserva result = reservaRepository.save(reserva);
        //Reserva result = reserva;
        return ResponseEntity.created(new URI("/api/reservas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reservas : Updates an existing reserva.
     *
     * @param reserva the reserva to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reserva,
     * or with status 400 (Bad Request) if the reserva is not valid,
     * or with status 500 (Internal Server Error) if the reserva couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reservas")
    @Timed
    public ResponseEntity<Reserva> updateReserva(@RequestBody Reserva reserva) throws URISyntaxException {
        log.debug("REST request to update Reserva : {}", reserva);
        if (reserva.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        // Reserva result = reservaRepository.save(reserva);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reserva.getId().toString()))
            .body(reserva);
    }

    /**
     * GET  /reservas : get all the reservas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reservas in body
     */
    @GetMapping("/reservas")
    @Timed
    public ResponseEntity<List<Reserva>> getAllReservas(Pageable pageable) {
        log.debug("REST request to get a page of Reservas");
        Page<Reserva> page = reservaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reservas");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /reservas/:id : get the "id" reserva.
     *
     * @param id the id of the reserva to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reserva, or with status 404 (Not Found)
     */
    @GetMapping("/reservas/{id}")
    @Timed
    public ResponseEntity<Reserva> getReserva(@PathVariable Long id) {
        log.debug("REST request to get Reserva : {}", id);
        Optional<Reserva> reserva = reservaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reserva);
    }

    /**
     * DELETE  /reservas/:id : delete the "id" reserva.
     *
     * @param id the id of the reserva to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reservas/{id}")
    @Timed
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id) {
        log.debug("REST request to delete Reserva : {}", id);

        reservaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @RabbitListener(queues = RabbitConfiguration.QUEUE_NAME)
    public void onMessageFromRabbitMQ(MessageQueue messageFromRabbitMQ) {
        log.info("COLA RECIBIDA {}", messageFromRabbitMQ);
        log.info("Host name {}", myhostname);
        log.info("Llegando un mensaje de {}", messageFromRabbitMQ.message_type);
        consenso_fallo(messageFromRabbitMQ);
    }

    public void consenso_fallo(MessageQueue messageFromRabbitMQ) {
        DateTime date = DateTime.now();
        long timeExecution_expired = date.getMillis();
        log.info("Time execution {}", timeExecution_expired);
        if(messageFromRabbitMQ.message_type == TypeMessage.NOTIFY && myhostname.equals(messageFromRabbitMQ.hostname)) {
            log.info("Se trata del mismo servidor {}", myhostname);
            Integer aux = pesos_transacciones.get(messageFromRabbitMQ.posicion_cola);
            pesos_transacciones.set(messageFromRabbitMQ.posicion_cola, aux+ messageFromRabbitMQ.peso);
            return;
        }

        if(messageFromRabbitMQ.message_type == TypeMessage.NOTIFY &&
            messageFromRabbitMQ.status_Reserva == TypeMessage.N0T_ALLOWED) {
            log.info("El mensaje no fue enviado");
            return;
        }

        if(messageFromRabbitMQ.message_type == TypeMessage.NOTIFY &&
            !myhostname.equals(messageFromRabbitMQ.hostname)) {
            log.info("Notifyy mensajeee {}");
            if(reservaRepository.findByRestauranteIdAndSpotIdAndFecha(messageFromRabbitMQ.message_body.getRestaurante().getId(),
                messageFromRabbitMQ.message_body.getSpot().getId(),
                messageFromRabbitMQ.message_body.getFecha()) != null) {
                log.info("La reserva ya existe");
                messageFromRabbitMQ.status_Reserva = TypeMessage.N0T_ALLOWED;

            } else {
                messageFromRabbitMQ.status_Reserva = TypeMessage.ALLOWED;
                messageFromRabbitMQ.message_type = TypeMessage.CONFIRM;
                messageFromRabbitMQ.peso = Integer.parseInt(myhostname);

                Reserva reserva = reservaRepository.findByRestauranteIdAndSpotIdAndFecha(messageFromRabbitMQ.message_body.getRestaurante().getId(),
                    messageFromRabbitMQ.message_body.getSpot().getId(),
                    messageFromRabbitMQ.message_body.getFecha());

                if (reserva == null) {
                    log.info("Guardando");
                    reservaRepository.save(messageFromRabbitMQ.message_body);
                }
            }
        }

        if(messageFromRabbitMQ.message_type == TypeMessage.CONFIRM && messageFromRabbitMQ.hostname.equals(myhostname)) {
            log.info("Mensaje de confirmacion");
            Integer auxiliar = pesos_transacciones.get(messageFromRabbitMQ.posicion_cola);
            pesos_transacciones.add(messageFromRabbitMQ.posicion_cola, auxiliar + messageFromRabbitMQ.peso);


            if(pesos_transacciones.get(messageFromRabbitMQ.posicion_cola) >= peso) {
                log.info("Confirmacion de pesos");
                Reserva reserva = reservaRepository.findByRestauranteIdAndSpotIdAndFecha(messageFromRabbitMQ.message_body.getRestaurante().getId(),
                    messageFromRabbitMQ.message_body.getSpot().getId(),
                    messageFromRabbitMQ.message_body.getFecha());
                if (reserva == null) {
                    log.info("Guardando");
                    reservaRepository.save(messageFromRabbitMQ.message_body);
                }

                messageFromRabbitMQ.message_type = TypeMessage.REGISTER;
            }
        }

        if(messageFromRabbitMQ.message_type == TypeMessage.REGISTER && !messageFromRabbitMQ.hostname.equals(myhostname)) {
            log.info("Registering");
            Reserva reserva = reservaRepository.findByRestauranteIdAndSpotIdAndFecha(messageFromRabbitMQ.message_body.getRestaurante().getId(),
                messageFromRabbitMQ.message_body.getSpot().getId(),
                messageFromRabbitMQ.message_body.getFecha());
            if (reserva == null) {
                reservaRepository.save(messageFromRabbitMQ.message_body);
            }

            if(historial_reservas.size() > 0) {
                log.info("Removiendo historial de reserva registrada ... ");
                historial_reservas.remove(messageFromRabbitMQ.posicion_cola);
                pesos_transacciones.remove(messageFromRabbitMQ.posicion_cola);
            }

            return;
        }


        try {
            log.info("Sending mensaje ");
            rabbitService.send_message(messageFromRabbitMQ);

        }catch(IOException e) {
            e.printStackTrace();
        }

    }


    @Scheduled(fixedDelay=40000)
    public void doSomething() {
        log.info("Informacion asdjaskhdajks");
        if(historial_reservas.size() <= 0) {
            log.info("No hay reservas esperando por deployarse");
            return;
        }

        for (MessageConsenso messageConsenso:historial_reservas) {
            messageConsenso.messageQueue.message_type = TypeMessage.NOTIFY;

            try {
                rabbitService.send_message(messageConsenso.messageQueue);
            } catch (JsonProcessingException e) {
                log.info("No se pudo enviar por la cola {}", messageConsenso.messageQueue);
                e.printStackTrace();
            }

        }

        // something that should execute periodically
    }
}
