package com.anarkia.games.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anarkia.games.modelo.Cliente;
import com.anarkia.games.modelo.DetalleFactura;
import com.anarkia.games.modelo.Factura;
import com.anarkia.games.modelo.TipoTicket;
import com.anarkia.games.repositorio.ClienteRepositorio;
import com.anarkia.games.repositorio.FacturaRepositorio;
import com.anarkia.games.repositorio.TipoTicketRepositorio;
import com.stripe.model.PaymentIntent;

@Service
public class FacturaService {

    private static final Logger logger = LoggerFactory.getLogger(FacturaService.class);

    private final FacturaRepositorio facturaRepositorio;
    private final TipoTicketRepositorio tipoTicketRepositorio;
    private final ClienteRepositorio clienteRepositorio;
    private final JavaMailSender mailSender;

    public FacturaService(FacturaRepositorio facturaRepositorio, TipoTicketRepositorio tipoTicketRepositorio, ClienteRepositorio clienteRepositorio, JavaMailSender mailSender) {
        this.facturaRepositorio = facturaRepositorio;
        this.tipoTicketRepositorio = tipoTicketRepositorio;
        this.clienteRepositorio = clienteRepositorio;
        this.mailSender = mailSender;
    }

    @Transactional
    public void registrarCompra(PaymentIntent paymentIntent) {
        logger.info("--- INICIANDO REGISTRO DE COMPRA ---");
        Map<String, String> metadata = paymentIntent.getMetadata();

        
        logger.info("Metadatos recibidos del webhook: {}", metadata);
        
        String clienteIdStr = metadata.get("clienteId");
        if (clienteIdStr == null) {
            logger.error("¡ERROR FATAL! No se encontró 'clienteId' en los metadatos. La compra no se puede registrar.");
            throw new RuntimeException("Falta el clienteId en los metadatos de Stripe.");
        }
        
        logger.info("ID del cliente extraído de los metadatos: {}", clienteIdStr);

        Long clienteId = Long.parseLong(clienteIdStr);
        Cliente cliente = clienteRepositorio.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));

        logger.info("Cliente encontrado en la base de datos: {}", cliente.getNombre());

        Factura factura = new Factura();
        factura.setCliente(cliente);
        factura.setFechaEmision(LocalDateTime.now());
        factura.setTotalPagado(paymentIntent.getAmount());

        List<DetalleFactura> detalles = new ArrayList<>();
        boolean comproTicketCompetidor = false;
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            if (entry.getKey().startsWith("ticket_")) {
                String tipoTicketNombre = entry.getKey().substring("ticket_".length());
                int cantidad = Integer.parseInt(entry.getValue());
                TipoTicket tipoTicket = tipoTicketRepositorio.findById(tipoTicketNombre)
                    .orElseThrow(() -> new RuntimeException("Tipo de ticket no encontrado: " + tipoTicketNombre));

                logger.info("Procesando ticket: '{}'", tipoTicket.getTipoTicket());
                if (tipoTicket.getTipoTicket().toLowerCase().contains("competidor")) {
                    comproTicketCompetidor = true;
                    logger.info("¡Se detectó un ticket de COMPETIDOR!");
                }

                DetalleFactura detalle = new DetalleFactura();
                detalle.setFactura(factura);
                detalle.setTipoTicket(tipoTicket);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitarioVenta(tipoTicket.getPrecio());
                detalles.add(detalle);
            }
        }

        factura.setDetalles(detalles);
        facturaRepositorio.save(factura);
        logger.info("✅ Factura guardada exitosamente en la base de datos para el cliente: {}", cliente.getNombre());
        if (comproTicketCompetidor) {
            logger.info("La compra incluye un ticket de competidor. Actualizando rol del cliente...");
            cliente.setTipoCliente("Competidor");
            clienteRepositorio.save(cliente);
            logger.info("✅ Rol del cliente {} actualizado a 'Competidor'.", cliente.getNombre());
        } else {
            logger.info("La compra NO incluye tickets de competidor. El rol del cliente no se modifica.");
        }
        logger.info("--- FIN DEL REGISTRO DE COMPRA ---");
        try {
            enviarCorreoConfirmacion(factura);
            logger.info("Correo de confirmación de compra enviado a {}.", factura.getCliente().getCorreo());
        } catch (Exception e) {

            logger.error("ERROR al enviar el correo de confirmación: ", e);
        }
        
    }
    private void enviarCorreoConfirmacion(Factura factura) {
        Cliente cliente = factura.getCliente();

        StringBuilder sb = new StringBuilder();
        sb.append("¡Hola, ").append(cliente.getNombre()).append("!\n\n");
        sb.append("¡Gracias por tu compra en Anarkia Games! Tu pago ha sido procesado exitosamente.\n\n");
        sb.append("========================================\n");
        sb.append(" NÚMERO DE ORDEN: #").append(factura.getCodFac()).append("\n");
        sb.append(" FECHA: ").append(factura.getFechaEmision().toLocalDate()).append("\n");
        sb.append("========================================\n\n");
        sb.append("RESUMEN DE TU COMPRA:\n\n");

        for (DetalleFactura detalle : factura.getDetalles()) {
            sb.append("----------------------------------------\n");
            sb.append("Producto: ").append(detalle.getTipoTicket().getEventoNombre()).append("\n");
            sb.append("Tipo: ").append(detalle.getTipoTicket().getTipoTicket()).append("\n");
            sb.append("Cantidad: ").append(detalle.getCantidad()).append("\n");
            
            sb.append("CÓDIGO DE TICKET: TKT-").append(detalle.getIdDetalle()).append("\n");
        }
        
        sb.append("----------------------------------------\n\n");
        
        String totalFormateado = String.format("%.2f", factura.getTotalPagado() / 100.0);
        sb.append("TOTAL PAGADO: S/ ").append(totalFormateado).append(" PEN\n\n");
        
        sb.append("Gracias por tu preferencia,\n");
        sb.append("El equipo de Anarkia Games");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(cliente.getCorreo());
        message.setSubject("Confirmación de tu compra en Anarkia Games (Orden #" + factura.getCodFac() + ")");
        message.setText(sb.toString());
        
        mailSender.send(message);
    }
}