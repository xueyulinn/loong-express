package com.loong.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "loong.shop")
@Data
public class ShopAddressProperties {
    String address;
    String ak;
}
