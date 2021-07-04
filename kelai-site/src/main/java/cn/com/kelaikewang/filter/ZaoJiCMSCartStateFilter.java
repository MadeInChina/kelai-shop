package cn.com.kelaikewang.filter;

import org.broadleafcommerce.core.order.domain.NullOrderFactory;
import org.broadleafcommerce.core.order.domain.NullOrderImpl;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.core.web.order.CartState;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ZaoJiCMSCartStateFilter extends OncePerRequestFilter {

    protected NullOrderFactory nullOrderFactory;

    public void setNullOrderFactory(NullOrderFactory nullOrderFactory) {
        this.nullOrderFactory = nullOrderFactory;
    }

    /**
     * 如果订单已经支付，清空购物车
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        Order cart = CartState.getCart();
        if (cart != null && !(cart instanceof NullOrderImpl) && OrderStatus.SUBMITTED.getType().equals(cart.getStatus().getType())){
               CartState.setCart(nullOrderFactory.getNullOrder());
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
