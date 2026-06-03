package com.anarkia.games.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.anarkia.games.service.FacturaService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

@RestController
public class StripeWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);


    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    private final FacturaService facturaService;

    public StripeWebhookController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }


    @PostMapping("/stripe/events")
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                    @RequestHeader("Stripe-Signature") String sigHeader) {
        try {

            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            logger.info("📦 Evento recibido desde Stripe: {}", event.getType());


            if ("payment_intent.succeeded".equals(event.getType())) {
                PaymentIntent paymentIntent = (PaymentIntent) event.getData().getObject();
                logger.info("✅ Pago exitoso: {}", paymentIntent.getId());


                facturaService.registrarCompra(paymentIntent);
            }


            return ResponseEntity.ok("Evento recibido correctamente");

        } catch (SignatureVerificationException e) {
            logger.error("⚠️ Firma inválida del Webhook de Stripe", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Firma inválida");
        } catch (Exception e) {
            logger.error("❌ Error al procesar el Webhook de Stripe", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno");
        }
    }
}