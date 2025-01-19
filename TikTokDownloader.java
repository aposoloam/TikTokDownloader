/*
 * javadocs suck :P
 */


import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class TikTokDownloader {
    public static void main(String[] args) {
        String inputFilePath = "Post.txt"; // replace the blank one with yours or it won't do anything
        String outputDir = "Vids";  

        try {
            List<String> videoUrls = extractVideoUrls(inputFilePath);

            downloadVideos(videoUrls, outputDir);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private static List<String> extractVideoUrls(String filePath) throws IOException {
        List<String> videoUrls = new ArrayList<>();
        Pattern urlPattern = Pattern.compile("Link:\\s*(https?://[^\\s]+)");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = urlPattern.matcher(line);
                if (matcher.find()) {
                    videoUrls.add(matcher.group(1));
                }
            }
        }
        return videoUrls;
    }

    private static void downloadVideos(List<String> urls, String outputDir) throws IOException {
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (int i = 855; i < urls.size(); i++) {
            String url = urls.get(i);
            System.out.println("Downloading: " + url);

            try (InputStream in = new URL(url).openStream()) { // i know this is deprecated, but i really just don't care
                String fileName = "video_" + (i + 1) + ".mp4";
                File outputFile = new File(dir, fileName);

                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                }
                System.out.println("Saved to: " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to download: " + url);
            }
        }
    }
}