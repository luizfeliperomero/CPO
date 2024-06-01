package ufsm.csi.cpo.modules.versions;

import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufsm.csi.cpo.data.PlatformData;

import java.util.List;

@RestController
@RequestMapping("ocpi/cpo/versions")
public class VersionController {
    private final PlatformData platformData;

    public VersionController() {
        this.platformData = PlatformData.getInstance();
    }

    @GetMapping()
    public ResponseEntity<List<Version>> getVersions(){
        return ResponseEntity.ok(this.platformData.getVersions());
    }

    @GetMapping("/2.2.1/details")
    @SneakyThrows
    public ResponseEntity<List<VersionDetails>> getVersionsDetails() {
        return ResponseEntity.ok(this.platformData.getVersionDetails());
    }

}
