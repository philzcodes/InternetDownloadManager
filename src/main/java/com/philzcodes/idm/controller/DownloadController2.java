package com.philzcodes.idm.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;

import com.philzcodes.idm.service.DownloadService;

@Controller
@RequestMapping("/download")
public class DownloadController2 {
	
	@Autowired
	DownloadService downloadService;

    @GetMapping
    public String downloadPage() {
        return "download";
    }
    @PostMapping
    public DeferredResult<String> downloadFile(@RequestParam String url) {
        DeferredResult<String> deferredResult = new DeferredResult<>();

        CompletableFuture<Boolean> downloadTask = downloadService.downloadFileFromUrl(url);

        downloadTask.thenAccept(success -> {
            if (success) {
                deferredResult.setResult("download-success");
            } else {
                deferredResult.setErrorResult("download-failed");
            }
        });

        return deferredResult;
    }
    
}
