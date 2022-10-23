package fit.fancyday.filter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import fit.fancyday.common.exception.CustomException;
import fit.fancyday.model.common.enums.HttpCodeEnum;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * Mobile's Filter
 *
 * @author 喵酱不熬夜
 * @date 2022/10/14
 */

@Component
public class AuthorizeFilter  implements Ordered, GlobalFilter {
    /** secret key */
    private static final String TOKEN_SIGNER_KEY = "123456";

    /**
     * Mobile's Filter
     *
     * @param exchange 交换
     * @param chain    链
     * @return {@code Mono<Void>}
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // Check whether the login request is from a mobile device
        if (request.getURI().getPath().contains("/login")) {
            return chain.filter(exchange);
        }
        // The agreed key-value pair in the request header
        String token = request.getHeaders().getFirst("Authorization");
        // Check whether the token are blank characters or null
        if (StrUtil.isBlank(token)) {
            throw new CustomException(HttpCodeEnum.TOKEN_NULL);
        }
        JWTValidator validator = JWTValidator.of(token);
        // Verify that the algorithm and signature are correct

        validator.validateAlgorithm(JWTSignerUtil.hs512(TOKEN_SIGNER_KEY.getBytes()));

        // Verify that data is in the correct range

        validator.validateDate(DateUtil.date());

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
