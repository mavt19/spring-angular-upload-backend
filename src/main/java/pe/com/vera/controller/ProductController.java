package pe.com.vera.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pe.com.vera.dto.Message;
import pe.com.vera.entity.Product;
import pe.com.vera.service.CloudinaryService;
import pe.com.vera.service.ProductService;

@CrossOrigin
@RestController
@RequestMapping("api")
@SuppressWarnings("rawtypes")
public class ProductController {

	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	CloudinaryService cloudinaryService;
	@Autowired
	ProductService<Product> productService;

	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam MultipartFile file) throws IOException {
		Map result = cloudinaryService.upload(file);
		System.out.println(result);
		LOG.info("" + result);
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@DeleteMapping("/remove/{id}")
	public ResponseEntity<?> remove(@PathVariable String id) throws IOException {
		Map result = cloudinaryService.delete(id);
		System.out.println(result);
		LOG.info("" + result);
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@GetMapping
	public String hello() {
		return "hola";
	}

	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestParam( required = false) MultipartFile file, @ModelAttribute Product product)
			throws IOException {

		

		if (file.isEmpty()) {
			Product result = productService.save(product, file);
			return new ResponseEntity<>(result, HttpStatus.CREATED);

		}
		BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
		if (bufferedImage == null) {
			return new ResponseEntity<>(new Message("image not valid"), HttpStatus.UNSUPPORTED_MEDIA_TYPE);

		}
		Product result = productService.save(product, file);
		LOG.info("" + result);
		return new ResponseEntity<>(result, HttpStatus.CREATED);

	}

	@PutMapping("/update")
	public ResponseEntity<?> update(@RequestParam( required = false) MultipartFile file, @ModelAttribute Product product)
			throws IOException {
		BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

		if (file.isEmpty()) {
			Product result = productService.save(product, file);
			return new ResponseEntity<>(result, HttpStatus.CREATED);

		}
		if (bufferedImage == null) {
			return new ResponseEntity<>(new Message("image not valid"), HttpStatus.UNSUPPORTED_MEDIA_TYPE);

		}
		Map result = productService.update(product, file);
		LOG.info("" + result);

		if (result == null) {
			return new ResponseEntity<>(new Message("product with id :" + product.getId() + " not found"),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(result, HttpStatus.CREATED);

	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateP(@PathVariable("id") Long id, @RequestParam MultipartFile file,
			@ModelAttribute Product product) throws IOException {
		BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
		if (productService.getById(id).isEmpty()) {
			return new ResponseEntity<>(new Message("product with id :" + product.getId() + " not found"),
					HttpStatus.NOT_FOUND);
		}
		
		if (file.isEmpty()) {
			Product result = productService.updateP(id, product, file);
			return new ResponseEntity<>(result, HttpStatus.CREATED);

		}
		if (bufferedImage == null) {
			return new ResponseEntity<>(new Message("image not valid"), HttpStatus.UNSUPPORTED_MEDIA_TYPE);

		}
		
		
		Product result = productService.updateP(id, product, file);
		LOG.info("*" + result);
 
		

		return new ResponseEntity<>(result, HttpStatus.CREATED);

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) throws IOException {

		Map result = productService.remove(id);
		System.out.println(result);

		if (result == null) {
			return new ResponseEntity<>(new Message("product with id :" + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@GetMapping("/listar")
	@ResponseStatus(code = HttpStatus.OK)
	public Iterable<Product> listar() {
		return productService.findAll();
	}

//	@GetMapping("/listar/{id}")
//	public ResponseEntity<?> getById(@PathVariable Long id) {
//		
//		Map resp = new HashMap<>();
//		
//		
//		 Optional<Product> result = productService.getById(id);
//		System.out.println("*".repeat(25) +result);
//		if(result.isEmpty()) {
//			resp.put("message", "User with id "+ id + " not found");
//			return new ResponseEntity<>(resp, HttpStatus.ACCEPTED);
//		}else {
//			resp.put("message", "User with id "+ id + " found");
//			resp.put("user", result);
//		return new ResponseEntity<>(resp, HttpStatus.OK);
//		
//		}
//		
//	}

	@GetMapping("/listar/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		Optional<Product> result = productService.getById(id);
		System.out.println("*".repeat(25) + result);

		if (result.isEmpty()) {
			return new ResponseEntity<>(new Message("product with id :" + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);

	}
}
