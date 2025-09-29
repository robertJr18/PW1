package com.unimag.gestion_vuelos_reservas.services.impl;

import com.unimag.gestion_vuelos_reservas.repositories.PassengerProfileRepository;
import com.unimag.gestion_vuelos_reservas.repositories.PassengerRepository;
import com.unimag.gestion_vuelos_reservas.services.PassengerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service@Transactional@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerProfileRepository passengerProfileRepository;

}
