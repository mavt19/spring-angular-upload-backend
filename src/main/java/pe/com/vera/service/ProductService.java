package pe.com.vera.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
@SuppressWarnings({  "rawtypes" })
public interface ProductService<T> {

	public default  Iterable<T> findAll(){
		return null;
	}
	
	public default T save(T entity, MultipartFile file) {
		return null;
	}
	
	
	public default Map update(T entity, MultipartFile file) {
		return null;
	}
	public default T updateP(Long id, T entity, MultipartFile file) throws IOException {
		return null;
	}
	public default Optional<T> getById(Long id) {
		return null;
	}
	public default Map remove(Long id) throws IOException {
		return null;
	}
}
