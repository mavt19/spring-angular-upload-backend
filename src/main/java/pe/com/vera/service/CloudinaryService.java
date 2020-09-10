package pe.com.vera.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CloudinaryService.class);

	Cloudinary cloudinary;

    private Map<String, String> valuesMap = new HashMap<>();

    public CloudinaryService() {
        valuesMap.put("cloud_name", "mavt19");
        valuesMap.put("api_key", "152151788667893");
        valuesMap.put("api_secret", "1PS9y2NQbxAvS0VQy2O2NJwOYq0");
        cloudinary = new Cloudinary(valuesMap);
    }
    
    
    @SuppressWarnings("rawtypes")
	public Map upload(MultipartFile multipartFile){
        
    	Map result = null;
		try {
//			File file = convert(multipartFile);
//			result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
			File file = convert(multipartFile);
			//"folder", rutaCarpetaCloudinary+"/","public_id", ""
			 result = cloudinary.uploader().upload(file, ObjectUtils.asMap(
		        	    "folder", "upload_spring_angular/products/","public_id", ""
		        	));
			LOG.info("" + result);
	        file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
//        Map result = cloudinary.uploader().upload(file, ObjectUtils.asMap(
//        	    "public_id", "upload_spring_angular/products/"        
//        	));
        
        
        return result;
    }
	
	@SuppressWarnings("rawtypes")
	public Map delete(String id) throws IOException {
        
		String url="upload_spring_angular/products/";
		
		Map result = cloudinary.uploader().destroy(url+id, ObjectUtils.emptyMap());
        return result;
    }
	
	@SuppressWarnings("rawtypes")
	public Map remove(String id) throws IOException {
        		
		Map result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
		LOG.info(" "+result);
        return result;
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
}
