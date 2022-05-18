package ru.gb.gbexternalapi.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderGateway orderGateway;

    @GetMapping
    public List<OrderDto> getOrderList() {
        return orderGateway.getOrderList();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("orderId") Long id) {
        return orderGateway.getOrder(id);
    }

    @PostMapping
    public ResponseEntity<OrderDto> handlePost(@Validated @RequestBody OrderDto orderDto) {
        return orderGateway.handlePost(orderDto);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> handleUpdate(@PathVariable("orderId") Long id, @Validated @RequestBody OrderDto orderDto) {
        return orderGateway.handleUpdate(id, orderDto);
    }

    @DeleteMapping("/{orderId}")
    public void deleteById(@PathVariable("orderId") Long id) {
        orderGateway.deleteById(id);
    }

}
