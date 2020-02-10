package upp.project.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import upp.project.model.ScientificPaper;
import upp.project.repositories.ScientificPaperRepository;

@Service
public class ScientificPaperService {

	@Autowired
	private ScientificPaperRepository scientificPaperRepository;

	public String store(MultipartFile file, String processId, String username) throws RuntimeException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		if(!Files.exists(Paths.get("scientific-papers-dir/"+username))) {
			try {
				Files.createDirectory(Paths.get("scientific-papers-dir/"+username));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		if(!Files.exists(Paths.get("scientific-papers-dir/"+username+"/" + processId))) {
			try {
				Files.createDirectory(Paths.get("scientific-papers-dir/"+username+"/" + processId));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		
		try {
			Files.copy(file.getInputStream(), Paths.get("scientific-papers-dir/"+username+"/" + processId).resolve(file.getOriginalFilename()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RuntimeException("FAIL!");
		}

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/scientificPaper/download/")
				.path(processId+"/"+fileName).toUriString();
		System.out.println(fileDownloadUri);
		return fileDownloadUri;
	}

	public Resource downloadFile(String processId, String username, String fileName) {
		Path path = Paths.get("scientific-papers-dir/"+ username + "/" + processId + "/" + fileName);
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return resource;
	}

	public ScientificPaper save(ScientificPaper scientificPaper) {
		return this.scientificPaperRepository.save(scientificPaper);
	}

	public ScientificPaper getOne(Long id) {
		return this.scientificPaperRepository.getOne(id);
	}
}
