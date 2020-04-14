package com.BuSEPTim18.BuSEPTim18.contorller;

import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BuSEPTim18.BuSEPTim18.dto.CertificateDTO;
import com.BuSEPTim18.BuSEPTim18.dto.CertificateNewDTO;
import com.BuSEPTim18.BuSEPTim18.service.CertificateService;


@RestController
@RequestMapping("api/certificates")
public class CertificateController {
	
	@Autowired
	CertificateService certificateService;
	
	/**
	 * @return sve sertifikate
	 */
	@GetMapping
	public ResponseEntity<List<CertificateDTO>> getCertificates() {
		
		//certificateService.validateCerthPath();
		return new ResponseEntity<>(certificateService.getAll(), HttpStatus.OK);
	}
	
	/**
	 * @return sve CA sertifikate
	 */
	@GetMapping("/allCAcertificates")
	public ResponseEntity<List<CertificateDTO>> getCACertificates() {
		
		return new ResponseEntity<>(certificateService.getAllCa(), HttpStatus.OK);
	}
	
	/**
	 * @param serialNumber - koji sertifikat trazim
	 * 
	 * @return nadjeni sertifikat
	 */
	@GetMapping("/{serialNumber}")
	public ResponseEntity<CertificateDTO> getCertificate(@PathVariable String serialNumber) {
		X509Certificate cert = certificateService.getOne(serialNumber);
		
		if(cert == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);			
		} 		
		//Ovo jos prokomentarisati sa ostalima
		CertificateDTO certDTO = new CertificateDTO(cert);
		
		return new ResponseEntity<>(certDTO, HttpStatus.OK);				
	}
	
	/**
	 * @param serialNumber - koji sertifikat trazim
	 * 
	 * @return download nadjeni sertifikat
	 */
	@GetMapping("/download/{serialNumber}")
	public ResponseEntity<?> downloadCertificate(@PathVariable String serialNumber) {
		String certFile = certificateService.download(serialNumber);

		return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType("application/pkix-cert"))
					.contentLength(certFile.length())
					.body(certFile);
	}
	
	/**
	 * @param certNewDTO - podaci za novi selfsigned sertifikat
	 * 
	 * @return kreirani selfsigned sertifikat
	 */
	@PostMapping("/selfsigned")
	public ResponseEntity<CertificateDTO> createSelfSigned(@RequestBody @Valid CertificateNewDTO certNewDTO) {
		
		System.out.println("1");
		
		LocalDate dt = LocalDate.parse(certNewDTO.getExpirationDate());
		
		System.out.println("2");
		
		if (dt.isBefore(LocalDate.now())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		System.out.println("3");
		
		CertificateDTO certDTO = certificateService.addSelfSignedCa(certNewDTO);
		if (certDTO == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		System.out.println("4");
		
		return new ResponseEntity<>(certDTO, HttpStatus.OK);
	}
	
	/**
	 * @param serialNumber - ko ga izdaje
	 * @param certNewDTO - podaci za novi intermediate sertifikat
	 * 
	 * @return kreirani intermediate sertifikat
	 */
	@PostMapping("/intermediate/{serialNumber}/{holderType}")
	public ResponseEntity<CertificateDTO> createIntermediate(@PathVariable String serialNumber, @RequestBody @Valid CertificateNewDTO certNewDTO,@PathVariable String holderType) {
		
		LocalDate dt = LocalDate.parse(certNewDTO.getExpirationDate());
		
		if (dt.isBefore(LocalDate.now())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		X509Certificate cert = certificateService.addCetrificate(serialNumber, certNewDTO, true,holderType);
		
		if (cert == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(new CertificateDTO(cert,holderType), HttpStatus.OK);
	}
	
	/**
	 * @param serialNumber - ko ga izdaje
	 * @param certNewDTO - podaci za novi end sertifikat
	 * 
	 * @return kreirani end sertifikat
	 */
	@PostMapping("/end/{serialNumber}/{holderType}")
	public ResponseEntity<CertificateDTO> createEnd(@PathVariable String serialNumber, @RequestBody @Valid CertificateNewDTO certNewDTO,@PathVariable String holderType) {
		
		LocalDate dt = LocalDate.parse(certNewDTO.getExpirationDate());
		
		if (dt.isBefore(LocalDate.now())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		X509Certificate cert = certificateService.addCetrificate(serialNumber, certNewDTO, false,holderType);
		
		if (cert == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(new CertificateDTO(cert,holderType), HttpStatus.OK);
	}
	
	@PostMapping("/revoke/{serialNumber}")
	public void revokeCertificate(@PathVariable String serialNumber) {		
		certificateService.revokeCertificate(serialNumber);
	}
	
	@GetMapping("/check/{serialNumber}")
	public ResponseEntity<String> checkCertificate(@PathVariable String serialNumber) {
		boolean result = certificateService.checkValidity(serialNumber);
		
		if (result == false) {
			return new ResponseEntity<>("NOT VALID", HttpStatus.OK);
		}
		
		return new ResponseEntity<>("VALID", HttpStatus.OK);
	}
	
	
	
}
