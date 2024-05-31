package ufsm.csi.cpo.modules.versions;

import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufsm.csi.cpo.data.CpoData;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.net.URL;

@RestController
@RequestMapping("ocpi/cpo/versions")
public class VersionController {
    private final CpoData cpoData;

    public VersionController() {
        this.cpoData = CpoData.getInstance();
    }

    @GetMapping()
    public ResponseEntity<List<Version>> getVersions(){
        return ResponseEntity.ok(this.cpoData.getVersions());
    }

    @GetMapping("/2.2.1/details")
    @SneakyThrows
    public ResponseEntity<List<VersionDetails>> getVersionsDetails() {
        return ResponseEntity.ok(this.cpoData.getVersionDetails());
    }

}
