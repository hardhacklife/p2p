package com.p2p.node.controller;

import com.p2p.node.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    // 🔹 UPLOAD
    @PostMapping("/{filename}")
    public ResponseEntity<String> upload(
            @PathVariable String filename,
            @RequestBody byte[] data,
            @RequestParam(defaultValue = "true") boolean replicate) {

        fileService.saveFile(filename, data, replicate);
        return ResponseEntity.ok("Fichier uploadé !");
    }

    // 🔹 DOWNLOAD avec TTL
    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> download(
            @PathVariable String filename,
            @RequestParam(defaultValue = "3") int ttl) {

        byte[] data = fileService.getFile(filename, ttl);

        if (data == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .body(data);
    }

    // 🔹 DELETE
    @DeleteMapping("/{filename}")
    public ResponseEntity<String> delete(@PathVariable String filename) {

        boolean deleted = fileService.deleteFile(filename);

        return deleted
                ? ResponseEntity.ok("Fichier supprimé")
                : ResponseEntity.notFound().build();
    }

    // 🔥 LISTE
    @GetMapping
    public ResponseEntity<List<String>> listFiles() {
        return ResponseEntity.ok(fileService.listFiles());
    }
}
