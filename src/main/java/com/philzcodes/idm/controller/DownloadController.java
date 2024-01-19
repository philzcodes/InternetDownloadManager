package com.philzcodes.idm.controller;

import com.philzcodes.idm.model.User;
import com.philzcodes.idm.model.DownloadItem;
import com.philzcodes.idm.service.DownloadService;
import com.philzcodes.idm.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
public class DownloadController {

    @Autowired
    private DownloadService downloadService;
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String index(Model model) {
        List<DownloadItem> downloadItems = downloadService.getDownloadItems();
        model.addAttribute("downloadItems", downloadItems);
//        List<DownloadItem> downloadItemsSorted = downloadService.getDownloadItemsSorted();
//        model.addAttribute("downloadItemsSorted", downloadItemsSorted);
        return "index";
    }

    @PostMapping("/addDownloadItem")
    public String addDownloadItem(DownloadItem item) {
        downloadService.addDownloadItem(item);
        return "redirect:/dashboard";
    }

    @PostMapping("/addAllDownloadItems")
    public String addAllDownloadItems(@RequestParam("items") String items, Model model) {
        List<DownloadItem> downloadItemList = parseCSV(items);

        if (downloadItemList == null) {
            // Handle invalid CSV format
            model.addAttribute("error", "Invalid CSV format");
            return "error";
        }

        downloadService.addAllDownloadItem(downloadItemList);
        return "redirect:/dashboard";
    }

    private List<DownloadItem> parseCSV(String csvData) {
        try {
            List<String> lines = Arrays.asList(csvData.split("\\n"));
            System.out.println("Number of lines: " + lines.size());  // Debugging

            return lines.stream()
                    .map(line -> {
                        System.out.println("Parsing line: " + line);  // Debugging
                        String[] fields = line.split(",");
                        if (fields.length == 4) {
                            try {
                                return new DownloadItem(
                                        fields[0].trim(),
                                        Integer.parseInt(fields[1].trim()),
                                        Integer.parseInt(fields[2].trim()),
                                        fields[3].trim()
                                );
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing numbers in line: " + line);  // Debugging
                                return null; // Invalid CSV line
                            }
                        } else {
                            System.err.println("Invalid CSV line: " + line);  // Debugging
                            return null; // Invalid CSV line
                        }
                    })
                    .filter(downloadItem -> {
                        System.out.println("Filtered item: " + downloadItem);  // Debugging
                        return downloadItem != null;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();  // Debugging
            return null; // Handle exceptions (e.g., NumberFormatException) as needed
        }
    }

    
//    @PostMapping("/addAllDownloadItems")
//    public String addAllDownloadItems(List<DownloadItem> items) {
//        downloadService.addAllDownloadItem(items);
//        return "redirect:/dashboard";
//    }

    @GetMapping("/startDownloads")
    public String startDownloads() {
      //  List<CompletableFuture<Boolean>> downloadResults = downloadService.downloadSelectedItems();
    	downloadService.downloadSelectedItems();
        return "redirect:/dashboard";
    }
    
    
	@GetMapping("/")
	public String login1() {
		return "login";
	}
    
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "logout";
	}
	
	@GetMapping("/register")
	public String signup(Model model) {
		User user= new User();
		model.addAttribute("user", user);
		return "register";
	}
	
	@PostMapping("/register")
	public String saveUser(@ModelAttribute("user") User user) {
		
		userService.saveUser(user);
		return "redirect:/login";
		
	}
}
