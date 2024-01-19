package com.philzcodes.idm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import com.philzcodes.idm.model.DownloadItem;
import com.philzcodes.idm.service.DownloadService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

//@RestController
//@RequestMapping("/api/downloads")
public class DownloadControllerApi {

//    @Autowired
//    private DownloadService knapsackService;
//
//    @PostMapping("/add")
//    public void addDownloadItem(@RequestBody DownloadItem item) {
//        // Add the download item to the knapsackService for processing
//        knapsackService.addDownloadItem(item);
//    }
//    
//    @PostMapping("/add-list")
//    public void addDownloadItems(@RequestBody List<DownloadItem> items) {
//    	 knapsackService.addAllDownloadItem(items);
//    }
//
//    
//    @PostMapping("/start")
//    public void startDownloads() {
//        knapsackService.downloadSelectedItems();
//    }
//
//    @GetMapping("/items")
//    public List<DownloadItem> getDownloadItems() {
//        return knapsackService.getDownloadItems();
//    }
//
//    @PostMapping
//    public DeferredResult<String> downloadFile(@RequestParam String url) {
//        DeferredResult<String> deferredResult = new DeferredResult<>();
//
//        CompletableFuture<Boolean> downloadTask = knapsackService.downloadFileFromUrl(url);
//
//        downloadTask.thenAccept(success -> {
//            if (success) {
//                deferredResult.setResult("download-success");
//            } else {
//                deferredResult.setErrorResult("download-failed");
//            }
//        });
//
//        return deferredResult;
//    }
    // Add endpoints for pausing, resuming, tracking downloads, etc.
}
