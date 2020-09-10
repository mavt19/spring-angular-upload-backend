package pe.com.vera.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import pe.com.vera.entity.Product;
import pe.com.vera.repository.ProductRepository;

@Transactional
@Service
@AllArgsConstructor
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ProductServiceImpl implements ProductService<Product> {

	private final ProductRepository _repository;
	private final CloudinaryService _cloudinariService;

	@Override
	public Product save(Product entity, MultipartFile file) {
		// TODO Auto-generated method stub

		if (file.isEmpty()) {
			return _repository.save(entity);
		} else {

			Map result = _cloudinariService.upload(file);
			entity.setImagenId((String) result.get("public_id"));
			entity.setImagenUrl((String) result.get("url"));
			return _repository.save(entity);
		}
	}

	@Override
	public Map update(Product entity, MultipartFile file) {

		Map resp = null;
		Map upload = null;
		Map remove = null;
		Optional<Product> result = _repository.findById(entity.getId());

		System.out.println(result.map(x -> x.getName()));

		try {

			if (result.isPresent()) {
				Product entityDb = _repository.findById(entity.getId()).get();
				if (file.isEmpty()) {
					resp = new HashMap<>();
					entity.setImagenId(entityDb.getImagenId());
					entity.setImagenUrl(entityDb.getImagenUrl());
					resp.put("product", _repository.save(entity));
				} else if (entityDb.getImagenId() == null) {
					resp = new HashMap<>();
					upload = _cloudinariService.upload(file);
					entity.setImagenId((String) upload.get("public_id"));
					entity.setImagenUrl((String) upload.get("url"));
					resp.put("upload", upload);
					resp.put("product", _repository.save(entity));
				} else {
					resp = new HashMap<>();
					remove = _cloudinariService.remove(result.map(r -> r.getImagenId()).get());
					System.out.println("*".repeat(20) + remove);
					upload = _cloudinariService.upload(file);
					entity.setImagenId((String) upload.get("public_id"));
					entity.setImagenUrl((String) upload.get("url"));

					resp.put("delete", remove);
					resp.put("upload", upload);
					resp.put("product", _repository.save(entity));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			resp.put("error", e.getMessage());
		}

		return resp;

	}

	@Override
	public Product updateP(Long id, Product entity, MultipartFile file) throws IOException {
		// TODO Auto-generated method stub

		Optional<Product> result = _repository.findById(id);
		Map upload = null;
		Map remove = null;
		System.out.println("".repeat(30) + result.map(x -> x.getName()));

		if (result.isPresent()) {
			Product entityDb = _repository.findById(entity.getId()).get();
			if (file.isEmpty()) {
				entity.setImagenId(entityDb.getImagenId());
				entity.setImagenUrl(entityDb.getImagenUrl());
				return _repository.save(entity);
			} else if (entityDb.getImagenId() == null) {
				upload = _cloudinariService.upload(file);
				entity.setImagenId((String) upload.get("public_id"));
				entity.setImagenUrl((String) upload.get("url"));
				return _repository.save(entity);
			} else {

				remove = _cloudinariService.remove(result.map(r -> r.getImagenId()).get());
				System.out.println("*".repeat(20) + remove);
				upload = _cloudinariService.upload(file);
				entity.setImagenId((String) upload.get("public_id"));
				entity.setImagenUrl((String) upload.get("url"));

				return _repository.save(entity);
			}
		}
		return result.get();
	}

	@Override
	public Optional<Product> getById(Long id) {
		// TODO Auto-generated method stub
		return _repository.findById(id);
	}

	@Override
	public Map remove(Long id) throws IOException {
		Map resp = null;
		Optional<Product> result = _repository.findById(id);
		// Product resultDb = _repository.findById(id).get();
		System.out.println(result.map(x -> x.getName()));

		if (result.isPresent()) {
			Product resultDb = _repository.findById(id).get();
			if (resultDb.getImagenId() == null) {
				resp = new HashMap<>();

				_repository.deleteById(id);
				resp.put("result", "ok");

			} else {
				resp = _cloudinariService.remove(result.map(r -> r.getImagenId()).get());
				_repository.deleteById(id);
			}
		}

		System.out.println("*".repeat(20) + resp);
		return resp;
	}

	@Override
	public Iterable<Product> findAll() {
		// TODO Auto-generated method stub
		return _repository.findAll();
	}
}
