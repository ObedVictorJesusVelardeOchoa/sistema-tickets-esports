package com.anarkia.games.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anarkia.games.dto.CartItem;
import com.anarkia.games.dto.CreatePaymentRequestCarrito;
import com.anarkia.games.dto.TipoTicketDTO;
import com.anarkia.games.modelo.Usuario;
import com.anarkia.games.repositorio.UsuarioRepositorio;
import com.anarkia.games.service.TipoTicketService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;


@RestController
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final TipoTicketService tipoTicketService;
    private final UsuarioRepositorio usuarioRepositorio;

    public PaymentController(TipoTicketService tipoTicketService, UsuarioRepositorio usuarioRepositorio) {
        this.tipoTicketService = tipoTicketService;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @PostMapping("/create-payment-intent-carrito")
    public Map<String, String> createPaymentIntentCarrito(@RequestBody CreatePaymentRequestCarrito request) throws StripeException {

        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            logger.error("¡ERROR FATAL! Intento de compra por usuario anónimo. Se requiere iniciar sesión.");
            throw new IllegalStateException("El usuario debe iniciar sesión para poder comprar.");
        }
        String currentUserName = authentication.getName();
        logger.info("Nombre de usuario autenticado: {}", currentUserName);

        Usuario usuario = usuarioRepositorio.findByCorreo(currentUserName); 
        if (usuario == null) {
            logger.error("¡ERROR FATAL! El usuario '{}' fue autenticado pero no se encontró en la base de datos.", currentUserName);
            throw new IllegalStateException("No se encontró un usuario autenticado para realizar la compra.");
        }


        Map<String, TipoTicketDTO> ticketsDisponibles = tipoTicketService.getTicketsDisponibles()
                .stream().collect(Collectors.toMap(TipoTicketDTO::getTipo, dto -> dto));

        long totalAmount = 0;
        Map<String, String> metadata = new HashMap<>();


        for (CartItem item : request.getItems()) {
            TipoTicketDTO ticketInfo = ticketsDisponibles.get(item.getTipo());

            if (ticketInfo == null) {
                throw new IllegalStateException("El ticket " + item.getTipo() + " no está disponible o no existe.");
            }
            if (ticketInfo.getStockActual() < item.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para el ticket: " + item.getTipo());
            }

            totalAmount += ticketInfo.getPrecio() * item.getCantidad();
            metadata.put("ticket_" + item.getTipo(), String.valueOf(item.getCantidad()));
        }
        

        metadata.put("clienteId", usuario.getId().toString());

        if (totalAmount <= 0) {
            throw new IllegalArgumentException("El total de la compra debe ser mayor a cero.");
        }

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(totalAmount)
                        .setCurrency("pen")
                        .putAllMetadata(metadata)
                        .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                        )
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", paymentIntent.getClientSecret());
        return response;
    }
}