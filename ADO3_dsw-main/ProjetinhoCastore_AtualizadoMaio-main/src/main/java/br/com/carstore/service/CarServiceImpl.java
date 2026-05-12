package br.com.carstore.service;

import br.com.carstore.dto.CarDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CarServiceImpl implements CarService {

    private List<CarDTO> cars;

    public CarServiceImpl() {
        cars = new ArrayList<CarDTO>();
    }

    // ADICIONE ESTE MÉTODO QUE ESTAVA FALTANDO:
    @Override
    public List<CarDTO> findAll() {
        return this.cars;
    }

    @Override
    public CarDTO findById(String id) {
        return this.cars.stream()
                .filter(car -> car.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(CarDTO carDTO) {
        if (carDTO.getId() == null || carDTO.getId().isBlank()) {
            UUID uuid = UUID.randomUUID();
            carDTO.setId(uuid.toString());
        }
        this.cars.add(carDTO);
    }

    @Override
    public void deleteById(String id) {
        this.cars.removeIf(car -> car.getId().equals(id));
    }

    @Override
    public void update(String id, CarDTO carDTO) {
        this.cars.replaceAll(car -> car.getId().equals(id) ? carDTO : car);
    }
}