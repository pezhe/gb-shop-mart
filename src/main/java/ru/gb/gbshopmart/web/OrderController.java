package ru.gb.gbshopmart.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.gbapi.order.dto.OrderDto;
import ru.gb.gbshopmart.service.OrderService;

import javax.servlet.http.HttpSession;


@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String getOrderList(Model model) {
        model.addAttribute("orders", orderService.findByUser());
        return "order/order-list";
    }

    @GetMapping("/{orderId}")
    public String getOrder(Model model, @PathVariable(name = "orderId") Long id) {
        model.addAttribute("order", orderService.findOrderById(id));
        return "order/order-info";
    }

    @GetMapping("/create")
    public String showOrderForm(Model model) {
        OrderDto orderDto = new OrderDto();
        model.addAttribute("order", orderDto);
        return "order/order-form";
    }

    @PostMapping("/create")
    public String processOrderForm(OrderDto orderDto, HttpSession session) {
        orderService.create(orderDto, session);
        return "redirect:/order";
    }

}