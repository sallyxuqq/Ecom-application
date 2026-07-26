package com.app.ecom.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.ecom.dto.OrderItemDTO;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Order;
import com.app.ecom.model.OrderItem;
import com.app.ecom.model.OrderStatus;
import com.app.ecom.model.User;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Optional<OrderResponse> createOrder(Long userId) {

        List<CartItem> cartItems = cartService.getCart(userId);

        if (cartItems.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();

        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {

        List<OrderItemDTO> itemDTOs = order.getItems()
                .stream()
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice().multiply(
                                BigDecimal.valueOf(item.getQuantity())
                        )
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                itemDTOs,
                order.getCreatedAt()
        );
    }
}