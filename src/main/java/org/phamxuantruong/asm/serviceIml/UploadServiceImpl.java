package org.phamxuantruong.asm.serviceIml;

import jakarta.servlet.ServletContext;
import org.phamxuantruong.asm.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    ServletContext app;

    public File save(MultipartFile file, String folder) {
        File dir = new File(app.getRealPath("/assets/" + folder));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String s = System.currentTimeMillis() + file.getOriginalFilename();
        String name = Integer.toHexString(s.hashCode()) + s.substring(s.lastIndexOf("."));
        try {
            File saveFile = new File(dir, name);
            file.transferTo(saveFile);
            System.out.println(saveFile.getAbsolutePath());
            return saveFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

