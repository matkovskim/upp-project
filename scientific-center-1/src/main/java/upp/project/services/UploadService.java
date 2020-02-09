package upp.project.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import upp.project.controller.StartController;

@Service
public class UploadService {

	private Map<String, String> files = new HashMap<String, String>();

	public void store(MultipartFile file, String procesId) {
		
		if(Files.exists(Paths.get("upload-dir/"+procesId))) {
			FileSystemUtils.deleteRecursively(Paths.get("upload-dir/"+procesId).toFile());
		}
		try {
			Files.createDirectory(Paths.get("upload-dir/"+procesId));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			Files.copy(file.getInputStream(), Paths.get("upload-dir/"+procesId).resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
			files.put(procesId, file.getOriginalFilename());
		} catch (Exception e) {
			throw new RuntimeException("FAIL!");
		}
	}

	public String loadFile(String procesId) {
		String fileLink = MvcUriComponentsBuilder.fromMethodName(StartController.class, "getFile", procesId).build().toString();
		return fileLink;
	}
	
	public Resource getFile(String procesId) {
		try {
			Path file = Paths.get("upload-dir/"+procesId).resolve(files.get(procesId));
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
		}
	}
	
}
