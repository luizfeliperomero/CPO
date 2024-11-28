package ufsm.csi.cpo.modules.locations;

import org.springframework.web.bind.annotation.*;
import ufsm.csi.cpo.modules.types.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {
    List<Location> locations = new ArrayList<Location>();
    @PostMapping
    public void getLocations(@RequestBody Location location) {
       this.locations.add(location);
    }

    @GetMapping
    public Response<List<Location>> getLocations() {
        return new Response<>(this.locations, 1000, new Date());
    }
}
