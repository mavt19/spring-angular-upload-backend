package pe.com.vera.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.vera.entity.Product;
public interface ProductRepository extends JpaRepository<Product, Long>{

}
