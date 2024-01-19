package com.philzcodes.idm.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.philzcodes.idm.model.*;

@Service
public class DownloadService {

    private static final int BUFFER_SIZE = 4096;

    private List<DownloadItem> downloadItems = new ArrayList<>();
    private List<DownloadItem> selectedItems = new ArrayList<>();
    private int knapsackCapacity = 1000; // Adjust this based on your requirements

    public void addDownloadItem(DownloadItem item) {
        downloadItems.add(item);
        // Assuming you are implementing 0/1 Knapsack
        optimizeDownloads();
    }

    public void addAllDownloadItem(List<DownloadItem> items) {
        downloadItems.addAll(items);
        // Assuming you are implementing 0/1 Knapsack
        System.out.println("=========SHUFFLING THE ITEMS=========" + downloadItems.size());
        optimizeDownloads();
        downloadSelectedItems();

    }

    public List<DownloadItem> getDownloadItems() {
        // Retrieve the list of download items from the knapsack algorithm
        return selectedItems;
    }

    public List<DownloadItem> optimizeDownloads() {
        int n = downloadItems.size();
        int[][] dp = new int[n + 1][knapsackCapacity + 1];

        for (int i = 0; i <= n; i++) {
            for (int w = 0; w <= knapsackCapacity; w++) {
                if (i == 0 || w == 0) {
                    dp[i][w] = 0;
                } else if (downloadItems.get(i - 1).getSize() <= w) {
                    int itemPriority = downloadItems.get(i - 1).getPriority();
                    int itemSize = downloadItems.get(i - 1).getSize();
                    dp[i][w] = Math.max(itemPriority + dp[i - 1][w - itemSize], dp[i - 1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
      
        int w = knapsackCapacity;
        selectedItems.clear(); // Clear the previously selected items
        for (int i = n; i > 0 && w > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                DownloadItem selectedItem = downloadItems.get(i - 1);
                selectedItems.add(selectedItem);
                System.out.println("=========counting THE ITEMS=========" + selectedItems.size());
                w -= selectedItem.getSize();
            }
        }
        
        System.out.println("=========optimize THE ITEMS=========" + selectedItems.size());
        return selectedItems;
    }

    public void downloadSelectedItems() {
        System.out.println("========= THE ITEMS=========" + selectedItems.size());
        for (DownloadItem item : selectedItems) {
            downloadFileFromUrl(item.getUrl());
        }
    }
	    public void downloadItem(DownloadItem item) {
	        String fileUrl = item.getUrl(); // Assuming the DownloadItem has a URL field
	        String fileName = item.getName(); // Assuming the DownloadItem has a name field
	        String savePath = "downloaded_files/" + fileName; // Adjust the save path as needed

	        try {
	            URL url = new URL(fileUrl);
	            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

	            int responseCode = httpConn.getResponseCode();

	            // Check if the HTTP response is successful
	            if (responseCode == HttpURLConnection.HTTP_OK) {
	                // Open an input stream from the connection
	                try (InputStream inputStream = httpConn.getInputStream();
	                     FileOutputStream outputStream = new FileOutputStream(savePath)) {

	                    byte[] buffer = new byte[BUFFER_SIZE];
	                    int bytesRead;

	                    // Read from the input stream and write to the output stream
	                    while ((bytesRead = inputStream.read(buffer)) != -1) {
	                        outputStream.write(buffer, 0, bytesRead);
	                    }

	                    System.out.println("Downloaded: " + fileName);
	                }
	            } else {
	                System.err.println("Failed to download: " + fileName);
	            }

	            httpConn.disconnect();
	        } catch (IOException e) {
	            e.printStackTrace();
	            System.err.println("Failed to download: " + fileName);
	        }
	    }	
	    
    @Async
    public CompletableFuture<Boolean> downloadFileFromUrl(String url) {
        try {
            // URL validation
            if (isValidURL(url)) {
                URL validURL = new URL(url);
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpGet httpGet = new HttpGet(url);
                    try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                        if (response.getStatusLine().getStatusCode() == 200) {
                            String fileName = getFileNameFromUrl(url);
                            String saveDirectory = "C:\\Users\\USER\\Downloads\\" + fileName;

                            try (InputStream inputStream = response.getEntity().getContent();
                                 FileOutputStream outputStream = new FileOutputStream(saveDirectory)) {

                                byte[] buffer = new byte[8192];
                                int bytesRead;
                                long totalBytes = response.getEntity().getContentLength();
                                long downloadedBytes = 0;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                    downloadedBytes += bytesRead;
                                    double progress = ((double) downloadedBytes / totalBytes) * 100;
                                    updateDownloadProgress(url, progress);
                                }

                                System.out.println("File downloaded successfully.");
                                return CompletableFuture.completedFuture(true);
                            }
                        } else {
                            System.err.println("Download failed: " + response.getStatusLine().getReasonPhrase());
                            return CompletableFuture.completedFuture(false);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Download failed.");
                }
            } else {
                System.err.println("Invalid URL: " + url);
                return CompletableFuture.completedFuture(false);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.err.println("Malformed URL: " + url);
            return CompletableFuture.completedFuture(false);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(false);
        }
        return CompletableFuture.completedFuture(false);
    }

    // URL validation
    private boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    private String getFileNameFromUrl(String url) {
        // Extract the file name from the URL (e.g., http://example.com/file.txt)
        String[] parts = url.split("/");
        return parts[parts.length - 1];
    }

    private void updateDownloadProgress(String url, double progress) {
        // Implement the logic to update the download progress, e.g., send it to the user interface
        System.out.println("Download progress for " + url + ": " + progress + "%");
    }
}
