package com.estacionamento.park.api.demo.parkapi.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class EstacionamentoUtils {

    public static String gerarRecibo(){
        LocalDateTime date = LocalDateTime.now();
        String recibo = date.toString().substring(0,19);
        return recibo.replace("-", "").replace(":", "").replace("T", "-");
    }

}
