package ufsm.csi.cpo.modules.versions;

import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.net.URL;

@RestController
@RequestMapping("ocpi/cpo/versions")
public class VersionController {
    List<Version> versions = Arrays.asList(new Version(VersionNumber.V2_2_1, new URL("https://www.csicpo.com/ocpi/cpo/2.2.1/details")));

    public VersionController() throws MalformedURLException {
    }

    @GetMapping()
    public ResponseEntity<List<Version>> getVersions(){
        return ResponseEntity.ok(versions);
    }

    @GetMapping("/2.2.1/details")
    @SneakyThrows
    public ResponseEntity<List<VersionDetails>> getVersionsDetails() {
        List<Endpoint> endpoints = Arrays.asList(
        new Endpoint(ModuleID.locations,
                InterfaceRole.SENDER,
                new URL("https://www.csicpo.com/ocpi/cpo/2.2.1/locations")));

        List<VersionDetails> versionDetails = Arrays.asList(new VersionDetails(VersionNumber.V2_2_1, endpoints));

        return ResponseEntity.ok(versionDetails);
    }

}
