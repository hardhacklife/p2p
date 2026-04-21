package com.p2p.master.service;

import com.p2p.master.config.NodeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private NodeConfig config;

    private final RestTemplate restTemplate = new RestTemplate();

    // SAVE
    public void saveFile(String filename, byte[] data, boolean replicate) {
        try {
            Path path = Paths.get(config.getStorage(), filename);

            Files.createDirectories(path.getParent());
            Files.write(path, data);

            System.out.println("Fichier sauvegardé : " + path);

            if (replicate) {
                replicateFile(filename, data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // REPLICATION
    private void replicateFile(String filename, byte[] data) {
        for (String peer : config.getPeers()) {
            try {
                System.out.println("Envoi vers : " + peer);

                restTemplate.postForEntity(
                        peer + "/files/" + filename + "?replicate=false",
                        data,
                        String.class
                );

            } catch (Exception e) {
                System.out.println("Erreur vers " + peer);
            }
        }
    }

    // GET AVEC TTL
    public byte[] getFile(String filename, int ttl) {

        if (ttl <= 0) {
            System.out.println("TTL expiré");
            return null;
        }

        try {
            Path path = Paths.get(config.getStorage(), filename);

            // ✅ LOCAL
            if (Files.exists(path)) {
                System.out.println("Trouvé local : " + filename);
                return Files.readAllBytes(path);
            }

            System.out.println("Recherche chez peers... TTL=" + ttl);

            // 🔥 PEERS
            return searchInPeers(filename, ttl - 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 🔹 SEARCH PEERS
    private byte[] searchInPeers(String filename, int ttl) {

        for (String peer : config.getPeers()) {
            try {
                System.out.println("Recherche dans : " + peer);

                byte[] data = restTemplate.getForObject(
                        peer + "/files/" + filename + "?ttl=" + ttl,
                        byte[].class
                );

                if (data != null) {
                    System.out.println("Trouvé chez : " + peer);

                    // 💾 sauvegarde locale
                    saveFile(filename, data, false);

                    return data;
                }

            } catch (Exception e) {
                System.out.println("Non trouvé sur " + peer);
            }
        }

        return null;
    }

    // 🔹 DELETE
    public boolean deleteFile(String filename) {
        try {
            Path path = Paths.get(config.getStorage(), filename);

            if (Files.exists(path)) {
                Files.delete(path);
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 🔥 LIST FILES (FIX IMPORTANT)
    public List<String> listFiles() {

        File folder = new File(config.getStorage());

        System.out.println("Dossier utilisé : " + folder.getAbsolutePath());

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File[] files = folder.listFiles();

        if (files == null) {
            return List.of();
        }

        return Arrays.stream(files)
                .map(File::getName)
                .toList();
    }
}