package ru.gb.gbshopmart.web;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public String getProductList(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "order-list";
    }


    @GetMapping("/{orderId}")
    public String info(Model model, @PathVariable(name = "orderId") Long id) {
        OrderDto orderDto;
        if (id != null) {
            orderDto = orderService.findById(id);
        } else {
            return "redirect:/product/all";
        }
        model.addAttribute("order", orderDto);
        return "order-info";
    }


    @GetMapping
    public String showForm(Model model, @RequestParam(name = "id", required = false) Long id) {
        OrderDto orderDto;

        if (id != null) {
            orderDto = orderService.findById(id);
        } else {
            orderDto = new OrderDto();
        }
        model.addAttribute("order", orderDto);
        return "order-form";
    }


    @PostMapping
    public String saveOrder(OrderDto orderDto) {
        orderService.save(orderDto);
        return "redirect:/order/all";
    }


    @GetMapping("/delete")
    public String deleteById(@RequestParam(name = "id") Long id) {
        orderService.deleteById(id);
        return "redirect:/order/all";
    }


}
