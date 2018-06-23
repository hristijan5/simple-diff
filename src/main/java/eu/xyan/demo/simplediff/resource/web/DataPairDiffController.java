package eu.xyan.demo.simplediff.resource.web;

import eu.xyan.demo.simplediff.algorithm.AlgorithmType;
import eu.xyan.demo.simplediff.algorithm.implementation.dto.DiffDtoV1;
import eu.xyan.demo.simplediff.exception.NoDataPairException;
import eu.xyan.demo.simplediff.service.DataPairService;
import eu.xyan.demo.simplediff.service.dto.DataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DataPairDiffController {

    private final DataPairService dataPairService;

    @PostMapping(value = "/v1/diff/{id}/left", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity setLeftData(@PathVariable("id") Long id, @RequestBody DataDto dataDto) {
        return ResponseEntity.ok(dataPairService.saveData(id, dataDto.getData(), false));
    }

    @PostMapping(value = "/v1/diff/{id}/right", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity setRightData(@PathVariable("id") Long id, @RequestBody DataDto dataDto) {
        return ResponseEntity.ok(dataPairService.saveData(id, dataDto.getData(), true));
    }

    @GetMapping(value = "/v1/diff/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiffDtoV1> getDiff(@RequestParam(required = false, defaultValue = "LINEAR")
                                                     AlgorithmType algorithmType, @PathVariable("id") Long id)
            throws NoDataPairException {
        return ResponseEntity.ok(dataPairService.computeDiff(algorithmType, id));
    }

}
