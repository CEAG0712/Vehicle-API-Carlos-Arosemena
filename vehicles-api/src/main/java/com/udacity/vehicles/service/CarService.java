package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.Price;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository repository;

    @Qualifier("pricing")
    private final WebClient webClientPricing;

    @Qualifier("maps")
    private final WebClient webClientMaps;

    private final ModelMapper modelMapper;

    public CarService(CarRepository repository, @Qualifier("pricing") WebClient webClientPricing, @Qualifier("maps") WebClient webClientMaps, ModelMapper modelMapper) {

        /**
         * TODO: Add the Maps and Pricing Web Clients you create
         *   in `VehiclesApiApplication` as arguments and set them here.
         */

        this.repository = repository;
        this.webClientPricing = webClientPricing;
        this.webClientMaps = webClientMaps;
        this.modelMapper = modelMapper;
    }




    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         *   Remove the below code as part of your implementation.
         */
        Car car = repository.findById(id).orElseThrow(CarNotFoundException::new);

        /**
         * TODO: Use the Pricing Web client you create in `VehiclesApiApplication`
         *   to get the price based on the `id` input'
         * TODO: Set the price of the car
         * Note: The car class file uses @transient, meaning you will need to call
         *   the pricing service each time to get the price.
         */
        car.setPrice(getPriceClient().getPrice(id));

        /**
         * TODO: Use the Maps Web client you create in `VehiclesApiApplication`
         *   to get the address for the vehicle. You should access the location
         *   from the car object and feed it to the Maps service.
         * TODO: Set the location of the vehicle, including the address information
         * Note: The Location class file also uses @transient for the address,
         * meaning the Maps service needs to be called each time for the address.
         */

        car.setLocation(getMapsClient().getAddress(car.getLocation()));

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setCondition(car.getCondition());
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        carToBeUpdated.setPrice(getPriceClient().getPrice(carToBeUpdated.getId()));
                        carToBeUpdated.setLocation(getMapsClient().getAddress(carToBeUpdated.getLocation()));
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }


        Car newCar = repository.save(car);
        newCar.setPrice(getPriceClient().getPrice(newCar.getId()));
        newCar.setLocation(getMapsClient().getAddress(newCar.getLocation()));
        return newCar;

//I was playing with persisting a price with the vehicle creation, want to keep the code here :)
        //Price price = car.getPrice();
//        price.setVehicleId(car.getId());
//        car.setPrice(price);
//        getPriceClient().savePrice(price);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         */

        Car carToBeDeleted = repository.findById(id).orElseThrow(CarNotFoundException::new);


        /**
         * TODO: Delete the car from the repository.
         */

        repository.delete(carToBeDeleted);
    }

    private PriceClient getPriceClient(){
        PriceClient priceClient = new PriceClient(webClientPricing);

        return priceClient;
    }

    private MapsClient getMapsClient(){
        MapsClient mapsClient = new MapsClient(webClientMaps, modelMapper);
        return mapsClient;
    }
}
