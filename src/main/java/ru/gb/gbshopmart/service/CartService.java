package ru.gb.gbshopmart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.gbapi.product.dto.ProductDto;
import ru.gb.gbshopmart.entity.Product;
import ru.gb.gbshopmart.web.model.Cart;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductService productService;


    public Cart getCurrentCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    public void resetCart(HttpSession session) {
        session.removeAttribute("cart");
    }

    public void addToCart(HttpSession session, Long productId) {
        Product product = productService.findProductById(productId);
        Cart cart = getCurrentCart(session);
        cart.add(product);
    }

    public void removeFromCart(HttpSession session, Long productId) {
        Product product = productService.findProductById(productId);
        Cart cart = getCurrentCart(session);
        cart.remove(product);
    }

    public void setProductCount(HttpSession session, Long productId, Integer quantity) {
        Product product = productService.findProductById(productId);
        Cart cart = getCurrentCart(session);
        cart.setQuantity(product, quantity);
    }

    public BigDecimal getTotalCost(HttpSession session) {
        return getCurrentCart(session).getTotalCost();
    }
}
