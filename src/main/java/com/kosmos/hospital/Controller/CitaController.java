package com.kosmos.hospital.Controller;

import com.kosmos.hospital.DTO.CitaDTO;
import com.kosmos.hospital.Service.CitaService;
import com.kosmos.hospital.model.Cita;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping("/addCita")
    public ResponseEntity<String> createCita(@RequestBody @Valid CitaDTO citaDTO) {
        Cita saveCita = citaService.agendarCita(citaDTO);
        return  ResponseEntity.ok("Cita fue creada con éxito.");
    }

    @DeleteMapping("/cancelar/{citaId}")
    public ResponseEntity<String> cancelarCita(@PathVariable Long citaId) {
        citaService.cancelarCita(citaId);
        return ResponseEntity.ok("Cita cancelada con éxito.");
    }

    @PutMapping("/citas/{citaId}")
    public ResponseEntity<String> editarCita(@PathVariable Long citaId, @RequestBody @Valid CitaDTO citaDTO) {
        citaService.editarCita(citaId, citaDTO);
        return ResponseEntity.ok("Cita actualizada con éxito.");
    }
}
